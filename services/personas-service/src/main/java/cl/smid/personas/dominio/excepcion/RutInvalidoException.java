package cl.smid.personas.dominio.excepcion;

/**
 * Se lanza cuando un RUT presente no supera la validación de módulo 11 o tiene un formato
 * estructuralmente inválido (Núcleo 5.5). Código {@code PER-002} → HTTP 422.
 *
 * <p>La construye el objeto de valor {@code Rut} durante {@code normalizarYValidar(...)}.
 * No aplica cuando el RUT es nulo: en ese caso simplemente no se construye el {@code Rut}.</p>
 */
public class RutInvalidoException extends PersonaException {

    public RutInvalidoException(String mensaje) {
        super(CodigoError.RUT_INVALIDO, mensaje);
    }
}
