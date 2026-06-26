package cl.smid.esnna.service;

import io.minio.*;
import io.minio.http.Method;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

/**
 * Respaldo del PDF fuente en MinIO, indexado por su SHA-256 (FEAT expediente).
 * Le da sentido al hash de auditoría: el hash apunta a un objeto recuperable.
 *
 * Diseño:
 *  - Best-effort en la subida: si MinIO falla, se loggea y el análisis continúa
 *    (no se pierde la corrida; sí el respaldo, y queda registrado).
 *  - Desactivable (esnna.minio.enabled=false) para correr en dev sin MinIO.
 *  - Idempotente: misma key = mismo hash, no duplica.
 */
@Service
public class DocumentoStorageService {

    private static final Logger log = LoggerFactory.getLogger(DocumentoStorageService.class);

    @Value("${esnna.minio.enabled:false}")
    private boolean enabled;
    @Value("${esnna.minio.endpoint:http://localhost:9000}")
    private String endpoint;
    @Value("${esnna.minio.access-key:}")
    private String accessKey;
    @Value("${esnna.minio.secret-key:}")
    private String secretKey;
    @Value("${esnna.minio.bucket:esnna-documentos}")
    private String bucket;

    private MinioClient client;

    @PostConstruct
    void init() {
        if (!enabled) {
            log.info("MinIO deshabilitado (esnna.minio.enabled=false); el respaldo de PDFs queda inactivo.");
            return;
        }
        try {
            this.client = MinioClient.builder()
                    .endpoint(endpoint)
                    .credentials(accessKey, secretKey)
                    .build();
            boolean existe = client.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
            if (!existe) {
                client.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
            }
        } catch (Exception e) {
            log.error("No se pudo inicializar MinIO; el respaldo de PDFs quedará inactivo. {}", e.getMessage());
            this.client = null;
        }
    }

    /** Sube el PDF best-effort. Devuelve true si quedó respaldado. */
    public boolean guardar(String sha256, byte[] contenido, String contentType) {
        if (!enabled || client == null) return false;
        try (InputStream in = new ByteArrayInputStream(contenido)) {
            client.putObject(PutObjectArgs.builder()
                    .bucket(bucket)
                    .object(sha256)
                    .stream(in, contenido.length, -1)
                    .contentType(contentType != null ? contentType : "application/pdf")
                    .build());
            return true;
        } catch (Exception e) {
            log.warn("No se pudo respaldar el documento {} en MinIO: {}", sha256, e.getMessage());
            return false;
        }
    }

    /** URL temporal de descarga (presigned). null si MinIO no está disponible. */
    public String urlDescarga(String sha256, int minutosValidez) {
        if (!enabled || client == null) return null;
        try {
            return client.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                    .method(Method.GET)
                    .bucket(bucket)
                    .object(sha256)
                    .expiry(minutosValidez, TimeUnit.MINUTES)
                    .build());
        } catch (Exception e) {
            log.warn("No se pudo generar URL de descarga para {}: {}", sha256, e.getMessage());
            return null;
        }
    }
}
