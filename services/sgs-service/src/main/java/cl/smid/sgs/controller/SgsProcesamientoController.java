package cl.smid.sgs.controller;

import cl.smid.sgs.dto.in.AplicarEvaluacionRequest;
import cl.smid.sgs.dto.in.OficioCreateDTO;
import cl.smid.sgs.dto.out.GuardarResultadoDTO;
import cl.smid.sgs.dto.out.JobAceptadoDTO;
import cl.smid.sgs.entity.JobEstado;
import cl.smid.sgs.enums.TipoAnalisis;
import cl.smid.sgs.exception.SgsValidationException;
import cl.smid.sgs.messaging.EvaluacionJobMessage;
import cl.smid.sgs.messaging.ExtraccionJobMessage;
import cl.smid.sgs.messaging.JobProducer;
import cl.smid.sgs.config.SgsProperties;
import cl.smid.sgs.service.HashUtil;
import cl.smid.sgs.service.JobService;
import cl.smid.sgs.service.MinioStorageService;
import cl.smid.sgs.service.OficioService;
import cl.smid.sgs.service.PdfExtractionService;
import cl.smid.sgs.service.SeguimientoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.util.List;

/** Endpoints de procesamiento. GPT corre asíncrono: 202 + jobId, el frontend pollea (decisión #5). */
@RestController
@RequestMapping("/api/sgs")
public class SgsProcesamientoController {

    private final PdfExtractionService pdf;
    private final MinioStorageService minio;
    private final JobService jobService;
    private final JobProducer producer;
    private final OficioService oficioService;
    private final SeguimientoService seguimientoService;
    private final SgsProperties props;

    public SgsProcesamientoController(PdfExtractionService pdf, MinioStorageService minio, JobService jobService,
                                      JobProducer producer, OficioService oficioService,
                                      SeguimientoService seguimientoService, SgsProperties props) {
        this.pdf = pdf;
        this.minio = minio;
        this.jobService = jobService;
        this.producer = producer;
        this.oficioService = oficioService;
        this.seguimientoService = seguimientoService;
        this.props = props;
    }

    /** Fase A: encola la extracción del oficio. */
    @PostMapping("/procesar-pdf")
    public ResponseEntity<JobAceptadoDTO> procesarPdf(@RequestParam("file") MultipartFile file) throws IOException {
        byte[] bytes = leerPdf(file);
        String hash = HashUtil.sha256(bytes);
        minio.putPdf(hash, bytes);
        JobEstado job = jobService.crear(TipoAnalisis.EXTRACCION);
        producer.publicarExtraccion(new ExtraccionJobMessage(job.getJobId(), hash, file.getOriginalFilename()));
        return accepted(job);
    }

    /** Persiste el oficio revisado por el analista (no involucra GPT). */
    @PostMapping("/guardar")
    public ResponseEntity<GuardarResultadoDTO> guardar(@Valid @RequestBody OficioCreateDTO dto, Authentication auth) {
        GuardarResultadoDTO result = oficioService.guardar(dto, auth.getName());
        return ResponseEntity.created(URI.create("/api/sgs/oficios/" + result.oficioId())).body(result);
    }

    /** Fase B: encola la evaluación de las candidatas contra el oficio de respuesta. */
    @PostMapping("/procesar-respuesta")
    public ResponseEntity<JobAceptadoDTO> procesarRespuesta(@RequestParam("file") MultipartFile file,
                                                            @RequestParam("ids") List<Long> ids) throws IOException {
        if (ids == null || ids.isEmpty()) throw new SgsValidationException("Debe indicar al menos una candidata.");
        if (ids.size() > props.getMaxCandidatas())
            throw new SgsValidationException("Máximo " + props.getMaxCandidatas() + " candidatas por evaluación.");
        byte[] bytes = leerPdf(file);
        String hash = HashUtil.sha256(bytes);
        minio.putPdf(hash, bytes);
        JobEstado job = jobService.crear(TipoAnalisis.EVALUACION);
        producer.publicarEvaluacion(new EvaluacionJobMessage(job.getJobId(), hash, file.getOriginalFilename(), ids));
        return accepted(job);
    }

    /** Confirma evaluaciones humanas -> crea hitos de Seguimiento. */
    @PostMapping("/evaluacion-aplicar")
    public ResponseEntity<Integer> aplicar(@Valid @RequestBody AplicarEvaluacionRequest req, Authentication auth) {
        return ResponseEntity.ok(seguimientoService.aplicar(req, auth.getName()));
    }

    private byte[] leerPdf(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) throw new SgsValidationException("Archivo vacío o ausente.");
        byte[] bytes = file.getBytes();
        if (!pdf.esPdf(bytes)) throw new SgsValidationException("El archivo no es un PDF válido.");
        return bytes;
    }

    private ResponseEntity<JobAceptadoDTO> accepted(JobEstado job) {
        return ResponseEntity.accepted()
                .location(URI.create("/api/sgs/jobs/" + job.getJobId()))
                .body(new JobAceptadoDTO(job.getJobId(), job.getTipo(), job.getStatus(), job.getCreatedAt()));
    }
}
