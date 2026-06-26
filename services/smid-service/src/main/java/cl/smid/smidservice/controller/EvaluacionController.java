package cl.smid.smidservice.controller;

import cl.smid.smidservice.dto.AlertaEvaluacionDTO;
import cl.smid.smidservice.entity.EvaluacionGarante;
import cl.smid.smidservice.entity.Requerimiento;
import cl.smid.smidservice.exception.ResourceNotFoundException;
import cl.smid.smidservice.repository.DerechoRepository;
import cl.smid.smidservice.repository.EvaluacionGaranteRepository;
import cl.smid.smidservice.repository.InstitucionRepository;
import cl.smid.smidservice.repository.RequerimientoRepository;
import cl.smid.smidservice.service.ScoringService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/evaluaciones")
@RequiredArgsConstructor
public class EvaluacionController {

    private final EvaluacionGaranteRepository evalRepo;
    private final RequerimientoRepository reqRepo;
    private final InstitucionRepository instRepo;
    private final DerechoRepository derechoRepo;
    private final ScoringService scoringService;

    @GetMapping("/form-data/{reqId}")
    public Map<String, Object> formData(@PathVariable String reqId) {
        Requerimiento req = reqRepo.findById(reqId)
                .orElseThrow(() -> new ResourceNotFoundException("Requerimiento invalido: " + reqId));

        Map<String, Object> data = new HashMap<>();
        data.put("requerimiento", req);
        data.put("instituciones", instRepo.findAll());
        data.put("derechos", derechoRepo.findAll());
        return data;
    }

    @PostMapping
    public ResponseEntity<AlertaEvaluacionDTO> crear(@Valid @RequestBody EvaluacionGarante evaluacion) {
        evaluacion.setIndiceAlineacion(scoringService.calcularIndiceAlineacion(evaluacion));
        EvaluacionGarante saved = evalRepo.save(evaluacion);

        boolean alerta = scoringService.esAlertaCritica(saved);
        String mensaje = alerta
                ? "¡ATENCIÓN! Se ha detectado una ALERTA CRÍTICA en este caso."
                : "Evaluación registrada correctamente.";

        return ResponseEntity.ok(AlertaEvaluacionDTO.builder()
                .evaluacion(saved)
                .alertaCritica(alerta)
                .mensaje(mensaje)
                .build());
    }
}
