package cl.smid.instituciones.dominio.excepcion;

/**
 * El recurso solicitado (tipo, institución o punto focal) no existe.
 * Se traduce a {@code INS-404 / 404}.
 */
public class RecursoNoEncontradoException extends ExcepcionDominio {

    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }

    @Override
    public CodigoError codigo() {
        return CodigoError.RECURSO_NO_ENCONTRADO;
    }
}
