package cl.smid.antecedentes.dominio.excepcion;

/**
 * Autenticado pero sin el rol requerido ({@code AUTZ-004}, HTTP 403): falta rol revisor
 * (acciones de revision) o rol administrador (escritura de tablas de referencia).
 *
 * <p>Importante: este 403 es por <em>rol</em>. La denegacion <em>territorial</em> de una
 * ficha se expresa como 404 ({@link RecursoNoEncontradoException}), no como 403.</p>
 */
public class AccesoDenegadoException extends AntecedentesException {

    public AccesoDenegadoException(String mensaje) {
        super(CodigoError.AUTZ_004, mensaje);
    }
}
