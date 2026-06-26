package cl.smid.requerimientos.dominio.servicio;

import cl.smid.requerimientos.dominio.excepcion.RecursoNoEncontrado;
import cl.smid.requerimientos.dominio.excepcion.ReglaNegocioViolada;
import cl.smid.requerimientos.dominio.excepcion.SolicitudInvalida;
import cl.smid.requerimientos.dominio.modelo.Admisibilidad;
import cl.smid.requerimientos.dominio.modelo.Complejidad;
import cl.smid.requerimientos.dominio.modelo.DerechoVulnerado;
import cl.smid.requerimientos.dominio.modelo.EventoDominio;
import cl.smid.requerimientos.dominio.modelo.Folio;
import cl.smid.requerimientos.dominio.modelo.NnaAfectado;
import cl.smid.requerimientos.dominio.modelo.PaginaDominio;
import cl.smid.requerimientos.dominio.modelo.Requerimiento;
import cl.smid.requerimientos.dominio.modelo.SnapshotPersona;
import cl.smid.requerimientos.dominio.puerto.entrada.GestionRequerimientosUseCase;
import cl.smid.requerimientos.dominio.puerto.salida.CatalogoDerechos;
import cl.smid.requerimientos.dominio.puerto.salida.DirectorioIdentidad;
import cl.smid.requerimientos.dominio.puerto.salida.DirectorioPersonas;
import cl.smid.requerimientos.dominio.puerto.salida.EventoPublicador;
import cl.smid.requerimientos.dominio.puerto.salida.GeneradorAltKey;
import cl.smid.requerimientos.dominio.puerto.salida.PersonaResuelta;
import cl.smid.requerimientos.dominio.puerto.salida.ProfesionalIdentidad;
import cl.smid.requerimientos.dominio.puerto.salida.RelojDominio;
import cl.smid.requerimientos.dominio.puerto.salida.RequerimientoRepositorio;

import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Implementación del caso de uso {@link GestionRequerimientosUseCase} (Núcleo 6.3). Orquesta los
 * puertos de salida, aplica la máquina de estados y el generador de folio, verifica referencias
 * por alt_key contra Personas y Catálogo, valida la pertenencia del profesional contra Identidad y
 * emite los eventos de dominio (solo metadatos, G7).
 *
 * <p>Es <b>dominio puro</b>: no conoce Spring, HTTP ni JPA. La transaccionalidad la demarca el
 * controlador; aquí solo se coordina la lógica. Se cablea como bean en la raíz de composición.</p>
 */
public class ServicioRequerimientos implements GestionRequerimientosUseCase {

    // Claves de enrutamiento de los eventos de dominio (topic exchange smid.eventos).
    private static final String EVENTO_INGRESADO = "requerimiento.ingresado";
    private static final String EVENTO_CLASIFICADO = "requerimiento.clasificado";
    private static final String EVENTO_INADMISIBLE = "requerimiento.inadmisible";
    private static final String EVENTO_RESPONDIDO = "requerimiento.respondido";
    private static final String EVENTO_ASIGNADO = "requerimiento.asignado";

    private final RequerimientoRepositorio repositorio;
    private final GeneradorFolio generadorFolio;
    private final DirectorioPersonas directorioPersonas;
    private final CatalogoDerechos catalogoDerechos;
    private final DirectorioIdentidad directorioIdentidad;
    private final EventoPublicador eventoPublicador;
    private final RelojDominio reloj;
    private final GeneradorAltKey generadorAltKey;
    /** Si la verificación de pertenencia del profesional es estricta (vs. permisiva). */
    private final boolean verificacionProfesionalEstricta;

    @SuppressWarnings("java:S107") // Orquestador hexagonal: depende explícitamente de sus puertos.
    public ServicioRequerimientos(RequerimientoRepositorio repositorio,
                                  GeneradorFolio generadorFolio,
                                  DirectorioPersonas directorioPersonas,
                                  CatalogoDerechos catalogoDerechos,
                                  DirectorioIdentidad directorioIdentidad,
                                  EventoPublicador eventoPublicador,
                                  RelojDominio reloj,
                                  GeneradorAltKey generadorAltKey,
                                  boolean verificacionProfesionalEstricta) {
        this.repositorio = Objects.requireNonNull(repositorio);
        this.generadorFolio = Objects.requireNonNull(generadorFolio);
        this.directorioPersonas = Objects.requireNonNull(directorioPersonas);
        this.catalogoDerechos = Objects.requireNonNull(catalogoDerechos);
        this.directorioIdentidad = Objects.requireNonNull(directorioIdentidad);
        this.eventoPublicador = Objects.requireNonNull(eventoPublicador);
        this.reloj = Objects.requireNonNull(reloj);
        this.generadorAltKey = Objects.requireNonNull(generadorAltKey);
        this.verificacionProfesionalEstricta = verificacionProfesionalEstricta;
    }

