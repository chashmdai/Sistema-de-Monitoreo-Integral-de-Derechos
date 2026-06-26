package cl.smid.esnna.exception;

/**
 * La cola del executor de análisis está llena (esnna.jobs.concurrencia +
 * esnna.jobs.cola agotados). Se mapea a 429 + Retry-After: el cliente debe
 * reintentar el submit, no es una falla del motor.
 */
public class CapacidadExcedidaException extends EsnnaException {

    public CapacidadExcedidaException(String message) {
        super(message);
    }
}