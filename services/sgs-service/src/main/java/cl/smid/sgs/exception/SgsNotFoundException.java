package cl.smid.sgs.exception;

/** Recurso inexistente -> 404. */
public class SgsNotFoundException extends RuntimeException {
    public SgsNotFoundException(String message) { super(message); }
}