    // ========================================================================
    //  USR.01 — Ingreso
    // ========================================================================

    @Override
    public Requerimiento crear(ComandoCrear cmd, ContextoUsuario ctx) {
        Objects.requireNonNull(cmd, "El comando de creación es obligatorio");
        Objects.requireNonNull(ctx, "El contexto del usuario es obligatorio");
        Instant momento = reloj.ahora();

        SnapshotPersona snapshotRequirente = resolverSnapshotSiPresente(cmd.idRequirenteAlt(), momento,
                "El requirente indicado no existe o no es accesible.");

        Folio folio = generadorFolio.generar(ctx.idSedeAlt(), momento, cmd.esBetaOverride());
        boolean esBeta = generadorFolio.esBetaEfectiva(momento, cmd.esBetaOverride());

        Requerimiento req = Requerimiento.crear(
                generadorAltKey.nuevo(), folio, ctx.idSedeAlt(), esBeta, ctx.usuarioAlt(),
                cmd.canal(), cmd.complejidad(), cmd.urgencia(), cmd.idUnidadDestinoAlt(),
                cmd.resumen(), cmd.idRequirenteAlt(), snapshotRequirente, momento);

        Requerimiento guardado = repositorio.guardar(req);

        // Si nace ya clasificado, se notifica la clasificación (costura FIR hacia 6.5).
        if (cmd.complejidad() != null) {
            publicarClasificado(guardado);
        }
        return guardado;
    }

    @Override
    public Requerimiento editar(String altKey, ComandoEditar cmd, ContextoUsuario ctx) {
        Objects.requireNonNull(cmd, "El comando de edición es obligatorio");
        Instant momento = reloj.ahora();
        Requerimiento req = cargarVisible(altKey, ctx);

        Complejidad complejidadPrevia = req.complejidad();

        SnapshotPersona snapshotRequirente = resolverSnapshotSiPresente(cmd.idRequirenteAlt(), momento,
                "El requirente indicado no existe o no es accesible.");

        req.editar(cmd.canal(), cmd.complejidad(), cmd.urgencia(), cmd.idUnidadDestinoAlt(),
                cmd.resumen(), cmd.idRequirenteAlt(), snapshotRequirente, momento);

        Requerimiento guardado = repositorio.guardar(req);

        // La clasificación es ajustable: cada cambio efectivo de complejidad emite un evento.
        if (cmd.complejidad() != null && cmd.complejidad() != complejidadPrevia) {
            publicarClasificado(guardado);
        }
        return guardado;
    }

    @Override
    public Requerimiento enviar(String altKey, ContextoUsuario ctx) {
        Instant momento = reloj.ahora();
        Requerimiento req = cargarVisible(altKey, ctx);
        req.enviar(momento);
        Requerimiento guardado = repositorio.guardar(req);
        eventoPublicador.publicar(EventoDominio.de(EVENTO_INGRESADO, guardado.altKey(), momento,
                metadatosBase(guardado)));
        return guardado;
    }

    @Override
    public Requerimiento agregarNna(String altKey, ComandoAgregarNna cmd, ContextoUsuario ctx) {
        Objects.requireNonNull(cmd, "El comando de alta de NNA es obligatorio");
        Instant momento = reloj.ahora();
        Requerimiento req = cargarVisible(altKey, ctx);

        // Resolver el NNA contra Personas (toma snapshot de resiliencia).
        PersonaResuelta persona = directorioPersonas.resolver(cmd.idPersonaAlt())
                .orElseThrow(() -> new SolicitudInvalida(
                        "El NNA indicado no existe o no es accesible."));
        SnapshotPersona snapshot = SnapshotPersona.de(persona.nombreLegible(), persona.rut(), momento);

        // Verificar cada derecho (y su causa) contra el Catálogo.
        List<DerechoVulnerado> derechos = new ArrayList<>();
        for (DerechoSolicitado d : cmd.derechos()) {
            verificarDerecho(d);
            derechos.add(DerechoVulnerado.nuevo(d.idDerechoAlt(), d.idCausaAlt()));
        }

        NnaAfectado nna = NnaAfectado.nuevo(cmd.idPersonaAlt(), snapshot, derechos);
        req.agregarNna(nna, momento);
        return repositorio.guardar(req);
    }

