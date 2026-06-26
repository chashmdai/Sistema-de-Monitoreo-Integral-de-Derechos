package cl.smid.sgs.dto.out;

import java.util.List;

/** Concordancia IA-vs-humano (decisión #12 ESNNA): tasa de override y desglose. */
public record MetricasConcordanciaDTO(
        long totalSeguimientos,
        long conOverride,
        double tasaOverride,
        List<PorEvaluacion> desglose
) {
    public record PorEvaluacion(String evaluacionIA, long total, long overrides, double tasa) {}
}
