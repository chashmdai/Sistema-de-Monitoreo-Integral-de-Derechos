package cl.smid.sgs.exception;

/** Entrada inválida -> 400. */
public class SgsValidationException extends RuntimeException {
    public SgsValidationException(String message) { super(message); }
}
