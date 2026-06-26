package cl.smid.instituciones.dominio.excepcion;

/**
 * El usuario está autenticado pero carece del rol administrador requerido para
 * una escritura sobre datos de referencia. Se traduce a {@code AUTZ-004 / 403}.
 */
public class NoAutorizadoException extends ExcepcionDominio {

    public NoAutorizadoException(String mensaje) {
        super(mensaje);
    }

    @Override
    public CodigoError codigo() {
        return CodigoError.NO_AUTORIZADO;
    }
}
