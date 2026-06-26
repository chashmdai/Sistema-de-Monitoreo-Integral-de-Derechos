package cl.smid.casos.dominio.modelo;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Agregado raíz del expediente: el <strong>Caso</strong>.
 *
 * <p>POJO PURO: sin Spring, sin JPA, sin Lombok. Encapsula el estado y las invariantes del
 * expediente. La identidad pública es {@link #altKey()} (UUID opaco); la PK numérica interna vive
 * solo en la capa de persistencia y jamás cruza la frontera (cierre IDOR, Núcleo 2.2).</p>
 *
 * <p>Un Caso NACE por materialización desde el evento {@code requerimiento.asignado}
 * (ver {@link #materializar}). No hay constructor público de "alta manual": el expediente es siempre
 * consecuencia de una asignación previa en requerimientos.</p>
 *
 * <p>La distinción entre {@link #historial()} (asientos ya persistidos, cargados desde el repositorio)
 * y {@link #nuevasTransiciones()} (asientos creados en esta sesión) permite al adaptador de
 * persistencia insertar únicamente los asientos nuevos, sin exponer identificadores internos.</p>
 */
public final class Caso {

    /**
     * Actor de sistema usado en la materialización: el listener NO tiene token de usuario (no hay
     * request HTTP ni SecurityContext), por lo que la apertura del caso se atribuye a este actor
     * sintético. Documentado como supuesto en el README y en V1__inicial.sql.
     */
    public static final String ACTOR_SISTEMA = "00000000-0000-0000-0000-000000000000";

    private final String altKey;
    private final NumeroExpediente numeroExpediente;
    private final String idRequerimientoOrigenAlt;
    private final String folioRequerimiento;
    private final String idSedeAlt;
    private final String idUnidadAlt;
    private final String idProfesionalResponsableAlt;
    private EstadoCaso estado;
    private final Complejidad complejidad;
    private final boolean requiereFichaReservada;
    private final boolean esBeta;
    private final Instant abiertoEn;
    private Instant cerradoEn;
    private final Instant creadoEn;
    private Instant actualizadoEn;
    private final String creadoPor;

    private final List<Transicion> historial;
    private final List<Transicion> nuevasTransiciones = new ArrayList<>();

    /** Constructor privado integral; usar las fábricas {@link #materializar} o {@link #reconstituir}. */
    private Caso(String altKey, NumeroExpediente numeroExpediente, String idRequerimientoOrigenAlt,
                 String folioRequerimiento, String idSedeAlt, String idUnidadAlt,
                 String idProfesionalResponsableAlt, EstadoCaso estado, Complejidad complejidad,
                 boolean requiereFichaReservada, boolean esBeta, Instant abiertoEn, Instant cerradoEn,
                 Instant creadoEn, Instant actualizadoEn, String creadoPor, List<Transicion> historial) {
        this.altKey = Objects.requireNonNull(altKey, "altKey");
        this.numeroExpediente = Objects.requireNonNull(numeroExpediente, "numeroExpediente");
        this.idRequerimientoOrigenAlt = Objects.requireNonNull(idRequerimientoOrigenAlt, "idRequerimientoOrigenAlt");
        this.folioRequerimiento = folioRequerimiento;
        this.idSedeAlt = Objects.requireNonNull(idSedeAlt, "idSedeAlt");
        this.idUnidadAlt = idUnidadAlt;
        this.idProfesionalResponsableAlt = idProfesionalResponsableAlt;
        this.estado = Objects.requireNonNull(estado, "estado");
        this.complejidad = complejidad;
        this.requiereFichaReservada = requiereFichaReservada;
        this.esBeta = esBeta;
        this.abiertoEn = Objects.requireNonNull(abiertoEn, "abiertoEn");
        this.cerradoEn = cerradoEn;
        this.creadoEn = Objects.requireNonNull(creadoEn, "creadoEn");
        this.actualizadoEn = Objects.requireNonNull(actualizadoEn, "actualizadoEn");
        this.creadoPor = creadoPor;
        this.historial = new ArrayList<>(historial == null ? List.of() : historial);
    }

    /**
     * Materializa un Caso nuevo (estado {@link EstadoCaso#ABIERTO}) a partir del evento de asignación.
     * Crea además el asiento de apertura ({@code MATERIALIZACION}) atribuido al actor de sistema.
     *
     * @param altKey          alt_key del nuevo caso (generado por el servicio).
     * @param transicionAltKey alt_key del asiento de apertura (generado por el servicio).
     * @param numeroExpediente número de expediente reservado de forma atómica.
     * @param evento          evento de origen.
     * @param ahora           instante actual (UTC) provisto por el reloj del dominio.
     */
    public static Caso materializar(String altKey, String transicionAltKey,
                                    NumeroExpediente numeroExpediente,
                                    EventoRequerimientoAsignado evento, Instant ahora) {
        Objects.requireNonNull(evento, "evento");
        Objects.requireNonNull(ahora, "ahora");
        Caso caso = new Caso(
                altKey,
                numeroExpediente,
                evento.requerimientoOrigenAlt(),
                evento.folio(),
                evento.idSedeAlt(),
                evento.idUnidadDestinoAlt(),
                evento.idProfesionalResponsableAlt(),
                EstadoCaso.ABIERTO,
                evento.complejidad(),
                evento.requiereFichaReservada(),
                Boolean.TRUE.equals(evento.esBeta()),
                ahora,
                null,
                ahora,
                ahora,
                ACTOR_SISTEMA,
                List.of());
        caso.nuevasTransiciones.add(new Transicion(
                transicionAltKey, null, EstadoCaso.ABIERTO,
                Transicion.ACCION_MATERIALIZACION,
                "Expediente materializado desde el evento requerimiento.asignado.",
                ACTOR_SISTEMA, ahora));
        return caso;
    }

    /**
     * Reconstituye un Caso desde la persistencia. No genera asientos nuevos: el historial recibido
     * corresponde a asientos ya almacenados.
     */
    public static Caso reconstituir(String altKey, NumeroExpediente numeroExpediente,
                                    String idRequerimientoOrigenAlt, String folioRequerimiento,
                                    String idSedeAlt, String idUnidadAlt,
                                    String idProfesionalResponsableAlt, EstadoCaso estado,
                                    Complejidad complejidad, boolean requiereFichaReservada,
                                    boolean esBeta, Instant abiertoEn, Instant cerradoEn,
                                    Instant creadoEn, Instant actualizadoEn, String creadoPor,
                                    List<Transicion> historial) {
        return new Caso(altKey, numeroExpediente, idRequerimientoOrigenAlt, folioRequerimiento,
                idSedeAlt, idUnidadAlt, idProfesionalResponsableAlt, estado, complejidad,
                requiereFichaReservada, esBeta, abiertoEn, cerradoEn, creadoEn, actualizadoEn,
                creadoPor, historial);
    }

    /**
     * Aplica una transición ya validada por la máquina de estados. Actualiza el estado, mantiene la
     * coherencia de {@code cerradoEn} y registra el asiento correspondiente.
     *
     * <p>Invariante: el {@code estadoOrigen} de la transición debe coincidir con el estado actual.</p>
     *
     * @param transicion asiento con origen, destino, acción, observación, actor e instante.
     */
    public void aplicar(Transicion transicion) {
        Objects.requireNonNull(transicion, "transicion");
        if (transicion.estadoOrigen() != this.estado) {
            throw new IllegalStateException(
                    "Incoherencia de transición: el origen no corresponde al estado actual del caso.");
        }
        EstadoCaso anterior = this.estado;
        this.estado = transicion.estadoDestino();

        // Coherencia de la marca de cierre.
        if (this.estado == EstadoCaso.CERRADO) {
            this.cerradoEn = transicion.ocurridoEn();
        } else if (anterior == EstadoCaso.CERRADO && this.estado != EstadoCaso.ARCHIVADO) {
            // Reapertura: el expediente vuelve a estar vigente; se limpia la marca de cierre.
            this.cerradoEn = null;
        }

        this.actualizadoEn = transicion.ocurridoEn();
        this.nuevasTransiciones.add(transicion);
    }

    // ----------------------------- Accesores (solo lectura) -----------------------------

    public String altKey() { return altKey; }
    public NumeroExpediente numeroExpediente() { return numeroExpediente; }
    public String idRequerimientoOrigenAlt() { return idRequerimientoOrigenAlt; }
    public String folioRequerimiento() { return folioRequerimiento; }
    public String idSedeAlt() { return idSedeAlt; }
    public String idUnidadAlt() { return idUnidadAlt; }
    public String idProfesionalResponsableAlt() { return idProfesionalResponsableAlt; }
    public EstadoCaso estado() { return estado; }
    public Complejidad complejidad() { return complejidad; }
    public boolean requiereFichaReservada() { return requiereFichaReservada; }
    public boolean esBeta() { return esBeta; }
    public Instant abiertoEn() { return abiertoEn; }
    public Instant cerradoEn() { return cerradoEn; }
    public Instant creadoEn() { return creadoEn; }
    public Instant actualizadoEn() { return actualizadoEn; }
    public String creadoPor() { return creadoPor; }

    /** Asientos ya persistidos (orden cronológico de carga). */
    public List<Transicion> historial() {
        return Collections.unmodifiableList(historial);
    }

    /** Asientos creados en esta sesión y aún no persistidos. El adaptador inserta exactamente estos. */
    public List<Transicion> nuevasTransiciones() {
        return Collections.unmodifiableList(nuevasTransiciones);
    }

    /** Vista combinada del historial (persistido + nuevo) para presentación. */
    public List<Transicion> historialCompleto() {
        List<Transicion> todo = new ArrayList<>(historial);
        todo.addAll(nuevasTransiciones);
        return Collections.unmodifiableList(todo);
    }
}
