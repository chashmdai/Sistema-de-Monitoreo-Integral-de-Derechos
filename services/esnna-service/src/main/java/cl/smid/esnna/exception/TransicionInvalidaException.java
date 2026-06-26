package cl.smid.esnna.exception;

/**
 * Transición de estado no permitida por la máquina de estados (HTTP 409).
 */
public class TransicionInvalidaException extends EsnnaException {

    public TransicionInvalidaException(String message) {
        super(message);
    }
}
