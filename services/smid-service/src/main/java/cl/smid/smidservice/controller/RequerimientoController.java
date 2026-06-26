package cl.smid.smidservice.controller;

import cl.smid.smidservice.entity.EvaluacionGarante;
import cl.smid.smidservice.entity.OficioSeguimiento;
import cl.smid.smidservice.entity.Requerimiento;
import cl.smid.smidservice.exception.ResourceNotFoundException;
import cl.smid.smidservice.repository.FactorRiesgoRepository;
import cl.smid.smidservice.repository.OficioSeguimientoRepository;
import cl.smid.smidservice.repository.RequerimientoRepository;
import cl.smid.smidservice.service.DeadlineService;
import cl.smid.smidservice.service.ScoringService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/requerimientos")
@RequiredArgsConstructor
public class RequerimientoController {

    private final RequerimientoRepository reqRepository;
    private final FactorRiesgoRepository factorRepository;
    private final OficioSeguimientoRepository oficioRepo;
    private final DeadlineService deadlineService;
    private final ScoringService scoringService;

    @GetMapping
    public List<Requerimiento> listar() {
        return reqRepository.findAll();
    }

    @GetMapping("/form-data")
    public Map<String, Object> formData() {
        Map<String, Object> data = new HashMap<>();
        data.put("factores", factorRepository.findAll());
        return data;
    }

    @PostMapping
    public ResponseEntity<Requerimiento> crear(@Valid @RequestBody Requerimiento requerimiento) {
        Requerimiento saved = reqRepository.save(requerimiento);
        return ResponseEntity.ok(saved);
    }

    @Transactional
    @GetMapping("/{codigo}")
    public Map<String, Object> ver(@PathVariable String codigo) {
        Requerimiento req = reqRepository.findByIdWithEvaluaciones(codigo)
                .orElseThrow(() -> new ResourceNotFoundException("Caso no encontrado: " + codigo));

        // Recalcular estados de oficios al vuelo
        if (req.getEvaluaciones() != null) {
            for (EvaluacionGarante ev : req.getEvaluaciones()) {
                List<OficioSeguimiento> oficios = oficioRepo.findByEvaluacionId(ev.getId());
                if (oficios != null) {
                    for (OficioSeguimiento of : oficios) {
                        of.setEstadoActual(deadlineService.calcularEstadoActual(of.getFechaVencimiento()));
                    }
                }
            }
        }

        // Calcular indicadores derivados (scoring) por cada evaluacion para el front
        Map<Long, Map<String, Object>> indicadores = new HashMap<>();
        if (req.getEvaluaciones() != null) {
            for (EvaluacionGarante ev : req.getEvaluaciones()) {
                Map<String, Object> ind = new HashMap<>();
                ind.put("gravedadTotal", scoringService.calcularGravedadTotal(ev));
                ind.put("danioInstitucional", scoringService.calcularDanioInstitucional(ev));
                ind.put("alertaCritica", scoringService.esAlertaCritica(ev));
                indicadores.put(ev.getId(), ind);
            }
        }

        Map<String, Object> response = new HashMap<>();
        response.put("requerimiento", req);
        response.put("indicadores", indicadores);
        return response;
    }

    @DeleteMapping("/{codigo}")
    public ResponseEntity<Void> eliminar(@PathVariable String codigo) {
        if (!reqRepository.existsById(codigo)) {
            throw new ResourceNotFoundException("Caso no encontrado: " + codigo);
        }
        reqRepository.deleteById(codigo);
        return ResponseEntity.noContent().build();
    }
}
