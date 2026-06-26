package cl.smid.sgs.messaging;

import cl.smid.sgs.config.RabbitConfig;
import cl.smid.sgs.dto.internal.ExtraccionJobResultDTO;
import cl.smid.sgs.exception.PdfExtractionException;
import cl.smid.sgs.service.ExtraccionService;
import cl.smid.sgs.service.JobService;
import cl.smid.sgs.service.MinioStorageService;
import cl.smid.sgs.service.PdfExtractionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/** Worker de Fase A. fail-fast en PDF ilegible (PDF-4) -> job FALLIDO (no reintenta, no va a DLQ por error de negocio). */
@Component
public class ExtraccionConsumer {

    private static final Logger log = LoggerFactory.getLogger(ExtraccionConsumer.class);

    private final JobService jobService;
    private final MinioStorageService minio;
    private final PdfExtractionService pdf;
    private final ExtraccionService extraccion;

    public ExtraccionConsumer(JobService jobService, MinioStorageService minio,
                              PdfExtractionService pdf, ExtraccionService extraccion) {
        this.jobService = jobService;
        this.minio = minio;
        this.pdf = pdf;
        this.extraccion = extraccion;
    }

    @RabbitListener(queues = RabbitConfig.Q_EXTRACCION)
    public void procesar(ExtraccionJobMessage msg) {
        jobService.marcarProcesando(msg.jobId());
        try {
            byte[] bytes = minio.getPdf(msg.pdfHash());
            String texto = pdf.extractText(bytes, msg.nombreArchivo());
            ExtraccionJobResultDTO result = extraccion.extraer(texto, msg.pdfHash());
            jobService.completar(msg.jobId(), result);
        } catch (PdfExtractionException e) {
            jobService.fallar(msg.jobId(), e.getMessage()); // error de negocio: termina FALLIDO, no relanza
        } catch (Exception e) {
            log.error("Fallo procesando extracción job {}", msg.jobId(), e);
            jobService.fallar(msg.jobId(), "Error procesando la extracción.");
        }
    }
}
