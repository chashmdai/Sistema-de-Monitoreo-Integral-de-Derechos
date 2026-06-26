package cl.smid.sgs.controller;

import cl.smid.sgs.dto.out.AlertaDTO;
import cl.smid.sgs.dto.out.CatalogoDTO;
import cl.smid.sgs.dto.out.MetricasConcordanciaDTO;
import cl.smid.sgs.enums.TipoCatalogo;
import cl.smid.sgs.service.AlertaService;
import cl.smid.sgs.service.CatalogoService;
import cl.smid.sgs.service.ExcelExportService;
import cl.smid.sgs.service.MetricasService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

/** Consultas transversales: export, alertas, métricas y catálogos. */
@RestController
@RequestMapping("/api/sgs")
public class SgsConsultaController {

    private final ExcelExportService excel;
    private final AlertaService alertaService;
    private final MetricasService metricasService;
    private final CatalogoService catalogoService;

    public SgsConsultaController(ExcelExportService excel, AlertaService alertaService,
                                 MetricasService metricasService, CatalogoService catalogoService) {
        this.excel = excel;
        this.alertaService = alertaService;
        this.metricasService = metricasService;
        this.catalogoService = catalogoService;
    }

    @GetMapping("/exportar-excel")
    public ResponseEntity<Resource> exportar() {
        byte[] data = excel.exportar();
        String nombre = "seguimiento-sgs-" + LocalDate.now() + ".xlsx";
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment().filename(nombre).toString())
                .contentLength(data.length)
                .body(new ByteArrayResource(data));
    }

    @GetMapping("/alertas")
    public List<AlertaDTO> alertas() {
        return alertaService.pendientes();
    }

    @GetMapping("/metricas/concordancia")
    public MetricasConcordanciaDTO concordancia() {
        return metricasService.concordancia();
    }

    @GetMapping("/catalogos")
    public List<CatalogoDTO> catalogos(@RequestParam TipoCatalogo tipo) {
        return catalogoService.listar(tipo);
    }
}
