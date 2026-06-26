package cl.smid.smidservice.service;

import cl.smid.smidservice.dto.DashboardStatsDTO;
import cl.smid.smidservice.dto.RankingDTO;
import cl.smid.smidservice.entity.EstadoOficio;
import cl.smid.smidservice.entity.EvaluacionGarante;
import cl.smid.smidservice.entity.Requerimiento;
import cl.smid.smidservice.repository.EvaluacionGaranteRepository;
import cl.smid.smidservice.repository.OficioSeguimientoRepository;
import cl.smid.smidservice.repository.RequerimientoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReporteService {

    private final EvaluacionGaranteRepository evalRepo;
    private final OficioSeguimientoRepository oficioRepo;
    private final RequerimientoRepository reqRepo;
    private final ScoringService scoringService;

    public List<RankingDTO> generarRankingGarantes() {
        List<EvaluacionGarante> todas = evalRepo.findAll();

        Map<String, Double> promedioPorInst = todas.stream()
                .filter(ev -> ev.getInstitucion() != null && ev.getIndiceAlineacion() != null)
                .collect(Collectors.groupingBy(
                        ev -> ev.getInstitucion().getNombre(),
                        Collectors.averagingDouble(ev -> ev.getIndiceAlineacion().doubleValue())
                ));

        List<RankingDTO> ranking = new ArrayList<>();

        promedioPorInst.forEach((nombre, promedio) -> {
            long count = todas.stream()
                    .filter(e -> e.getInstitucion() != null && e.getInstitucion().getNombre().equals(nombre))
                    .count();

            ranking.add(RankingDTO.builder()
                    .nombreInstitucion(nombre)
                    .indicePromedio(BigDecimal.valueOf(promedio).setScale(2, RoundingMode.HALF_UP))
                    .cantidadCasos(count)
                    .build());
        });

        ranking.sort(Comparator.comparing(RankingDTO::getIndicePromedio));
        return ranking;
    }

    public long contarAlertasCriticas() {
        return evalRepo.findAll().stream()
                .filter(scoringService::esAlertaCritica)
                .count();
    }

    public long contarOficiosCriticos() {
        return oficioRepo.findAll().stream()
                .filter(o -> o.getEstadoActual() == EstadoOficio.VENCIDO ||
                             o.getEstadoActual() == EstadoOficio.POR_VENCER)
                .count();
    }

    public DashboardStatsDTO obtenerEstadisticasGraficas() {
        List<Requerimiento> casos = reqRepo.findAll();
        List<EvaluacionGarante> evaluaciones = evalRepo.findAll();

        long totalCasos = casos.size();
        long casosPrioritarios = casos.stream()
                .filter(Requerimiento::isPrioridadReforzada)
                .count();

        Map<String, Long> porRegion = casos.stream()
                .filter(r -> r.getRegion() != null)
                .collect(Collectors.groupingBy(Requerimiento::getRegion, Collectors.counting()));

        Map<String, Double> promedios = evaluaciones.stream()
                .filter(e -> e.getInstitucion() != null && e.getIndiceAlineacion() != null)
                .collect(Collectors.groupingBy(
                        e -> e.getInstitucion().getNombre(),
                        Collectors.averagingDouble(e -> e.getIndiceAlineacion().doubleValue())
                ));

        List<String> labelsInst = new ArrayList<>();
        List<Double> dataInst = new ArrayList<>();

        promedios.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .limit(10)
                .forEach(entry -> {
                    labelsInst.add(entry.getKey());
                    dataInst.add(entry.getValue());
                });

        return DashboardStatsDTO.builder()
                .totalCasos(totalCasos)
                .casosPrioritarios(casosPrioritarios)
                .labelsRegiones(new ArrayList<>(porRegion.keySet()))
                .dataRegiones(new ArrayList<>(porRegion.values()))
                .labelsInstituciones(labelsInst)
                .dataDesempeno(dataInst)
                .build();
    }

    public Map<String, Map<String, Long>> generarMatrizCalor() {
        List<EvaluacionGarante> evaluaciones = evalRepo.findAll();
        Map<String, Map<String, Long>> matriz = new HashMap<>();

        for (EvaluacionGarante ev : evaluaciones) {
            if (ev.getInstitucion() != null && ev.getDerecho() != null) {
                String inst = ev.getInstitucion().getNombre();
                String der = ev.getDerecho().getNombre();

                matriz.putIfAbsent(inst, new HashMap<>());
                Map<String, Long> derechosMap = matriz.get(inst);
                derechosMap.put(der, derechosMap.getOrDefault(der, 0L) + 1);
            }
        }
        return matriz;
    }

    public Set<String> obtenerDerechosUnicos() {
        return evalRepo.findAll().stream()
                .filter(ev -> ev.getDerecho() != null)
                .map(ev -> ev.getDerecho().getNombre())
                .collect(Collectors.toSet());
    }

    public BigDecimal calcularPromedioGeneral(Long institucionId) {
        if (institucionId == null) return BigDecimal.ZERO;

        List<EvaluacionGarante> evals = evalRepo.findByInstitucionId(institucionId);
        if (evals.isEmpty()) return BigDecimal.ZERO;

        double suma = evals.stream()
                .filter(ev -> ev.getIndiceAlineacion() != null)
                .mapToDouble(ev -> ev.getIndiceAlineacion().doubleValue())
                .sum();

        long count = evals.stream()
                .filter(ev -> ev.getIndiceAlineacion() != null)
                .count();

        return count == 0 ? BigDecimal.ZERO : BigDecimal.valueOf(suma / count).setScale(2, RoundingMode.HALF_UP);
    }
}
