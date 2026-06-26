package cl.smid.sgs.service;

import cl.smid.sgs.dto.out.MetricasConcordanciaDTO;
import cl.smid.sgs.repository.SeguimientoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/** Concordancia IA-vs-humano: insumo de calibración del motor (decisión #12 ESNNA). */
@Service
public class MetricasService {

    private final SeguimientoRepository seguimientoRepo;
    public MetricasService(SeguimientoRepository seguimientoRepo) { this.seguimientoRepo = seguimientoRepo; }

    @Transactional(readOnly = true)
    public MetricasConcordanciaDTO concordancia() {
        long total = seguimientoRepo.countConEvaluacionFinal();
        long overrides = seguimientoRepo.countOverrides();
        double tasa = total == 0 ? 0.0 : (double) overrides / total;

        List<MetricasConcordanciaDTO.PorEvaluacion> desglose = new ArrayList<>();
        for (Object[] fila : seguimientoRepo.overridesPorEvaluacionIA()) {
            String ia = fila[0] == null ? "N/D" : fila[0].toString();
            long t = ((Number) fila[1]).longValue();
            long o = fila[2] == null ? 0L : ((Number) fila[2]).longValue();
            desglose.add(new MetricasConcordanciaDTO.PorEvaluacion(ia, t, o, t == 0 ? 0.0 : (double) o / t));
        }
        return new MetricasConcordanciaDTO(total, overrides, tasa, desglose);
    }
}
