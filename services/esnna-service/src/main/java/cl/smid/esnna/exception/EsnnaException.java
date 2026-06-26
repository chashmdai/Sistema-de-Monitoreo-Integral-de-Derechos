package cl.smid.esnna.exception;

/**
 * Excepción base del dominio ESNNA. Permite al GlobalExceptionHandler discriminar
 * fallos propios vs externos.
 */
public abstract class EsnnaException extends RuntimeException {

    protected EsnnaException(String message) {
        super(message);
    }

    protected EsnnaException(String message, Throwable cause) {
        super(message, cause);
    }
}
