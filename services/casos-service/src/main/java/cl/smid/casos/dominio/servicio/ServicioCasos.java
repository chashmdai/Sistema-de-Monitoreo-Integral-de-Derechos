package cl.smid.casos.dominio.servicio;

import cl.smid.casos.dominio.excepcion.AccesoDenegadoException;
import cl.smid.casos.dominio.excepcion.CasoNoEncontradoException;
import cl.smid.casos.dominio.modelo.Caso;
import cl.smid.casos.dominio.modelo.CasoEnriquecido;
import cl.smid.casos.dominio.modelo.ContextoTerritorial;
import cl.smid.casos.dominio.modelo.DatosEnriquecimiento;
import cl.smid.casos.dominio.modelo.EstadoCaso;
import cl.smid.casos.dominio.modelo.EventoRequerimientoAsignado;
import cl.smid.casos.dominio.modelo.FiltroCasos;
import cl.smid.casos.dominio.modelo.NumeroExpediente;
import cl.smid.casos.dominio.modelo.Pagina;
import cl.smid.casos.dominio.modelo.SerieExpediente;
import cl.smid.casos.dominio.modelo.Transicion;
import cl.smid.casos.dominio.puerto.entrada.ConsultarCasos;
import cl.smid.casos.dominio.puerto.entrada.MaterializarCaso;
import cl.smid.casos.dominio.puerto.entrada.TransicionarCaso;
import cl.smid.casos.dominio.puerto.salida.ClienteRequerimientos;
import cl.smid.casos.dominio.puerto.salida.CorrelativoExpedientePort;
import cl.smid.casos.dominio.puerto.salida.DirectorioSedes;
import cl.smid.casos.dominio.puerto.salida.GeneradorIdentificadores;
import cl.smid.casos.dominio.puerto.salida.PublicadorEventos;
import cl.smid.casos.dominio.puerto.salida.PublicadorEventos.EventoDominio;
import cl.smid.casos.dominio.puerto.salida.Reloj;
import cl.smid.casos.dominio.puerto.salida.RepositorioCasos;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Servicio de aplicación del dominio de Casos: orquesta los casos de uso de materialización, consulta
 * y transición. Es un POJO PURO (sin Spring/JPA/Lombok); se cablea en {@code config.CableadoDominio}.
 *
 * <p>Implementa los tres puertos de entrada del dominio. La demarcación transaccional NO vive aquí:
 * la aplican los adaptadores de entrada (controlador REST y listener de eventos), tal como exige la
 * arquitectura. Este servicio asume estar ejecutándose dentro de una transacción cuando escribe.</p>
 */
public final class ServicioCasos implements MaterializarCaso, ConsultarCasos, TransicionarCaso {

    private final RepositorioCasos repositorio;
    private final CorrelativoExpedientePort correlativo;
    private final DirectorioSedes directorioSedes;
    private final PublicadorEventos publicador;
    private final ClienteRequerimientos clienteRequerimientos;
    private final Reloj reloj;
    private final GeneradorIdentificadores generador;
    private final MaquinaEstadosCaso maquina;
    private final GeneradorNumeroExpediente generadorNumero;
    private final EvaluadorAlcance evaluadorAlcance;
    private final Set<String> rolesCoordinacion;
    private final LocalDate inicioOficialExpediente;

    public ServicioCasos(RepositorioCasos repositorio, CorrelativoExpedientePort correlativo,
                         DirectorioSedes directorioSedes, PublicadorEventos publicador,
                         ClienteRequerimientos clienteRequerimientos, Reloj reloj,
                         GeneradorIdentificadores generador, MaquinaEstadosCaso maquina,
                         GeneradorNumeroExpediente generadorNumero, EvaluadorAlcance evaluadorAlcance,
                         Set<String> rolesCoordinacion, LocalDate inicioOficialExpediente) {
        this.repositorio = Objects.requireNonNull(repositorio);
        this.correlativo = Objects.requireNonNull(correlativo);
        this.directorioSedes = Objects.requireNonNull(directorioSedes);
        this.publicador = Objects.requireNonNull(publicador);
        this.clienteRequerimientos = Objects.requireNonNull(clienteRequerimientos);
        this.reloj = Objects.requireNonNull(reloj);
        this.generador = Objects.requireNonNull(generador);
        this.maquina = Objects.requireNonNull(maquina);
        this.generadorNumero = Objects.requireNonNull(generadorNumero);
        this.evaluadorAlcance = Objects.requireNonNull(evaluadorAlcance);
        this.rolesCoordinacion = Set.copyOf(Objects.requireNonNull(rolesCoordinacion));
        this.inicioOficialExpediente = Objects.requireNonNull(inicioOficialExpediente);
    }

