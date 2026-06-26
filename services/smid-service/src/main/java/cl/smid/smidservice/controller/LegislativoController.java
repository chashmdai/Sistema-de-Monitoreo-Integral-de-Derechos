package cl.smid.smidservice.controller;

import cl.smid.smidservice.entity.Ley;
import cl.smid.smidservice.entity.TramiteLegislativo;
import cl.smid.smidservice.exception.ResourceNotFoundException;
import cl.smid.smidservice.repository.LeyRepository;
import cl.smid.smidservice.repository.TramiteLegislativoRepository;
import cl.smid.smidservice.service.LegislativoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/legislativo")
@RequiredArgsConstructor
public class LegislativoController {

    private final LeyRepository leyRepo;
    private final TramiteLegislativoRepository tramiteRepo;
    private final LegislativoService legService;

    @GetMapping("/leyes")
    public List<Ley> listarLeyes() {
        return leyRepo.findAll();
    }

    @PostMapping("/leyes")
    public ResponseEntity<Ley> crearLey(@RequestBody Ley ley) {
        return ResponseEntity.ok(leyRepo.save(ley));
    }

    @GetMapping("/leyes/{id}")
    public Map<String, Object> verLey(@PathVariable Integer id) {
        Ley ley = leyRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ley invalida: " + id));

        Map<String, Object> data = new HashMap<>();
        data.put("ley", ley);
        data.put("tramites", tramiteRepo.findByLeyIdOrderByFechaTramiteAsc(id));
        return data;
    }

    @GetMapping("/tramites/form-data/{leyId}")
    public Map<String, Object> formDataTramite(@PathVariable Integer leyId) {
        Ley ley = leyRepo.findById(leyId)
                .orElseThrow(() -> new ResourceNotFoundException("Ley invalida: " + leyId));

        Map<String, Object> data = new HashMap<>();
        data.put("ley", ley);
        return data;
    }

    @PostMapping("/tramites")
    public ResponseEntity<TramiteLegislativo> crearTramite(@RequestBody TramiteLegislativo tramite) {
        BigDecimal irn = legService.calcularIRN(tramite);
        BigDecimal irri = legService.calcularIRRI(tramite);
        BigDecimal compuesto = legService.calcularIndiceCompuesto(irn, irri);

        tramite.setIndiceIrn(irn);
        tramite.setIndiceIrri(irri);
        tramite.setIndiceCompuesto(compuesto);

        TramiteLegislativo saved = tramiteRepo.save(tramite);
        return ResponseEntity.ok(saved);
    }
}
