package cl.smid.esnna.controller;

import cl.smid.esnna.domain.EstadoGestion;
import cl.smid.esnna.domain.Semaforo;
import cl.smid.esnna.dto.*;
import cl.smid.esnna.exception.EsnnaValidationException;
import cl.smid.esnna.service.EsnnaCasoService;
import cl.smid.esnna.service.EsnnaMotorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * API del motor ESNNA. El controller orquesta: lee el usuario del JWT (CTRL-9),
 * valida la entrada de /procesar (CTRL-4/7) y delega en los servicios. Nunca
 * serializa la entidad: solo DTOs (CTRL-1).
 */
@RestController
@RequestMapping("/api/esnna")
public class EsnnaController {

    private final EsnnaMotorService motorService;
    private final EsnnaCasoService casoService;

    @Value("${esnna.procesar.max-archivos:30}")
    private int maxArchivos;

    @Value("${esnna.documentos.url-validez-minutos:15}")
    private int urlValidezMinutos;

    public EsnnaController(EsnnaMotorService motorService, EsnnaCasoService casoService) {
        this.motorService = motorService;
        this.casoService = casoService;
    }

    // ===================== Tablero =====================

    @GetMapping("/casos")
    public Page<EsnnaResumenDTO> listar(
            @RequestParam(required = false) Semaforo semaforo,
            @RequestParam(required = false) EstadoGestion estado,
            @RequestParam(required = false) String region,
            @RequestParam(required = false) String delito,
            @RequestParam(required = false) String ruc,
            @RequestParam(required = false) String cedulaNna,
            @RequestParam(required = false) String rutImputado,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta,
            @PageableDefault(size = 20, sort = "fechaIngreso", direction = Sort.Direction.DESC) Pageable pageable) {

        LocalDateTime d = desde != null ? desde.atStartOfDay() : null;
        LocalDateTime h = hasta != null ? hasta.atTime(23, 59, 59) : null;
        return casoService.listar(semaforo, estado, region, delito, ruc, cedulaNna, rutImputado, d, h, pageable);
    }

    @GetMapping("/casos/{id}")
    public EsnnaDetalleDTO detalle(@PathVariable Long id) {
        return casoService.obtenerDetalle(id);
    }

    // ===================== Procesar (OCR + IA) =====================

    @PostMapping(value = "/procesar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public EsnnaProcesoResultado procesar(@RequestParam("files") List<MultipartFile> files) {
        if (files == null || files.isEmpty()) {
            throw new EsnnaValidationException("Debe adjuntar al menos un archivo PDF.");
        }
        if (files.size() > maxArchivos) {
            throw new EsnnaValidationException("Máximo " + maxArchivos + " archivos por caso. Recibidos: " + files.size() + ".");
        }
        for (MultipartFile f : files) {
            if (!esPdf(f)) {
                throw new EsnnaValidationException("Solo se aceptan archivos PDF. Inválido: " + nombre(f));
            }
        }
        return motorService.procesar(files);
    }

    // ===================== Guardar =====================

    @PostMapping("/guardar")
    public ResponseEntity<EsnnaDetalleDTO> guardar(@Valid @RequestBody EsnnaCasoCreateRequest req,
                                                   @AuthenticationPrincipal Jwt jwt) {
        GuardarCasoResultado res = casoService.guardar(req, sub(jwt));
        HttpHeaders headers = new HttpHeaders();
        if (!res.posiblesDuplicados().isEmpty()) {
            headers.add("X-Posibles-Duplicados",
                    res.posiblesDuplicados().stream().map(String::valueOf).reduce((a, b) -> a + "," + b).orElse(""));
        }
        return ResponseEntity.status(HttpStatus.CREATED).headers(headers).body(res.caso());
    }

    // ===================== Editar / estado / anular =====================

    @PatchMapping("/casos/{id}")
    public EsnnaDetalleDTO actualizar(@PathVariable Long id,
                                      @Valid @RequestBody EsnnaCasoUpdateRequest req,
                                      @AuthenticationPrincipal Jwt jwt) {
        return casoService.actualizar(id, req, sub(jwt));
    }

    @PostMapping("/casos/{id}/estado")
    public EsnnaDetalleDTO cambiarEstado(@PathVariable Long id,
                                         @Valid @RequestBody CambioEstadoRequest req,
                                         @AuthenticationPrincipal Jwt jwt) {
        return casoService.cambiarEstado(id, req, sub(jwt));
    }

    @DeleteMapping("/casos/{id}")
    public EsnnaDetalleDTO anular(@PathVariable Long id,
                                  @RequestParam String motivo,
                                  @AuthenticationPrincipal Jwt jwt) {
        return casoService.anularCaso(id, motivo, sub(jwt));
    }

    // ===================== Informe / documentos =====================

    @GetMapping("/casos/{id}/informe")
    public ResponseEntity<byte[]> informe(@PathVariable Long id) {
        byte[] pdf = casoService.generarInforme(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Informe_ESNNA_" + id + ".pdf");
        return ResponseEntity.ok().headers(headers).body(pdf);
    }

    @GetMapping("/casos/{id}/documento")
    public List<DocumentoDescargaDTO> documentos(@PathVariable Long id) {
        return casoService.documentos(id, urlValidezMinutos);
    }

    // ===================== Export =====================

    @GetMapping("/exportar-excel")
    public ResponseEntity<InputStreamResource> exportar(@RequestParam(required = false) Semaforo semaforo) {
        ByteArrayInputStream in = casoService.exportarExcel(semaforo);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Matriz_ESNNA_Consolidada.xlsx");
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(new InputStreamResource(in));
    }

    // ===================== Métricas =====================

    @GetMapping("/metricas/concordancia")
    public ConcordanciaMetricaDTO concordancia() {
        return casoService.metricasConcordancia();
    }

    // ===================== Helpers =====================

    private String sub(Jwt jwt) {
        return jwt != null ? jwt.getSubject() : null;
    }

    private boolean esPdf(MultipartFile f) {
        if (f == null || f.isEmpty()) return false;
        String ct = f.getContentType();
        String nombre = f.getOriginalFilename();
        boolean ctOk = ct != null && ct.equalsIgnoreCase(MediaType.APPLICATION_PDF_VALUE);
        boolean extOk = nombre != null && nombre.toLowerCase().endsWith(".pdf");
        return ctOk || extOk;
    }

    private String nombre(MultipartFile f) {
        return f != null && f.getOriginalFilename() != null ? f.getOriginalFilename() : "(sin nombre)";
    }
}