    // ============================ Materialización (consumo de eventos) ============================

    /**
     * Materializa el Caso "esqueleto" desde el evento. Idempotente:
     * <ol>
     *   <li><strong>Fast-path:</strong> si ya existe un caso para el requerimiento de origen, no-op
     *       (devuelve el existente, sin reservar correlativo ni emitir evento).</li>
     *   <li><strong>Carrera:</strong> si dos consumidores procesan el mismo evento a la vez, la clave
     *       única de {@code id_requerimiento_origen_alt} provoca un conflicto al insertar el segundo;
     *       se captura como {@link CasoYaMaterializadoException} y se trata como no-op.</li>
     * </ol>
     *
     * <p>El caso se crea SOLO con referencias {@code alt_key} del evento (estrategia esqueleto): no se
     * consulta a Requerimientos/Personas porque el listener no porta token de usuario. El
     * enriquecimiento se difiere a la consulta on-demand.</p>
     */
    @Override
    public Caso materializar(EventoRequerimientoAsignado evento) {
        Objects.requireNonNull(evento, "evento");

        Optional<Caso> existente = repositorio.buscarPorRequerimientoOrigen(evento.requerimientoOrigenAlt());
        if (existente.isPresent()) {
            return existente.get(); // no-op idempotente (reentrega secuencial del mismo evento)
        }

        Instant ahora = reloj.ahora();
        int anio = anioDe(ahora);
        SerieExpediente serie = determinarSerie(evento, ahora);

        // Reserva atómica del correlativo por (sede, año, serie). Participa de la transacción del
        // listener: si el alta del caso se revierte, también se revierte el correlativo (sin huecos).
        long numeroSerie = correlativo.reservarSiguiente(evento.idSedeAlt(), anio, serie);
        String codigoSede = directorioSedes.codigoDe(evento.idSedeAlt());
        NumeroExpediente numero = generadorNumero.generar(codigoSede, serie, numeroSerie, anio);

        Caso caso = Caso.materializar(generador.nuevoAltKey(), generador.nuevoAltKey(), numero, evento, ahora);

        // El adaptador traduce la violación de unicidad de id_requerimiento_origen_alt en
        // CasoYaMaterializadoException. En el único escenario donde esto ocurre (dos consumidores
        // procesando el MISMO evento simultáneamente, p. ej. varias instancias), la excepción se
        // propaga al listener; su política de reintento la reejecuta y el pre-chequeo de arriba la
        // resuelve entonces como no-op. Aquí NO se consulta tras el fallo: la transacción quedó
        // marcada para rollback y reconsultarla sería incorrecto.
        Caso guardado = repositorio.guardar(caso);

        publicarEvento("caso.abierto", guardado);
        return guardado;
    }

    // ================================== Consulta ==================================

    @Override
    public CasoEnriquecido detalle(String altKey, ContextoTerritorial ctx, String tokenBearer) {
        Caso caso = cargarVisible(altKey, ctx);
        // Enriquecimiento on-demand con el token del usuario (tolerante, solo metadatos G7).
        DatosEnriquecimiento enriquecimiento =
                clienteRequerimientos.enriquecer(caso.idRequerimientoOrigenAlt(), tokenBearer);
        return new CasoEnriquecido(caso, enriquecimiento);
    }

