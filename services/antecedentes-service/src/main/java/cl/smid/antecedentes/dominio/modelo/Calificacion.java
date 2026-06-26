package cl.smid.antecedentes.dominio.modelo;

/**
 * Calificacion del aprendizaje institucional que recoge una {@link FichaAntecedente}.
 */
public enum Calificacion {

    /** Practica que merece destacarse y replicarse. */
    BUENA_PRACTICA,

    /** Nudo critico: obstaculo o dificultad recurrente detectada. */
    NUDO_CRITICO,

    /** Caso no abordado: brecha de cobertura o atencion. */
    CASO_NO_ABORDADO
}
