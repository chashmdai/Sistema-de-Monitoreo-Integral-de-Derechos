package cl.smid.requerimientos.dominio.modelo;

import cl.smid.requerimientos.dominio.excepcion.ReglaNegocioViolada;
import cl.smid.requerimientos.dominio.excepcion.TransicionInvalida;
import cl.smid.requerimientos.dominio.servicio.EventoTransicion;
import cl.smid.requerimientos.dominio.servicio.MaquinaEstados;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Agregado raíz del dominio de Requerimientos (Núcleo 6.3). Encapsula el ciclo de vida del
 * ingreso (USR.01) y la admisibilidad (USR.02), su máquina de estados, sus NNA afectados, sus
 * anexos (solo metadatos) y el historial de decisiones de admisibilidad.
 *
 * <p>Es <b>dominio puro</b>: sin anotaciones de framework, sin JPA, sin Lombok. La mutación
 * ocurre solo a través de métodos de negocio que protegen las invariantes (ventana de
 * mutabilidad, mínimos para ingresar, transiciones válidas). El {@code idInterno} es un handle
 * de persistencia opaco que jamás cruza la API (cierre IDOR); hacia afuera el requerimiento se
 * identifica por {@code altKey}.</p>
 */
public class Requerimiento {

    // --- Identidad y folio ---
    /** Handle de persistencia opaco; nulo si el agregado aún no se ha persistido. */
    private final Long idInterno;
    /** Identificador público (UUID). Único e inmutable. */
    private final String altKey;
    /** Folio oficial compuesto (Núcleo 6.5). Asignado al crear; inmutable. */
    private final Folio folio;

    // --- Territorialidad ---
    /** alt_key de la sede (estampada desde el token al crear). */
    private final String idSedeAlt;
    /** alt_key de la unidad de destino (nulable hasta clasificar/derivar). */
    private String idUnidadDestinoAlt;

    // --- Estado y clasificación ---
    private EstadoRequerimiento estado;
    private CanalIngreso canal;
    private Complejidad complejidad;
    private Urgencia urgencia;
    /** Costura hacia Vulneraciones (6.5): 1 si la complejidad exige Ficha Interna Reservada. */
    private boolean requiereFichaReservada;

    // --- Requirente y contenido ---
    private String idRequirenteAlt;
    private SnapshotPersona requirenteSnapshot;
    private String resumen;

    // --- Marcas de tiempo y trazabilidad ---
    private Instant fechaIngreso;
    private final boolean esBeta;
    private boolean vigente;
    private final Instant creadoEn;
    private Instant actualizadoEn;
    private final String creadoPor;

    // --- Colecciones hijas del agregado ---
    private final List<NnaAfectado> nnas;
    private final List<Anexo> anexos;
    private final List<Admisibilidad> admisibilidades;

    /**
     * Constructor completo de rehidratación (carga desde persistencia). No aplica reglas: se
     * asume que el estado persistido ya es consistente.
     */
    @SuppressWarnings("java:S107") // La rehidratación de un agregado rico exige muchos parámetros.
    public Requerimiento(Long idInterno, String altKey, Folio folio, String idSedeAlt,
                         String idUnidadDestinoAlt, EstadoRequerimiento estado, CanalIngreso canal,
                         Complejidad complejidad, Urgencia urgencia, boolean requiereFichaReservada,
                         String idRequirenteAlt, SnapshotPersona requirenteSnapshot, String resumen,
                         Instant fechaIngreso, boolean esBeta, boolean vigente, Instant creadoEn,
                         Instant actualizadoEn, String creadoPor, List<NnaAfectado> nnas,
                         List<Anexo> anexos, List<Admisibilidad> admisibilidades) {
        this.idInterno = idInterno;
        this.altKey = Objects.requireNonNull(altKey, "altKey es obligatorio");
        this.folio = Objects.requireNonNull(folio, "folio es obligatorio");
        this.idSedeAlt = Objects.requireNonNull(idSedeAlt, "idSedeAlt es obligatorio");
        this.idUnidadDestinoAlt = idUnidadDestinoAlt;
        this.estado = Objects.requireNonNull(estado, "estado es obligatorio");
        this.canal = canal;
        this.complejidad = complejidad;
        this.urgencia = urgencia;
        this.requiereFichaReservada = requiereFichaReservada;
        this.idRequirenteAlt = idRequirenteAlt;
        this.requirenteSnapshot = requirenteSnapshot;
        this.resumen = resumen;
        this.fechaIngreso = fechaIngreso;
        this.esBeta = esBeta;
        this.vigente = vigente;
        this.creadoEn = Objects.requireNonNull(creadoEn, "creadoEn es obligatorio");
        this.actualizadoEn = Objects.requireNonNull(actualizadoEn, "actualizadoEn es obligatorio");
        this.creadoPor = creadoPor;
        this.nnas = (nnas == null) ? new ArrayList<>() : new ArrayList<>(nnas);
        this.anexos = (anexos == null) ? new ArrayList<>() : new ArrayList<>(anexos);
        this.admisibilidades = (admisibilidades == null) ? new ArrayList<>() : new ArrayList<>(admisibilidades);
    }

