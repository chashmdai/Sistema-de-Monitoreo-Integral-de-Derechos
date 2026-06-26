package cl.smid.sgs.service;

import cl.smid.sgs.dto.out.AlertaDTO;
import cl.smid.sgs.entity.AlertaSeguimiento;
import cl.smid.sgs.entity.Recomendacion;
import cl.smid.sgs.repository.AlertaRepository;
import cl.smid.sgs.repository.RecomendacionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class AlertaService {

    private final AlertaRepository alertaRepo;
    private final RecomendacionRepository recomendacionRepo;

    public AlertaService(AlertaRepository alertaRepo, RecomendacionRepository recomendacionRepo) {
        this.alertaRepo = alertaRepo;
        this.recomendacionRepo = recomendacionRepo;
    }

    @Transactional(readOnly = true)
    public List<AlertaDTO> pendientes() {
        List<AlertaSeguimiento> alertas = alertaRepo.findByAtendidaFalseOrderByFechaLimiteAsc();
        List<Long> ids = alertas.stream().map(AlertaSeguimiento::getRecomendacionId).distinct().toList();
        Map<Long, Recomendacion> recs = ids.isEmpty() ? Map.of()
                : recomendacionRepo.findAllById(ids).stream()
                    .collect(Collectors.toMap(Recomendacion::getId, Function.identity()));
        LocalDate hoy = LocalDate.now();
        return alertas.stream().map(a -> {
            Recomendacion r = recs.get(a.getRecomendacionId());
            long dias = ChronoUnit.DAYS.between(hoy, a.getFechaLimite());
            return new AlertaDTO(
                    a.getId(), a.getRecomendacionId(),
                    r == null ? null : r.getOficio().getNroOficio(),
                    r == null ? null : r.getCorrelativo(),
                    a.getTipo(), a.getFechaLimite(), dias, dias < 0,
                    r != null && r.isGv());
        }).toList();
    }
}