    // ========================================================================
    //  USR.02 — Admisibilidad
    // ========================================================================

    @Override
    public Requerimiento decidirAdmisibilidad(String altKey, ComandoAdmisibilidad cmd, ContextoUsuario ctx) {
        Objects.requireNonNull(cmd, "El comando de admisibilidad es obligatorio");
        Instant momento = reloj.ahora();
        Requerimiento req = cargarVisible(altKey, ctx);

        // Para ASIGNACION: validar pertenencia del profesional a la unidad de destino.
        if (cmd.accion() == cl.smid.requerimientos.dominio.modelo.AccionAdmisibilidad.ASIGNACION) {
            validarProfesionalEnUnidad(cmd.idProfesionalAsignadoAlt(), req.idUnidadDestinoAlt());
        }

        // Apertura automática de la admisibilidad si el requerimiento aún está INGRESADO.
        if (req.estado() == cl.smid.requerimientos.dominio.modelo.EstadoRequerimiento.INGRESADO) {
            req.abrirAdmisibilidad(momento);
        }

        Admisibilidad decision = new Admisibilidad(
                null, cmd.accion(), ctx.usuarioAlt(), cmd.escaladoADefensora(),
                cmd.idProfesionalAsignadoAlt(), cmd.observacion(), momento);

        req.decidir(decision, momento);
        Requerimiento guardado = repositorio.guardar(req);

        publicarEventoDecision(guardado, decision, momento);
        return guardado;
    }

    // ========================================================================
    //  Lecturas
    // ========================================================================

    @Override
    public Requerimiento obtener(String altKey, ContextoUsuario ctx) {
        return cargarVisible(altKey, ctx);
    }

    @Override
    public PaginaDominio<Requerimiento> listar(FiltroListado filtro, ContextoUsuario ctx) {
        Objects.requireNonNull(filtro, "El filtro de listado es obligatorio");
        Objects.requireNonNull(ctx, "El contexto del usuario es obligatorio");
        return repositorio.listar(filtro.estado(), filtro.idUnidadDestinoAlt(), ctx.alcance(),
                ctx.idSedeAlt(), ctx.idUnidadAlt(), filtro.pagina(), filtro.tamano());
    }

    // ========================================================================
    //  Apoyo interno
    // ========================================================================

    /**
     * Carga un requerimiento por alt_key y verifica que sea visible para el usuario (Núcleo 2.3).
     * Fuera de alcance => 404 (no se revela su existencia).
     */
    private Requerimiento cargarVisible(String altKey, ContextoUsuario ctx) {
        Objects.requireNonNull(altKey, "El alt_key es obligatorio");
        Objects.requireNonNull(ctx, "El contexto del usuario es obligatorio");
        Requerimiento req = repositorio.buscarPorAltKey(altKey)
                .orElseThrow(() -> new RecursoNoEncontrado(
                        "El requerimiento no existe o está fuera de su alcance."));
        boolean visible = EvaluadorAlcance.puedeVer(ctx.alcance(), ctx.idSedeAlt(), ctx.idUnidadAlt(),
                req.idSedeAlt(), req.idUnidadDestinoAlt());
        if (!visible) {
            throw new RecursoNoEncontrado("El requerimiento no existe o está fuera de su alcance.");
        }
        return req;
    }

    /** Resuelve un snapshot de persona si se indicó alt_key; si no se puede resolver, falla. */
    private SnapshotPersona resolverSnapshotSiPresente(String idPersonaAlt, Instant momento, String mensajeError) {
        if (idPersonaAlt == null || idPersonaAlt.isBlank()) {
            return null;
        }
        PersonaResuelta persona = directorioPersonas.resolver(idPersonaAlt)
                .orElseThrow(() -> new SolicitudInvalida(mensajeError));
        return SnapshotPersona.de(persona.nombreLegible(), persona.rut(), momento);
    }

