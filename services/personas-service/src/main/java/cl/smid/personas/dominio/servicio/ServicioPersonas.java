package cl.smid.personas.dominio.servicio;

import cl.smid.personas.dominio.excepcion.PersonaNoEncontradaException;
import cl.smid.personas.dominio.excepcion.RutDuplicadoException;
import cl.smid.personas.dominio.modelo.ClaveDedup;
import cl.smid.personas.dominio.modelo.Contacto;
import cl.smid.personas.dominio.modelo.ContextoTerritorial;
import cl.smid.personas.dominio.modelo.CriterioDuplicados;
import cl.smid.personas.dominio.modelo.DatosPersona;
import cl.smid.personas.dominio.modelo.EventoDominio;
import cl.smid.personas.dominio.modelo.Pagina;
import cl.smid.personas.dominio.modelo.Persona;
import cl.smid.personas.dominio.modelo.ResultadoDuplicados;
import cl.smid.personas.dominio.modelo.Rut;
import cl.smid.personas.dominio.puerto.entrada.GestionPersonasUseCase;
import cl.smid.personas.dominio.puerto.salida.BuscadorDuplicados;
import cl.smid.personas.dominio.puerto.salida.EventoPublicador;
import cl.smid.personas.dominio.puerto.salida.GeneradorAltKey;
import cl.smid.personas.dominio.puerto.salida.PersonaRepositorio;
import cl.smid.personas.dominio.puerto.salida.RelojDominio;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Implementación del caso de uso de gestión de personas (Núcleo 5.4/5.5). Es un POJO puro:
 * recibe sus colaboradores (puertos) por constructor y no conoce Spring. La demarcación
 * transaccional la aporta el controlador; aquí sólo vive la lógica de negocio.
 *
 * <p>Responsabilidades clave: validación/normalización de RUT, unicidad global de RUT, cálculo
 * del hash de deduplicación, filtro territorial registro a registro y emisión de eventos de
 * dominio con metadatos no sensibles.</p>
 */
public final class ServicioPersonas implements GestionPersonasUseCase {

    private final PersonaRepositorio repositorio;
    private final BuscadorDuplicados buscadorDuplicados;
    private final EventoPublicador eventoPublicador;
    private final RelojDominio reloj;
    private final GeneradorAltKey generadorAltKey;

    public ServicioPersonas(PersonaRepositorio repositorio,
                            BuscadorDuplicados buscadorDuplicados,
                            EventoPublicador eventoPublicador,
                            RelojDominio reloj,
                            GeneradorAltKey generadorAltKey) {
        this.repositorio = repositorio;
        this.buscadorDuplicados = buscadorDuplicados;
        this.eventoPublicador = eventoPublicador;
        this.reloj = reloj;
        this.generadorAltKey = generadorAltKey;
    }

    // ----------------------------------------------------------------------
    //  Alta
    // ----------------------------------------------------------------------
    @Override
    public Persona crear(DatosPersona datos, ContextoTerritorial ctx) {
        Instant ahora = reloj.ahora();

        // 1) RUT (si viene): valida módulo 11 y verifica unicidad global vigente.
        Rut rut = resolverRut(datos.rut());
        if (rut != null) {
            repositorio.buscarVigentePorRutGlobal(rut.canonico()).ifPresent(existente -> {
                throw new RutDuplicadoException(rut.canonico());
            });
        }

        // 2) Clave de deduplicación difusa.
        ClaveDedup clave = ClaveDedup.desde(
                datos.apellidoPaterno(), datos.apellidoMaterno(), datos.nombres(), datos.fechaNacimiento());

        // 3) Construcción del agregado, estampando territorio y autoría desde el contexto.
        Persona nueva = Persona.builder()
                .altKey(generadorAltKey.nuevo())
                .tipo(datos.tipo())
                .rut(rut == null ? null : rut.canonico())
                .dv(rut == null ? null : String.valueOf(rut.dv()))
                .nombres(limpiar(datos.nombres()))
                .apellidoPaterno(limpiar(datos.apellidoPaterno()))
                .apellidoMaterno(limpiar(datos.apellidoMaterno()))
                .razonSocial(limpiar(datos.razonSocial()))
                .fechaNacimiento(datos.fechaNacimiento())
                .sexo(datos.sexo())
                .nacionalidad(limpiar(datos.nacionalidad()))
                .hashDedup(clave.hash())
                .idSede(ctx.idSede())
                .idUnidad(ctx.idUnidad())
                .vigente(true)
                .creadoEn(ahora)
                .actualizadoEn(ahora)
                .creadoPor(ctx.actor())
                .contactos(datos.contactos() == null ? List.of() : datos.contactos())
                .construir();

        Persona guardada = repositorio.guardar(nueva);
        publicar("persona.creada", guardada, ctx, ahora);
        return guardada;
    }