    /**
     * Crea un requerimiento nuevo en estado {@code BORRADOR} (USR.01). Admite datos parciales:
     * la mayoría de los campos puede llegar nulo y completarse luego (flexibilidad de ingreso).
     * El folio se asigna al crear (la columna es NOT NULL); la sede y la autoría se estampan
     * desde el token.
     *
     * @param altKey             identificador público recién generado
     * @param folio              folio compuesto y reservado
     * @param idSedeAlt          alt_key de la sede (del token)
     * @param esBeta             si el folio pertenece a la serie BETA (marcha blanca)
     * @param creadoPor          alt_key del autor (claim {@code sub})
     * @param canal              canal de ingreso (nulable en BORRADOR)
     * @param complejidad        complejidad inicial (nulable)
     * @param urgencia           urgencia inicial (nulable)
     * @param idUnidadDestinoAlt unidad de destino inicial (nulable)
     * @param resumen            resumen inicial (nulable)
     * @param idRequirenteAlt    alt_key del requirente (nulable)
     * @param requirenteSnapshot snapshot del requirente (nulable)
     * @param momento            instante de creación (UTC)
     * @return el agregado nuevo
     */
    @SuppressWarnings("java:S107")
    public static Requerimiento crear(String altKey, Folio folio, String idSedeAlt, boolean esBeta,
                                      String creadoPor, CanalIngreso canal, Complejidad complejidad,
                                      Urgencia urgencia, String idUnidadDestinoAlt, String resumen,
                                      String idRequirenteAlt, SnapshotPersona requirenteSnapshot,
                                      Instant momento) {
        Objects.requireNonNull(momento, "El instante de creación es obligatorio");
        boolean fir = complejidad != null && complejidad.requiereFichaReservada();
        return new Requerimiento(
                null, altKey, folio, idSedeAlt, idUnidadDestinoAlt,
                EstadoRequerimiento.BORRADOR, canal, complejidad, urgencia, fir,
                idRequirenteAlt, requirenteSnapshot, resumen,
                null, esBeta, true, momento, momento, creadoPor,
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    }

    /**
     * Edita campos del requerimiento (PUT). Aplica <i>partial-merge</i>: cada argumento nulo
     * significa "no tocar". Solo es posible dentro de la ventana de mutabilidad
     * ({@code BORRADOR}/{@code INGRESADO}/{@code EN_ADMISIBILIDAD}).
     *
     * <p>Si se modifica la {@code complejidad}, se recalcula {@code requiereFichaReservada}
     * (costura FIR hacia 6.5). El emisor del evento {@code requerimiento.clasificado} es la capa
     * de aplicación, que compara la complejidad previa con la nueva.</p>
     *
     * @param canal              nuevo canal (nulable = no tocar)
     * @param complejidad        nueva complejidad (nulable = no tocar)
     * @param urgencia           nueva urgencia (nulable = no tocar)
     * @param idUnidadDestinoAlt nueva unidad de destino (nulable = no tocar)
     * @param resumen            nuevo resumen (nulable = no tocar)
     * @param idRequirenteAlt    nuevo requirente (nulable = no tocar)
     * @param requirenteSnapshot nuevo snapshot del requirente (nulable = no tocar)
     * @param momento            instante de la edición (UTC)
     * @throws TransicionInvalida si el requerimiento ya no es mutable (REQ-409)
     */
    @SuppressWarnings("java:S107")
    public void editar(CanalIngreso canal, Complejidad complejidad, Urgencia urgencia,
                       String idUnidadDestinoAlt, String resumen, String idRequirenteAlt,
                       SnapshotPersona requirenteSnapshot, Instant momento) {
        exigirMutable("editar");
        if (canal != null) {
            this.canal = canal;
        }
        if (complejidad != null) {
            this.complejidad = complejidad;
            this.requiereFichaReservada = complejidad.requiereFichaReservada();
        }
        if (urgencia != null) {
            this.urgencia = urgencia;
        }
        if (idUnidadDestinoAlt != null) {
            this.idUnidadDestinoAlt = idUnidadDestinoAlt;
        }
        if (resumen != null) {
            this.resumen = resumen;
        }
        if (idRequirenteAlt != null) {
            this.idRequirenteAlt = idRequirenteAlt;
        }
        if (requirenteSnapshot != null) {
            this.requirenteSnapshot = requirenteSnapshot;
        }
        tocar(momento);
    }

    /**
     * Agrega un NNA afectado (con sus derechos) al requerimiento (USR.01). Solo dentro de la
     * ventana de mutabilidad.
     *
     * @param nna     NNA afectado a agregar
     * @param momento instante de la operación (UTC)
     * @throws TransicionInvalida si el requerimiento ya no es mutable (REQ-409)
     */
    public void agregarNna(NnaAfectado nna, Instant momento) {
        exigirMutable("agregar NNA");
        this.nnas.add(Objects.requireNonNull(nna, "El NNA es obligatorio"));
        tocar(momento);
    }

    /**
     * Agrega un anexo (solo metadatos) al requerimiento. Solo dentro de la ventana de mutabilidad.
     *
     * @param anexo   anexo a agregar
     * @param momento instante de la operación (UTC)
     */
    public void agregarAnexo(Anexo anexo, Instant momento) {
        exigirMutable("agregar anexo");
        this.anexos.add(Objects.requireNonNull(anexo, "El anexo es obligatorio"));
        tocar(momento);
    }

    /**
     * Envía el requerimiento: transición {@code BORRADOR -> INGRESADO} (USR.01). Exige los
     * mínimos de ingreso: canal, sede y al menos un NNA afectado.
     *
     * @param momento instante del envío (UTC)
     * @throws ReglaNegocioViolada si falta algún mínimo (REQ-422)
     * @throws TransicionInvalida  si el estado no permite enviar (REQ-409)
     */
    public void enviar(Instant momento) {
        List<String> faltantes = new ArrayList<>();
        if (canal == null) {
            faltantes.add("canal");
        }
        if (idSedeAlt == null || idSedeAlt.isBlank()) {
            faltantes.add("sede");
        }
        if (nnas.isEmpty()) {
            faltantes.add("al menos un NNA afectado");
        }
        if (!faltantes.isEmpty()) {
            throw new ReglaNegocioViolada(
                    "No se puede ingresar el requerimiento: falta(n) " + String.join(", ", faltantes) + ".");
        }
        this.estado = MaquinaEstados.siguiente(this.estado, EventoTransicion.ENVIAR);
        this.fechaIngreso = momento;
        tocar(momento);
    }

    /**
     * Abre la admisibilidad: transición {@code INGRESADO -> EN_ADMISIBILIDAD}. Se invoca de
     * forma automática al registrar una decisión sobre un requerimiento aún en {@code INGRESADO}.
     *
     * @param momento instante de la operación (UTC)
     * @throws TransicionInvalida si el estado no permite abrir admisibilidad (REQ-409)
     */
    public void abrirAdmisibilidad(Instant momento) {
        this.estado = MaquinaEstados.siguiente(this.estado, EventoTransicion.ABRIR_ADMISIBILIDAD);
        tocar(momento);
    }

    /**
     * Registra una decisión de admisibilidad (USR.02) y aplica su transición de estado. Exactamente
     * una de las tres acciones disjuntas. Para {@code ASIGNACION} el alt_key del profesional es
     * obligatorio (invariante de dominio); al asignar, el requerimiento queda en {@code ASIGNADO}
     * y se cierra a edición.
     *
     * <p>La validación de pertenencia del profesional a la unidad de destino la realiza la capa de
     * aplicación contra Identidad (puerto), pues requiere acceso externo.</p>
     *
     * @param decision la decisión a registrar
     * @param momento  instante de la decisión (UTC)
     * @throws ReglaNegocioViolada si {@code ASIGNACION} no trae profesional (REQ-422)
     * @throws TransicionInvalida  si el estado no permite la decisión (REQ-409)
     */
    public void decidir(Admisibilidad decision, Instant momento) {
        Objects.requireNonNull(decision, "La decisión es obligatoria");
        if (decision.accion() == AccionAdmisibilidad.ASIGNACION
                && (decision.idProfesionalAsignadoAlt() == null
                || decision.idProfesionalAsignadoAlt().isBlank())) {
            throw new ReglaNegocioViolada(
                    "La asignación exige el alt_key del profesional asignado.");
        }
        EventoTransicion evento = MaquinaEstados.eventoDeAccion(decision.accion());
        this.estado = MaquinaEstados.siguiente(this.estado, evento);
        this.admisibilidades.add(decision);
        tocar(momento);
    }

    /**
     * Borrado lógico (Núcleo 2.6).
     *
     * @param momento instante de la operación (UTC)
     */
    public void anular(Instant momento) {
        this.vigente = false;
        tocar(momento);
    }

    // ------------------------------------------------------------------------
    //  Invariantes y utilidades internas
    // ------------------------------------------------------------------------

    private void exigirMutable(String operacion) {
        if (!estado.esMutable()) {
            throw new TransicionInvalida(
                    "No se puede " + operacion + ": el requerimiento está en estado " + estado
                            + " y ya no admite edición.");
        }
    }

    private void tocar(Instant momento) {
        this.actualizadoEn = Objects.requireNonNull(momento, "El instante es obligatorio");
    }

    /** @return {@code true} si el agregado aún no se ha persistido. */
    public boolean esNuevo() {
        return idInterno == null;
    }

    // ------------------------------------------------------------------------
    //  Accesores (solo lectura; las colecciones se exponen inmodificables)
    // ------------------------------------------------------------------------

    public Long idInterno() {
        return idInterno;
    }

    public String altKey() {
        return altKey;
    }

    public Folio folio() {
        return folio;
    }

    public String idSedeAlt() {
        return idSedeAlt;
    }

    public String idUnidadDestinoAlt() {
        return idUnidadDestinoAlt;
    }

    public EstadoRequerimiento estado() {
        return estado;
    }

    public CanalIngreso canal() {
        return canal;
    }

    public Complejidad complejidad() {
        return complejidad;
    }

    public Urgencia urgencia() {
        return urgencia;
    }

    public boolean requiereFichaReservada() {
        return requiereFichaReservada;
    }

    public String idRequirenteAlt() {
        return idRequirenteAlt;
    }

    public SnapshotPersona requirenteSnapshot() {
        return requirenteSnapshot;
    }

    public String resumen() {
        return resumen;
    }

    public Instant fechaIngreso() {
        return fechaIngreso;
    }

    public boolean esBeta() {
        return esBeta;
    }

    public boolean vigente() {
        return vigente;
    }

    public Instant creadoEn() {
        return creadoEn;
    }

    public Instant actualizadoEn() {
        return actualizadoEn;
    }

    public String creadoPor() {
        return creadoPor;
    }

    /** @return vista inmodificable de los NNA afectados. */
    public List<NnaAfectado> nnas() {
        return Collections.unmodifiableList(nnas);
    }

    /** @return vista inmodificable de los anexos. */
    public List<Anexo> anexos() {
        return Collections.unmodifiableList(anexos);
    }

    /** @return vista inmodificable del historial de decisiones de admisibilidad. */
    public List<Admisibilidad> admisibilidades() {
        return Collections.unmodifiableList(admisibilidades);
    }
}
