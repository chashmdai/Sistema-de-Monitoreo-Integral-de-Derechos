package cl.smid.sgs.exception;

/** 429 de OpenAI -> reintentable por Resilience4j; si agota -> 429 al cliente (RES-1). */
public class GptRateLimitException extends RuntimeException {
    public GptRateLimitException(String message) { super(message); }
    public GptRateLimitException(String message, Throwable cause) { super(message, cause); }
}
