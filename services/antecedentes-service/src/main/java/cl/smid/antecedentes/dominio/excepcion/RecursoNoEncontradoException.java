package cl.smid.antecedentes.dominio.excepcion;

/**
 * Recurso inexistente <strong>o</strong> fuera del alcance territorial del solicitante
 * ({@code ANT-404}, HTTP 404). La denegacion territorial se expresa como 404 (no 403) para
 * no revelar la existencia del recurso (override #5).
 */
public class RecursoNoEncontradoException extends AntecedentesException {

    public RecursoNoEncontradoException(String mensaje) {
        super(CodigoError.ANT_404, mensaje);
    }
}
