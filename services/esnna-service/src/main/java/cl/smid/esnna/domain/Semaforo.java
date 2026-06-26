package cl.smid.esnna.domain;

/**
 * Prioridad del caso según protocolo PR-PDR-05.
 *
 * La regla copulativa la aplica el BACKEND de forma determinista (no la IA):
 * la IA solo detecta el cumplimiento de cada criterio y aporta evidencia; este
 * enum traduce ese conteo a un color auditable. Así el semáforo deja de ser una
 * caja negra y el override humano puede discutir un criterio puntual.
 */
public enum Semaforo {
    ROJO,
    AMARILLO,
    VERDE;

    /**
     * Evalúa el semáforo a partir de la evidencia estructurada de la IA.
     *
     * @param criteriosCumplidos   cantidad de criterios C1..C5 con cumple=true (0..5).
     * @param exclusionAplicable   alguna causal de exclusión del protocolo (§5.1/§5.4/§6.2.5).
     * @param improcedenciaPmaNad  improcedencia PMA/NAD (§5.2, párrafo final).
     * @return ROJO si ≥3 criterios; AMARILLO si exactamente 2; VERDE en el resto
     *         o si aplica cualquier exclusión/improcedencia.
     */
    public static Semaforo evaluar(int criteriosCumplidos,
                                   boolean exclusionAplicable,
                                   boolean improcedenciaPmaNad) {
        if (exclusionAplicable || improcedenciaPmaNad) {
            return VERDE;
        }
        if (criteriosCumplidos >= 3) {
            return ROJO;
        }
        if (criteriosCumplidos == 2) {
            return AMARILLO;
        }
        return VERDE;
    }
}
