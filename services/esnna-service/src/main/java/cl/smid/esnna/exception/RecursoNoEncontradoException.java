package cl.smid.esnna.exception;

/**
 * Recurso inexistente (HTTP 404). Para GET/PATCH/DELETE de un caso por id.
 */
public class RecursoNoEncontradoException extends EsnnaException {

    public RecursoNoEncontradoException(String message) {
        super(message);
    }
}
