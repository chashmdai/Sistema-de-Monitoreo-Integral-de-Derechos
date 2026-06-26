package cl.smid.antecedentes.dominio.modelo;

/**
 * Estados del ciclo de vida de una {@link FichaAntecedente}.
 *
 * <pre>
 *   BORRADOR --enviarRevision--&gt; EN_REVISION --aprobar--&gt; APROBADA (terminal)
 *      ^                            |   |
 *      |          devolver          |   | rechazar
 *      +----------------------------+   +--------------&gt; RECHAZADA (terminal)
 * </pre>
 *
 * Solo {@code BORRADOR} admite edicion y eliminacion. {@code APROBADA} y
 * {@code RECHAZADA} son estados terminales.
 */
public enum EstadoFicha {

    /** Estado inicial; unica ventana de mutabilidad (editable y eliminable). */
    BORRADOR,

    /** Enviada a revision; espera decision de un revisor (aprobar/devolver/rechazar). */
    EN_REVISION,

    /** Aprobada por un revisor. Estado terminal. */
    APROBADA,

    /** Rechazada por un revisor. Estado terminal. */
    RECHAZADA;

    /** Indica si en este estado la ficha admite edicion y eliminacion. */
    public boolean esMutable() {
        return this == BORRADOR;
    }

    /** Indica si este estado es terminal (no admite mas transiciones). */
    public boolean esTerminal() {
        return this == APROBADA || this == RECHAZADA;
    }
}
