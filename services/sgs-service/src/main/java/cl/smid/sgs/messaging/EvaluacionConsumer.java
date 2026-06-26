package cl.smid.sgs.messaging;

import cl.smid.sgs.config.RabbitConfig;
import cl.smid.sgs.config.SgsProperties;
import cl.smid.sgs.dto.internal.SgsEvaluacionResponseDTO;
import cl.smid.sgs.entity.Recomendacion;
import cl.smid.sgs.exception.PdfExtractionException;
import cl.smid.sgs.repository.RecomendacionRepository;
import cl.smid.sgs.service.EvaluacionService;
import cl.smid.sgs.service.JobService;
import cl.smid.sgs.service.MinioStorageService;
import cl.smid.sgs.service.PdfExtractionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;

/** Worker de Fase B. Carga candidatas, valida tope (decisión #14) y evalúa por bloques (decisión #12). */
@Component
public class EvaluacionConsumer {

    private static final Logger log = LoggerFactory.getLogger(EvaluacionConsumer.class);

    private final JobService jobService;
    private final MinioStorageService minio;
    private final PdfExtractionService pdf;
    private final EvaluacionService evaluacion;
    private final RecomendacionRepository recomendacionRepo;
    private final SgsProperties props;

    public EvaluacionConsumer(JobService jobService, MinioStorageService minio, PdfExtractionService pdf,
                              EvaluacionService evaluacion, RecomendacionRepository recomendacionRepo,
                              SgsProperties props) {
        this.jobService = jobService;
        this.minio = minio;
        this.pdf = pdf;
        this.evaluacion = evaluacion;
        this.recomendacionRepo = recomendacionRepo;
        this.props = props;
    }

    @RabbitListener(queues = RabbitConfig.Q_EVALUACION)
    public void procesar(EvaluacionJobMessage msg) {
        jobService.marcarProcesando(msg.jobId());
        try {
            if (msg.ids() == null || msg.ids().isEmpty()) {
                jobService.fallar(msg.jobId(), "No se indicaron candidatas a evaluar.");
                return;
            }
            if (msg.ids().size() > props.getMaxCandidatas()) {
                jobService.fallar(msg.jobId(), "Excede el máximo de " + props.getMaxCandidatas() + " candidatas.");
                return;
            }
            byte[] bytes = minio.getPdf(msg.pdfHash());
            String texto = pdf.extractText(bytes, msg.nombreArchivo());
            List<Recomendacion> candidatas = recomendacionRepo.findAllById(msg.ids());
            SgsEvaluacionResponseDTO result = evaluacion.evaluar(texto, candidatas);
            jobService.completar(msg.jobId(), result);
        } catch (PdfExtractionException e) {
            jobService.fallar(msg.jobId(), e.getMessage());
        } catch (Exception e) {
            log.error("Fallo procesando evaluación job {}", msg.jobId(), e);
            jobService.fallar(msg.jobId(), "Error procesando la evaluación.");
        }
    }
}
