package cl.smid.sgs.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/** Almacenamiento de expediente (PDF fuente por hash). */
@ConfigurationProperties(prefix = "minio")
public class MinioProperties {
    private boolean enabled = true;
    private String endpoint = "http://localhost:9000";
    private String accessKey;
    private String secretKey;
    private String bucket = "sgs-expedientes";

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean v) { this.enabled = v; }
    public String getEndpoint() { return endpoint; }
    public void setEndpoint(String v) { this.endpoint = v; }
    public String getAccessKey() { return accessKey; }
    public void setAccessKey(String v) { this.accessKey = v; }
    public String getSecretKey() { return secretKey; }
    public void setSecretKey(String v) { this.secretKey = v; }
    public String getBucket() { return bucket; }
    public void setBucket(String v) { this.bucket = v; }
}
