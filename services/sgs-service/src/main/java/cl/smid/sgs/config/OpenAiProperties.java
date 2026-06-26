package cl.smid.sgs.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.math.BigDecimal;

/** Configuración del cliente OpenAI. Modelos pineados a snapshot (GPT-5/decisión #13). */
@ConfigurationProperties(prefix = "openai")
public class OpenAiProperties {

    private String apiKey;
    private String baseUrl = "https://api.openai.com/v1";
    /** Fase A (MAP de extracción). */
    private String modelExtraccion = "gpt-4o-mini";
    /** Fase B (REDUCE de evaluación), pineado. */
    private String modelEvaluacion = "gpt-5.5";
    private String reasoningEffort = "high";
    private int maxOutputTokens = 32000;
    private long timeoutMs = 120000;
    private String rubricaVersion = "Rúbrica DDN v1";
    /** Costos opcionales por 1k tokens (CLP/USD según configuración) para costo_estimado. */
    private BigDecimal costPromptPer1k = BigDecimal.ZERO;
    private BigDecimal costCompletionPer1k = BigDecimal.ZERO;

    public String getApiKey() { return apiKey; }
    public void setApiKey(String v) { this.apiKey = v; }
    public String getBaseUrl() { return baseUrl; }
    public void setBaseUrl(String v) { this.baseUrl = v; }
    public String getModelExtraccion() { return modelExtraccion; }
    public void setModelExtraccion(String v) { this.modelExtraccion = v; }
    public String getModelEvaluacion() { return modelEvaluacion; }
    public void setModelEvaluacion(String v) { this.modelEvaluacion = v; }
    public String getReasoningEffort() { return reasoningEffort; }
    public void setReasoningEffort(String v) { this.reasoningEffort = v; }
    public int getMaxOutputTokens() { return maxOutputTokens; }
    public void setMaxOutputTokens(int v) { this.maxOutputTokens = v; }
    public long getTimeoutMs() { return timeoutMs; }
    public void setTimeoutMs(long v) { this.timeoutMs = v; }
    public String getRubricaVersion() { return rubricaVersion; }
    public void setRubricaVersion(String v) { this.rubricaVersion = v; }
    public BigDecimal getCostPromptPer1k() { return costPromptPer1k; }
    public void setCostPromptPer1k(BigDecimal v) { this.costPromptPer1k = v; }
    public BigDecimal getCostCompletionPer1k() { return costCompletionPer1k; }
    public void setCostCompletionPer1k(BigDecimal v) { this.costCompletionPer1k = v; }
}
