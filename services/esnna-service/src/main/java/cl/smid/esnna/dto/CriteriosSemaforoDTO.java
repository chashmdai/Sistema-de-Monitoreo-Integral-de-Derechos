package cl.smid.esnna.dto;

/**
 * Evidencia estructurada que devuelve la IA (Fase 2). El backend cuenta los
 * criterios cumplidos y aplica la regla determinista (FEAT semáforo-en-backend):
 * la IA NO asigna el color.
 */
public record CriteriosSemaforoDTO(
        CriterioEvaluadoDTO c1PluralidadNna,
        CriterioEvaluadoDTO c2Interseccionalidad,
        CriterioEvaluadoDTO c3AgenteCualificado,
        CriterioEvaluadoDTO c4PluralidadVictimarios,
        CriterioEvaluadoDTO c5CausaBasal,
        boolean exclusionAplicable,
        String cualExclusion,
        boolean improcedenciaPmaNad,
        String detalleImprocedencia
) {
    /** Cantidad de criterios C1..C5 con cumple=true. */
    public int cumplidos() {
        int n = 0;
        if (c1PluralidadNna != null && c1PluralidadNna.cumple()) n++;
        if (c2Interseccionalidad != null && c2Interseccionalidad.cumple()) n++;
        if (c3AgenteCualificado != null && c3AgenteCualificado.cumple()) n++;
        if (c4PluralidadVictimarios != null && c4PluralidadVictimarios.cumple()) n++;
        if (c5CausaBasal != null && c5CausaBasal.cumple()) n++;
        return n;
    }
}
