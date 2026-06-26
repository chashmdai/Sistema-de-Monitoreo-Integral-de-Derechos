package cl.smid.smidservice.service;

import cl.smid.smidservice.entity.EvaluacionGarante;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class ScoringService {

    private static final BigDecimal PESO_PARTICIPACION = new BigDecimal("0.35");
    private static final BigDecimal PESO_INTERSECTOR = new BigDecimal("0.25");
    private static final BigDecimal PESO_EFECTIVIDAD = new BigDecimal("0.20");
    private static final BigDecimal PESO_PRIORIZACION = new BigDecimal("0.20");

    public BigDecimal calcularIndiceAlineacion(EvaluacionGarante ev) {
        BigDecimal part = BigDecimal.valueOf(ev.getScoreParticipacion()).multiply(PESO_PARTICIPACION);
        BigDecimal inter = BigDecimal.valueOf(ev.getScoreIntersector()).multiply(PESO_INTERSECTOR);
        BigDecimal efec = BigDecimal.valueOf(ev.getScoreEfectividad()).multiply(PESO_EFECTIVIDAD);
        BigDecimal prio = BigDecimal.valueOf(ev.getScorePriorizacion()).multiply(PESO_PRIORIZACION);

        return part.add(inter).add(efec).add(prio).setScale(2, RoundingMode.HALF_UP);
    }

    public int calcularGravedadTotal(EvaluacionGarante ev) {
        return ev.getDanioFisico() + ev.getDanioPsicologico()
             + ev.getDanioEstructural() + ev.getDanioUrgencia();
    }

    public int calcularDanioInstitucional(EvaluacionGarante ev) {
        return ev.getEstadoOmision() + ev.getEstadoRevictimiza()
             + ev.getEstadoContradiccion();
    }

    public boolean esAlertaCritica(EvaluacionGarante ev) {
        return calcularGravedadTotal(ev) >= 4 && calcularDanioInstitucional(ev) >= 4;
    }
}
