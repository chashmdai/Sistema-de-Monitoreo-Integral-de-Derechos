package cl.smid.antecedentes.dominio.modelo;

/**
 * Criterios de relevancia que pueden marcarse en una {@link FichaAntecedente}.
 * Se modela como conjunto (varios criterios por ficha, sin repeticion).
 */
public enum Criterio {

    /** Gravedad de la situacion descrita. */
    GRAVEDAD,

    /** Complejidad de la coordinacion intersectorial involucrada. */
    COMPLEJIDAD_COORDINACION,

    /** Magnitud de la poblacion afectada. */
    MAGNITUD_POBLACION,

    /** Caracter repetitivo o sistematico del fenomeno. */
    REPETICION,

    /** Requiere interpretacion juridica o tecnica relevante. */
    INTERPRETACION
}
