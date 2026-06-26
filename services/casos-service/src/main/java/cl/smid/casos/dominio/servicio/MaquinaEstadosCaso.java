package cl.smid.casos.dominio.servicio;

import cl.smid.casos.dominio.excepcion.TransicionInvalidaException;
import cl.smid.casos.dominio.modelo.AccionCaso;
import cl.smid.casos.dominio.modelo.EstadoCaso;

import java.util.Map;

/**
 * Máquina de estados del Caso, modelada como <strong>tabla PURA</strong>
 * {@code Map<(estadoOrigen, accion) → estadoDestino>}, igual que en requerimientos. La resolución es
 * O(1) y la tabla es inmutable. Una transición no contemplada lanza {@link TransicionInvalidaException}
 * (CAS-409).
 *
 * <p>Diseño de transiciones (declarado como supuesto; no hay spec formal de 6.4):</p>
 * <pre>
 *   ABIERTO          ──INICIAR_INVESTIGACION──▶ EN_INVESTIGACION
 *   ABIERTO          ──CERRAR───────────────────▶ CERRADO
 *   EN_INVESTIGACION ──DERIVAR_A_SEGUIMIENTO────▶ EN_SEGUIMIENTO
 *   EN_INVESTIGACION ──SUSPENDER────────────────▶ SUSPENDIDO
 *   EN_INVESTIGACION ──CERRAR────────────────────▶ CERRADO
 *   EN_SEGUIMIENTO   ──REANUDAR──────────────────▶ EN_INVESTIGACION
 *   EN_SEGUIMIENTO   ──SUSPENDER──────────────────▶ SUSPENDIDO
 *   EN_SEGUIMIENTO   ──CERRAR──────────────────────▶ CERRADO
 *   SUSPENDIDO       ──REANUDAR──────────────────▶ EN_INVESTIGACION
 *   SUSPENDIDO       ──CERRAR──────────────────────▶ CERRADO
 *   CERRADO          ──REABRIR──────────────────▶ EN_INVESTIGACION
 *   CERRADO          ──ARCHIVAR──────────────────▶ ARCHIVADO   (terminal)
 * </pre>
 */
public final class MaquinaEstadosCaso {

    /** Clave compuesta de la tabla de transiciones. */
    private record Clave(EstadoCaso origen, AccionCaso accion) {}

    private final Map<Clave, EstadoCaso> transiciones;

    public MaquinaEstadosCaso() {
        this.transiciones = Map.ofEntries(
                Map.entry(new Clave(EstadoCaso.ABIERTO, AccionCaso.INICIAR_INVESTIGACION), EstadoCaso.EN_INVESTIGACION),
                Map.entry(new Clave(EstadoCaso.ABIERTO, AccionCaso.CERRAR), EstadoCaso.CERRADO),
                Map.entry(new Clave(EstadoCaso.EN_INVESTIGACION, AccionCaso.DERIVAR_A_SEGUIMIENTO), EstadoCaso.EN_SEGUIMIENTO),
                Map.entry(new Clave(EstadoCaso.EN_INVESTIGACION, AccionCaso.SUSPENDER), EstadoCaso.SUSPENDIDO),
                Map.entry(new Clave(EstadoCaso.EN_INVESTIGACION, AccionCaso.CERRAR), EstadoCaso.CERRADO),
                Map.entry(new Clave(EstadoCaso.EN_SEGUIMIENTO, AccionCaso.REANUDAR), EstadoCaso.EN_INVESTIGACION),
                Map.entry(new Clave(EstadoCaso.EN_SEGUIMIENTO, AccionCaso.SUSPENDER), EstadoCaso.SUSPENDIDO),
                Map.entry(new Clave(EstadoCaso.EN_SEGUIMIENTO, AccionCaso.CERRAR), EstadoCaso.CERRADO),
                Map.entry(new Clave(EstadoCaso.SUSPENDIDO, AccionCaso.REANUDAR), EstadoCaso.EN_INVESTIGACION),
                Map.entry(new Clave(EstadoCaso.SUSPENDIDO, AccionCaso.CERRAR), EstadoCaso.CERRADO),
                Map.entry(new Clave(EstadoCaso.CERRADO, AccionCaso.REABRIR), EstadoCaso.EN_INVESTIGACION),
                Map.entry(new Clave(EstadoCaso.CERRADO, AccionCaso.ARCHIVAR), EstadoCaso.ARCHIVADO));
    }

    /**
     * Resuelve el estado destino para {@code (origen, accion)}; si no existe transición válida,
     * lanza {@link TransicionInvalidaException}.
     */
    public EstadoCaso transicionar(EstadoCaso origen, AccionCaso accion) {
        EstadoCaso destino = transiciones.get(new Clave(origen, accion));
        if (destino == null) {
            throw new TransicionInvalidaException(origen, accion);
        }
        return destino;
    }

    /** {@code true} si el estado es terminal definitivo (no admite más transiciones). */
    public boolean esTerminal(EstadoCaso estado) {
        return estado == EstadoCaso.ARCHIVADO;
    }
}
