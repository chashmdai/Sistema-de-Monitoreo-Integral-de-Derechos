package cl.smid.esnna.exception;

/**
 * Validación de entrada con mensaje seguro para el cliente (HTTP 400).
 * ERR-6: se agrega el constructor con causa para consistencia con el resto de la jerarquía.
 */
public class EsnnaValidationException extends EsnnaException {

    public EsnnaValidationException(String message) {
        super(message);
    }

    public EsnnaValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
