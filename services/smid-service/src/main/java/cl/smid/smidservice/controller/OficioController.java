package cl.smid.smidservice.controller;

import cl.smid.smidservice.entity.EvaluacionGarante;
import cl.smid.smidservice.entity.OficioSeguimiento;
import cl.smid.smidservice.entity.TipoDocumento;
import cl.smid.smidservice.exception.ResourceNotFoundException;
import cl.smid.smidservice.repository.EvaluacionGaranteRepository;
import cl.smid.smidservice.repository.OficioSeguimientoRepository;
import cl.smid.smidservice.service.DeadlineService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/oficios")
@RequiredArgsConstructor
public class OficioController {

    private final OficioSeguimientoRepository oficioRepo;
    private final EvaluacionGaranteRepository evalRepo;
    private final DeadlineService deadlineService;

    @GetMapping("/form-data/{evalId}")
    public Map<String, Object> formData(@PathVariable Long evalId) {
        EvaluacionGarante evaluacion = evalRepo.findById(evalId)
                .orElseThrow(() -> new ResourceNotFoundException("Evaluacion no encontrada: " + evalId));

        Map<String, Object> data = new HashMap<>();
        data.put("evaluacion", evaluacion);
        data.put("tiposDocumento", TipoDocumento.values());
        data.put("plazoDiasDefault", 10);
        return data;
    }

    @PostMapping
    public ResponseEntity<OficioSeguimiento> crear(@Valid @RequestBody OficioSeguimiento oficio) {
        oficio.setFechaVencimiento(
            deadlineService.calcularVencimientoHabiles(oficio.getFechaEnvio(), oficio.getPlazoDias())
        );

        oficio.setEstadoActual(
            deadlineService.calcularEstadoActual(oficio.getFechaVencimiento())
        );

        OficioSeguimiento saved = oficioRepo.save(oficio);
        return ResponseEntity.ok(saved);
    }
}
