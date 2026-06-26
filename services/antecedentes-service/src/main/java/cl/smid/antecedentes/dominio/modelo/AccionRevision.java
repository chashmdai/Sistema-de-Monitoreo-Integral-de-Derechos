package cl.smid.antecedentes.dominio.modelo;

/**
 * Acciones (eventos) que disparan transiciones en la maquina de estados de la
 * {@link FichaAntecedente}. Cada una corresponde a un endpoint del controlador.
 */
public enum AccionRevision {

    /** {@code BORRADOR -> EN_REVISION}. La ejecuta el autor o un miembro de la unidad. */
    ENVIAR_REVISION,

    /** {@code EN_REVISION -> BORRADOR}. Reabre la edicion. Exige rol revisor. */
    DEVOLVER,

    /** {@code EN_REVISION -> APROBADA} (terminal). Exige rol revisor. */
    APROBAR,

    /** {@code EN_REVISION -> RECHAZADA} (terminal). Exige rol revisor. */
    RECHAZAR
}
