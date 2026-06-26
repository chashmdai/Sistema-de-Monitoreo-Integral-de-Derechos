package cl.smid.esnna.domain;

/**
 * Ciclo de vida de un job de /procesar. Los estados intermedios alimentan la
 * UI de polling (fase + avance), no decisiones de negocio.
 */
public enum EstadoJob {
    EN_COLA,
    EXTRAYENDO,        // PDFBox + respaldo MinIO
    ANALIZANDO,        // Fase 1 (MAP) — expone progreso x/y
    CONSOLIDANDO,      // Fase 2 (REDUCE, gpt-5.5 high)
    COMPLETADO,
    FALLIDO;

    public boolean terminal() {
        return this == COMPLETADO || this == FALLIDO;
    }
}