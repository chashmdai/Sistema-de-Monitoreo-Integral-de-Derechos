package cl.smid.sgs.service;

import cl.smid.sgs.config.MinioProperties;
import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/** Expediente: PDF fuente por hash en MinIO (PDF-3). El objeto se nombra con su sha256. */
@Service
public class MinioStorageService {

    private final ObjectProvider<MinioClient> clientProvider;
    private final MinioProperties props;

    public MinioStorageService(ObjectProvider<MinioClient> clientProvider, MinioProperties props) {
        this.clientProvider = clientProvider;
        this.props = props;
    }

    private MinioClient client() {
        MinioClient c = clientProvider.getIfAvailable();
        if (c == null) throw new IllegalStateException("MinIO no está habilitado (minio.enabled=false).");
        return c;
    }

    public void ensureBucket() {
        try {
            MinioClient c = client();
            boolean exists = c.bucketExists(BucketExistsArgs.builder().bucket(props.getBucket()).build());
            if (!exists) c.makeBucket(MakeBucketArgs.builder().bucket(props.getBucket()).build());
        } catch (Exception e) {
            throw new IllegalStateException("No se pudo asegurar el bucket MinIO.", e);
        }
    }

    public void putPdf(String sha256, byte[] bytes) {
        try {
            ensureBucket();
            try (InputStream in = new ByteArrayInputStream(bytes)) {
                client().putObject(PutObjectArgs.builder()
                        .bucket(props.getBucket())
                        .object(sha256 + ".pdf")
                        .stream(in, bytes.length, -1)
                        .contentType("application/pdf")
                        .build());
            }
        } catch (Exception e) {
            throw new IllegalStateException("No se pudo almacenar el PDF en MinIO.", e);
        }
    }

    public byte[] getPdf(String sha256) {
        try (InputStream in = client().getObject(GetObjectArgs.builder()
                .bucket(props.getBucket()).object(sha256 + ".pdf").build())) {
            return in.readAllBytes();
        } catch (Exception e) {
            throw new IllegalStateException("No se pudo recuperar el PDF desde MinIO.", e);
        }
    }
}
