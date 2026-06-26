package cl.smid.esnna.exception;

/**
 * ERR-5: subtipo para 429 (rate limit real) y 503 de OpenAI. Siempre retryable.
 * RES-5: porta Retry-After para que el IntervalBiFunction del retry lo honre
 * (antes se capturaba y se ignoraba; el backoff fijo caía dentro de la ventana
 * de throttle).
 *
 * Nota RES-4: un 429 con error.code=insufficient_quota NO debe construirse como
 * esta excepción — es fatal hasta acción administrativa y se clasifica como
 * GptClientException(retryable=false). La discriminación vive en EsnnaGptClient.
 */
public class GptRateLimitException extends GptClientException {

    private final Long retryAfterSeconds;

    public GptRateLimitException(String message, Long retryAfterSeconds, int httpStatus) {
        super(message, null, httpStatus, "rate_limit", true);
        this.retryAfterSeconds = retryAfterSeconds;
    }

    public Long getRetryAfterSeconds() {
        return retryAfterSeconds;
    }
}