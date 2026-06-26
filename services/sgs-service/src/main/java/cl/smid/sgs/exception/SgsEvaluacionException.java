package cl.smid.sgs.exception;

/** Base de negocio del motor de evaluación. Se conserva como raíz de la rama de dominio. */
public class SgsEvaluacionException extends RuntimeException {
    public SgsEvaluacionException(String message) { super(message); }
    public SgsEvaluacionException(String message, Throwable cause) { super(message, cause); }
}
