package cl.smid.casos.dominio.excepcion;

/**
 * Raíz de las excepciones de negocio del dominio de Casos. Transporta un {@link CodigoError} para
 * que la capa API construya el sobre de error unificado sin acoplarse a detalles internos.
 */
public abstract class ExcepcionDominio extends RuntimeException {

    private final transient CodigoError codigoError;

    protected ExcepcionDominio(CodigoError codigoError, String mensaje) {
        super(mensaje);
        this.codigoError = codigoError;
    }

    public CodigoError codigoError() {
        return codigoError;
    }
}
