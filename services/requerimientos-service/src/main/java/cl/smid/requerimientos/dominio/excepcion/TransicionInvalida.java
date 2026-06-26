package cl.smid.requerimientos.dominio.excepcion;

/**
 * Transición de estado no permitida por la máquina de estados (REQ-409, HTTP 409).
 *
 * <p>La lanza la máquina de estados del dominio cuando se intenta un evento que no tiene
 * arista válida desde el estado actual (p. ej. editar un requerimiento ya {@code ASIGNADO}).</p>
 */
public class TransicionInvalida extends RequerimientoException {

    public TransicionInvalida(String mensaje) {
        super(CodigoError.CONFLICTO, mensaje);
    }
}
