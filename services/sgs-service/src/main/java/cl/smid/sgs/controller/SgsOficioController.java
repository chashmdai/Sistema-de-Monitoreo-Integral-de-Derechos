package cl.smid.sgs.controller;

import cl.smid.sgs.dto.in.AnulacionDTO;
import cl.smid.sgs.dto.in.CambioEstadoDTO;
import cl.smid.sgs.dto.in.RecomendacionPatchDTO;
import cl.smid.sgs.dto.out.OficioDetalleDTO;
import cl.smid.sgs.dto.out.SgsResumenDTO;
import cl.smid.sgs.enums.EstadoGestion;
import cl.smid.sgs.enums.PlazoRecomendacion;
import cl.smid.sgs.service.InformeService;
import cl.smid.sgs.service.MinioStorageService;
import cl.smid.sgs.service.OficioService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/** Tablero, detalle, edición de gestión y documentos del expediente. */
@RestController
@RequestMapping("/api/sgs")
public class SgsOficioController {

    private final OficioService oficioService;
    private final InformeService informeService;
    private final MinioStorageService minio;

    public SgsOficioController(OficioService oficioService, InformeService informeService, MinioStorageService minio) {
        this.oficioService = oficioService;
        this.informeService = informeService;
        this.minio = minio;
    }

    /** Tablero paginado (proyección, nunca la entity — CTRL-2). */
    @GetMapping("/oficios")
    public Page<SgsResumenDTO> tablero(
            @RequestParam(required = false) EstadoGestion estado,
            @RequestParam(required = false) PlazoRecomendacion plazo,
            @RequestParam(required = false) Boolean gv,
            @RequestParam(required = false) String region,
            @RequestParam(required = false) String institucion,
            @RequestParam(required = false) String dimension,
            @RequestParam(required = false) String nroOficio,
            @RequestParam(required = false) String responsable,
            @PageableDefault(size = 20) Pageable pageable) {
        return oficioService.tablero(estado, plazo, gv, region, institucion, dimension, nroOficio, responsable, pageable);
    }

    @GetMapping("/oficios/{id}")
    public OficioDetalleDTO detalle(@PathVariable Long id) {
        return oficioService.detalle(id);
    }

    @GetMapping("/oficios/{id}/informe")
    public ResponseEntity<byte[]> informe(@PathVariable Long id) {
        byte[] pdf = informeService.generar(id);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.inline().filename("informe-" + id + ".pdf").toString())
                .body(pdf);
    }

    /** Sirve el PDF fuente desde MinIO por el hash del oficio. */
    @GetMapping("/oficios/{id}/documento")
    public ResponseEntity<byte[]> documento(@PathVariable Long id) {
        String hash = oficioService.detalle(id).pdfHash();
        byte[] pdf = minio.getPdf(hash);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.inline().filename("oficio-" + id + ".pdf").toString())
                .body(pdf);
    }

    @PatchMapping("/recomendaciones/{id}")
    public ResponseEntity<Void> patch(@PathVariable Long id, @RequestBody RecomendacionPatchDTO dto) {
        oficioService.patch(id, dto);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/recomendaciones/{id}/estado")
    public ResponseEntity<Void> estado(@PathVariable Long id, @Valid @RequestBody CambioEstadoDTO dto, Authentication auth) {
        oficioService.cambiarEstado(id, dto, auth.getName());
        return ResponseEntity.noContent().build();
    }

    /** Anulación = borrado lógico con motivo (ENT-10). */
    @DeleteMapping("/recomendaciones/{id}")
    public ResponseEntity<Void> anular(@PathVariable Long id, @Valid @RequestBody AnulacionDTO dto, Authentication auth) {
        oficioService.anular(id, dto, auth.getName());
        return ResponseEntity.noContent().build();
    }
}