    @Override
    public Pagina<Caso> listar(ContextoTerritorial ctx, EstadoCaso estado, String unidadFiltroAlt,
                               int pagina, int tamano) {
        FiltroCasos filtro = FiltroCasos.desde(ctx, estado, unidadFiltroAlt);
        return repositorio.listar(filtro, pagina, tamano);
    }

    // ================================== Transición ==================================

    @Override
    public Caso transicionar(ComandoTransicion comando, ContextoTerritorial ctx) {
        Objects.requireNonNull(comando, "comando");
        Objects.requireNonNull(ctx, "ctx");

        // 1) Cargar respetando alcance territorial (fuera de alcance => 404, no se revela existencia).
        Caso caso = cargarVisible(comando.casoAltKey(), ctx);

        // 2) Facultad: las acciones administrativas exigen rol de Coordinación (=> 403 si falta).
        if (comando.accion().esAdministrativa() && !ctx.tieneAlgunRol(rolesCoordinacion)) {
            throw new AccesoDenegadoException(
                    "La acción '" + comando.accion() + "' requiere rol de Coordinación.");
        }

        // 3) Validar la transición contra la máquina de estados (=> CAS-409 si es inválida).
        EstadoCaso destino = maquina.transicionar(caso.estado(), comando.accion());

        // 4) Aplicar y persistir, dejando asiento de historial atribuido al usuario.
        Instant ahora = reloj.ahora();
        Transicion transicion = new Transicion(generador.nuevoAltKey(), caso.estado(), destino,
                comando.accion().name(), comando.observacion(), ctx.sujetoAlt(), ahora);
        caso.aplicar(transicion);
        Caso guardado = repositorio.guardar(caso);

        // 5) Emitir evento de dominio según el destino (tolerante a fallos).
        publicarEvento(tipoEventoPara(destino), guardado);
        return guardado;
    }

    // ================================== Auxiliares ==================================

    /** Carga el caso y verifica visibilidad territorial; si no procede, 404 indistinguible. */
    private Caso cargarVisible(String altKey, ContextoTerritorial ctx) {
        Caso caso = repositorio.buscarPorAltKey(altKey)
                .orElseThrow(() -> new CasoNoEncontradoException(altKey));
        if (!evaluadorAlcance.puedeVer(caso, ctx)) {
            throw new CasoNoEncontradoException(altKey);
        }
        return caso;
    }

    /** Determina la serie del expediente: bandera del evento si viene; si no, política por fecha. */
    private SerieExpediente determinarSerie(EventoRequerimientoAsignado evento, Instant ahora) {
        if (evento.esBeta() != null) {
            return evento.esBeta() ? SerieExpediente.BETA : SerieExpediente.OFICIAL;
        }
        LocalDate hoy = LocalDate.ofInstant(ahora, ZoneOffset.UTC);
        return hoy.isBefore(inicioOficialExpediente) ? SerieExpediente.BETA : SerieExpediente.OFICIAL;
    }

    private int anioDe(Instant ahora) {
        return LocalDate.ofInstant(ahora, ZoneOffset.UTC).getYear();
    }

    private String tipoEventoPara(EstadoCaso destino) {
        return switch (destino) {
            case CERRADO -> "caso.cerrado";
            case ARCHIVADO -> "caso.archivado";
            default -> "caso.estado_cambiado";
        };
    }

    /** Construye y publica el evento de dominio con metadatos NO sensibles (G7). */
    private void publicarEvento(String tipo, Caso caso) {
        Map<String, Object> metadatos = new LinkedHashMap<>();
        metadatos.put("numeroExpediente", caso.numeroExpediente().valor());
        metadatos.put("estado", caso.estado().name());
        metadatos.put("idSede", caso.idSedeAlt());
        metadatos.put("idUnidad", caso.idUnidadAlt());
        metadatos.put("complejidad", caso.complejidad() == null ? null : caso.complejidad().name());
        metadatos.put("requiereFichaReservada", caso.requiereFichaReservada());
        metadatos.put("esBeta", caso.esBeta());
        publicador.publicar(new EventoDominio(
                tipo, caso.altKey(), reloj.ahora(), caso.idSedeAlt(), caso.idUnidadAlt(), metadatos));
    }
}
