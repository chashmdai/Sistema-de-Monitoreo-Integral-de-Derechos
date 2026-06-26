package cl.smid.esnna.exception;

/**
 * Falla en la orquestación del motor: consolidación inválida, mapeo final fallido.
 */
public class EsnnaProcessingException extends EsnnaException {

    public EsnnaProcessingException(String message) {
        super(message);
    }

    public EsnnaProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
