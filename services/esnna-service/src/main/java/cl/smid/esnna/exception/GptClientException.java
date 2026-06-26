package cl.smid.esnna.exception;

/**
 * Falla en la comunicación con OpenAI o en el parseo de su respuesta.
 * PRIV-2: el mensaje solo lleva tipo/código de OpenAI, nunca eco del input.
 *
 * RES-1/RES-4: porta el status HTTP, el error.code de OpenAI y un marcador
 * explícito de retryabilidad. El retry (Resilience4j) decide por este campo
 * mediante predicate, no por instanceof de una subclase — así 500/502/504/408
 * y fallos de transporte son reintentables, e insufficient_quota (429) no.
 */
public class GptClientException extends EsnnaException {

    private final Integer httpStatus;     // null si no hubo respuesta HTTP (transporte/parseo)
    private final String errorCode;       // error.code de OpenAI o código interno (FALLO_RED, ...)
    private final boolean retryable;

    /** Compatibilidad: fatal, sin status. */
    public GptClientException(String message) {
        this(message, null, null, null, false);
    }

    /** Compatibilidad: fatal, sin status. */
    public GptClientException(String message, Throwable cause) {
        this(message, cause, null, null, false);
    }

    public GptClientException(String message, Throwable cause,
                              Integer httpStatus, String errorCode, boolean retryable) {
        super(message, cause);
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
        this.retryable = retryable;
    }

    public Integer getHttpStatus() {
        return httpStatus;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public boolean isRetryable() {
        return retryable;
    }
}