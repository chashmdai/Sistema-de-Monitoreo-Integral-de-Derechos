package cl.smid.smidservice.controller;

import cl.smid.smidservice.dto.ComparacionDTO;
import cl.smid.smidservice.dto.DashboardResponseDTO;
import cl.smid.smidservice.entity.Institucion;
import cl.smid.smidservice.repository.InstitucionRepository;
import cl.smid.smidservice.service.ReporteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/reportes")
@RequiredArgsConstructor
public class ReporteController {

    private final ReporteService reporteService;
    private final InstitucionRepository instRepo;

    @GetMapping("/dashboard")
    public DashboardResponseDTO dashboard() {
        return DashboardResponseDTO.builder()
                .alertasCriticas(reporteService.contarAlertasCriticas())
                .oficiosPendientes(reporteService.contarOficiosCriticos())
                .totalInstituciones(instRepo.count())
                .stats(reporteService.obtenerEstadisticasGraficas())
                .ranking(reporteService.generarRankingGarantes())
                .matrizCalor(reporteService.generarMatrizCalor())
                .columnasDerechos(reporteService.obtenerDerechosUnicos())
                .instituciones(instRepo.findAll())
                .build();
    }

    @GetMapping("/comparar")
    public ResponseEntity<ComparacionDTO> comparar(@RequestParam(name = "g1", required = false) Long id1,
                                                   @RequestParam(name = "g2", required = false) Long id2) {

        if (id1 == null || id2 == null) {
            return ResponseEntity.badRequest().build();
        }

        String nombre1 = instRepo.findById(id1).map(Institucion::getNombre).orElse("Garante A");
        String nombre2 = instRepo.findById(id2).map(Institucion::getNombre).orElse("Garante B");
        BigDecimal score1 = reporteService.calcularPromedioGeneral(id1);
        BigDecimal score2 = reporteService.calcularPromedioGeneral(id2);

        return ResponseEntity.ok(ComparacionDTO.builder()
                .nombreGarante1(nombre1)
                .scoreGarante1(score1)
                .nombreGarante2(nombre2)
                .scoreGarante2(score2)
                .infoComparacion(String.format("Análisis generado: %s vs %s.", nombre1, nombre2))
                .build());
    }
}
