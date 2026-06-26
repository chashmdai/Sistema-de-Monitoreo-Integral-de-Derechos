package cl.smid.casos.dominio.excepcion;

/**
 * El usuario está autenticado pero carece del rol requerido para la operación (típicamente, rol de
 * Coordinación para acciones administrativas del expediente). Responde {@code 403} (AUTZ-004).
 *
 * <p>Importante: esto es DISTINTO del recorte territorial, que responde {@code 404} para no revelar
 * la existencia del recurso. Aquí el recurso es visible para el usuario, pero la acción concreta
 * excede sus facultades.</p>
 */
public class AccesoDenegadoException extends ExcepcionDominio {

    public AccesoDenegadoException(String mensaje) {
        super(CodigoError.AUTZ_004, mensaje);
    }
}
