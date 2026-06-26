package cl.smid.antecedentes.dominio.modelo;

/**
 * Estados de un {@link Hallazgo}. Un hallazgo nace {@code PROPUESTO} (sea por una
 * ficha que lo propone o por creacion directa de gestion) y luego puede pasar a
 * {@code ASOCIADO} (incorporado al conocimiento institucional) o {@code RECHAZADO}.
 */
public enum EstadoHallazgo {

    /** Propuesta inicial, pendiente de evaluacion. */
    PROPUESTO,

    /** Incorporado formalmente como hallazgo institucional. */
    ASOCIADO,

    /** Descartado tras evaluacion. */
    RECHAZADO
}
