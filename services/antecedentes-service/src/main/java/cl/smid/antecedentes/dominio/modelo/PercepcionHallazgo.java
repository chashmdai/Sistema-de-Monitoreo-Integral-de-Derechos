package cl.smid.antecedentes.dominio.modelo;

/**
 * Evaluacion de la ficha respecto de su relacion con un hallazgo institucional.
 * Gobierna reglas de coherencia (ver {@link FichaAntecedente#validarCoherenciaHallazgo}).
 */
public enum PercepcionHallazgo {

    /** No constituye un hallazgo; prohibe vincular hallazgo o instrumento. */
    NO_ES_HALLAZGO,

    /** Es antecedente de un hallazgo existente; exige {@code hallazgoAlt} valido. */
    ANTECEDENTE_DE_HALLAZGO,

    /** Propone un nuevo hallazgo; exige instrumento y crea el {@code Hallazgo} (PROPUESTO). */
    SE_PROPONE_HALLAZGO
}