    /** Verifica que un derecho (y su causa opcional) exista en el Catálogo. */
    private void verificarDerecho(DerechoSolicitado d) {
        if (d.idDerechoAlt() == null || d.idDerechoAlt().isBlank()) {
            throw new SolicitudInvalida("Cada derecho debe indicar su alt_key.");
        }
        if (!catalogoDerechos.existeDerecho(d.idDerechoAlt())) {
            throw new SolicitudInvalida(
                    "El derecho " + d.idDerechoAlt() + " no existe en el Catálogo.");
        }
        if (d.idCausaAlt() != null && !d.idCausaAlt().isBlank()
                && !catalogoDerechos.existeCausa(d.idDerechoAlt(), d.idCausaAlt())) {
            throw new SolicitudInvalida(
                    "La causa " + d.idCausaAlt() + " no existe bajo el derecho " + d.idDerechoAlt() + ".");
        }
    }

    /**
     * Valida que el profesional pertenezca a la unidad de destino del requerimiento.
     *
     * <p>Política conmutable: en modo {@code estricta} se exige resolver al profesional en Identidad
     * y que su unidad coincida; en modo {@code permisiva} se tolera la indisponibilidad del endpoint
     * de Identidad (costura: aún no estable). En ambos modos, si la unidad se resuelve y NO coincide,
     * se rechaza.</p>
     */
    private void validarProfesionalEnUnidad(String idProfesionalAlt, String unidadDestinoAlt) {
        if (idProfesionalAlt == null || idProfesionalAlt.isBlank()) {
            throw new ReglaNegocioViolada("La asignación exige el alt_key del profesional asignado.");
        }
        Optional<ProfesionalIdentidad> resuelto = directorioIdentidad.resolverProfesional(idProfesionalAlt);

        if (resuelto.isEmpty()) {
            if (verificacionProfesionalEstricta) {
                throw new ReglaNegocioViolada(
                        "No se pudo resolver al profesional en Identidad para validar su unidad.");
            }
            return; // Modo permisivo: se tolera la indisponibilidad de Identidad.
        }

        String unidadProfesional = resuelto.get().idUnidadAlt();
        boolean datosSuficientes = unidadProfesional != null && unidadDestinoAlt != null;
        if (datosSuficientes && !Objects.equals(unidadProfesional, unidadDestinoAlt)) {
            throw new ReglaNegocioViolada(
                    "El profesional asignado no pertenece a la unidad de destino del requerimiento.");
        }
        if (!datosSuficientes && verificacionProfesionalEstricta) {
            throw new ReglaNegocioViolada(
                    "Faltan datos de unidad para validar la pertenencia del profesional (modo estricto).");
        }
    }

    // --- Emisión de eventos (solo metadatos no sensibles, G7) ---

    private void publicarEventoDecision(Requerimiento req, Admisibilidad decision, Instant momento) {
        Map<String, Object> meta = metadatosBase(req);
        meta.put("accion", decision.accion().name());
        switch (decision.accion()) {
            case INADMISIBLE -> {
                meta.put("escaladoADefensora", decision.escaladoADefensora());
                eventoPublicador.publicar(EventoDominio.de(EVENTO_INADMISIBLE, req.altKey(), momento, meta));
            }
            // RESPUESTA_INMEDIATA: se REGISTRA y NO se envía comunicación saliente (heredado).
            case RESPUESTA_INMEDIATA ->
                    eventoPublicador.publicar(EventoDominio.de(EVENTO_RESPONDIDO, req.altKey(), momento, meta));
            case ASIGNACION -> {
                meta.put("idProfesionalAsignadoAlt", decision.idProfesionalAsignadoAlt());
                eventoPublicador.publicar(EventoDominio.de(EVENTO_ASIGNADO, req.altKey(), momento, meta));
            }
        }
    }

    private void publicarClasificado(Requerimiento req) {
        Map<String, Object> meta = metadatosBase(req);
        eventoPublicador.publicar(EventoDominio.de(EVENTO_CLASIFICADO, req.altKey(), req.actualizadoEn(), meta));
    }

    /**
     * Metadatos técnicos comunes de un requerimiento. <b>Nunca</b> incluye datos personales
     * (G7): solo estado, folio, banderas y clasificación.
     */
    private Map<String, Object> metadatosBase(Requerimiento req) {
        Map<String, Object> meta = new LinkedHashMap<>();
        meta.put("folio", req.folio().valor());
        meta.put("estado", req.estado().name());
        meta.put("idSede", req.idSedeAlt());
        if (req.idUnidadDestinoAlt() != null) {
            meta.put("idUnidadDestino", req.idUnidadDestinoAlt());
        }
        if (req.complejidad() != null) {
            meta.put("complejidad", req.complejidad().name());
        }
        meta.put("requiereFichaReservada", req.requiereFichaReservada());
        meta.put("esBeta", req.esBeta());
        return meta;
    }
}