    // ----------------------------------------------------------------------
    //  Actualización (partial-merge)
    // ----------------------------------------------------------------------
    @Override
    public Persona actualizar(String altKey, DatosPersona datos, ContextoTerritorial ctx) {
        Instant ahora = reloj.ahora();

        Persona actual = obtenerVisibleOError(altKey, ctx);

        // RUT: si viene informado, revalida y controla unicidad contra OTRA persona.
        String rutFinal = actual.rut();
        String dvFinal = actual.dv();
        if (datos.rut() != null) {
            if (datos.rut().isBlank()) {
                // Permite limpiar el RUT explícitamente con cadena en blanco.
                rutFinal = null;
                dvFinal = null;
            } else {
                Rut rut = Rut.normalizarYValidar(datos.rut());
                Optional<Persona> conMismoRut = repositorio.buscarVigentePorRutGlobal(rut.canonico());
                if (conMismoRut.isPresent() && !conMismoRut.get().altKey().equals(actual.altKey())) {
                    throw new RutDuplicadoException(rut.canonico());
                }
                rutFinal = rut.canonico();
                dvFinal = String.valueOf(rut.dv());
            }
        }

        // Campos de identidad: sólo cambian los no nulos (el tipo no se modifica en edición).
        String nombres = datos.nombres() != null ? limpiar(datos.nombres()) : actual.nombres();
        String apPaterno = datos.apellidoPaterno() != null ? limpiar(datos.apellidoPaterno()) : actual.apellidoPaterno();
        String apMaterno = datos.apellidoMaterno() != null ? limpiar(datos.apellidoMaterno()) : actual.apellidoMaterno();
        String razonSocial = datos.razonSocial() != null ? limpiar(datos.razonSocial()) : actual.razonSocial();
        var fechaNac = datos.fechaNacimiento() != null ? datos.fechaNacimiento() : actual.fechaNacimiento();
        var sexo = datos.sexo() != null ? datos.sexo() : actual.sexo();
        String nacionalidad = datos.nacionalidad() != null ? limpiar(datos.nacionalidad()) : actual.nacionalidad();

        // Contactos: lista no nula reemplaza el conjunto; lista nula mantiene el existente.
        List<Contacto> contactos = datos.contactos() != null ? datos.contactos() : actual.contactos();

        // Recalcular hash de deduplicación con los valores resultantes del merge.
        ClaveDedup clave = ClaveDedup.desde(apPaterno, apMaterno, nombres, fechaNac);

        Persona actualizada = actual.copia()
                .rut(rutFinal)
                .dv(dvFinal)
                .nombres(nombres)
                .apellidoPaterno(apPaterno)
                .apellidoMaterno(apMaterno)
                .razonSocial(razonSocial)
                .fechaNacimiento(fechaNac)
                .sexo(sexo)
                .nacionalidad(nacionalidad)
                .hashDedup(clave.hash())
                .contactos(contactos)
                .actualizadoEn(ahora)
                .construir();

        Persona guardada = repositorio.guardar(actualizada);
        publicar("persona.actualizada", guardada, ctx, ahora);
        return guardada;
    }

    // ----------------------------------------------------------------------
    //  Consultas
    // ----------------------------------------------------------------------
    @Override
    public Persona obtener(String altKey, ContextoTerritorial ctx) {
        return obtenerVisibleOError(altKey, ctx);
    }

    @Override
    public Pagina<Persona> buscarPorRut(String rut, ContextoTerritorial ctx) {
        // Un RUT malformado es un error de cliente en cualquier endpoint (PER-002).
        Rut normalizado = Rut.normalizarYValidar(rut);
        return repositorio.buscarVigentePorRutEnAlcance(normalizado.canonico(), ctx)
                .map(p -> new Pagina<>(List.of(p), 0, 1, 1L))
                .orElseGet(() -> Pagina.vacia(0, 1));
    }

    @Override
    public Pagina<Persona> buscarPorNombre(String termino, ContextoTerritorial ctx, int pagina, int tamano) {
        return repositorio.buscarPorNombreEnAlcance(termino, ctx, pagina, tamano);
    }

    @Override
    public ResultadoDuplicados buscarDuplicados(CriterioDuplicados criterio) {
        return buscadorDuplicados.buscar(criterio);
    }

    // ----------------------------------------------------------------------
    //  Apoyo interno
    // ----------------------------------------------------------------------

    /** Obtiene la persona y aplica el filtro territorial; 404 uniforme si no es visible. */
    private Persona obtenerVisibleOError(String altKey, ContextoTerritorial ctx) {
        Persona p = repositorio.buscarPorAltKey(altKey)
                .orElseThrow(() -> new PersonaNoEncontradaException(altKey));
        if (!EvaluadorAlcance.dentroDeAlcance(p.idSede(), p.idUnidad(), ctx)) {
            // Fuera de alcance = inexistente para el solicitante (no se revela su existencia).
            throw new PersonaNoEncontradaException(altKey);
        }
        return p;
    }

    /** Normaliza y valida el RUT si está presente; devuelve {@code null} si no viene. */
    private static Rut resolverRut(String rutEntrada) {
        if (rutEntrada == null || rutEntrada.isBlank()) {
            return null;
        }
        return Rut.normalizarYValidar(rutEntrada);
    }

    /** Recorta espacios y convierte cadenas en blanco a {@code null}. */
    private static String limpiar(String s) {
        if (s == null) {
            return null;
        }
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }

    /**
     * Publica un evento de dominio con metadatos NO sensibles (G7): tipo de persona y si tenía
     * RUT. Nunca nombres, RUT ni contactos.
     */
    private void publicar(String nombre, Persona p, ContextoTerritorial ctx, Instant ahora) {
        Map<String, Object> datos = new LinkedHashMap<>();
        datos.put("tipo", p.tipo() == null ? null : p.tipo().name());
        datos.put("conRut", p.rut() != null);
        EventoDominio evento = new EventoDominio(
                nombre, ahora, ctx.actor(), p.altKey(), ctx.idSede(), ctx.idUnidad(), datos);
        eventoPublicador.publicar(evento);
    }
}
