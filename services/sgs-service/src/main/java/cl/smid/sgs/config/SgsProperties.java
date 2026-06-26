package cl.smid.sgs.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/** Parámetros operativos del dominio SGS. */
@ConfigurationProperties(prefix = "sgs")
public class SgsProperties {

    /** Tope de archivos/candidatas por procesamiento (decisión #14). */
    private int maxCandidatas = 30;
    /** Tamaño de bloque para el MAP/REDUCE de evaluación (decisión #12). */
    private int blockSize = 5;
    /** Mínimo de caracteres de texto tras extracción para considerar el PDF legible (PDF-4). */
    private int minTextoChars = 200;
    /** Umbral de confianza bajo el cual se marca requiereRevisionHumana (decisión #11). */
    private double confianzaUmbral = 0.60;
    /** Claim del JWT que transporta los roles (SEC-2). */
    private String rolesClaim = "roles";

    public int getMaxCandidatas() { return maxCandidatas; }
    public void setMaxCandidatas(int v) { this.maxCandidatas = v; }
    public int getBlockSize() { return blockSize; }
    public void setBlockSize(int v) { this.blockSize = v; }
    public int getMinTextoChars() { return minTextoChars; }
    public void setMinTextoChars(int v) { this.minTextoChars = v; }
    public double getConfianzaUmbral() { return confianzaUmbral; }
    public void setConfianzaUmbral(double v) { this.confianzaUmbral = v; }
    public String getRolesClaim() { return rolesClaim; }
    public void setRolesClaim(String v) { this.rolesClaim = v; }
}
