package cl.smid.antecedentes.dominio.excepcion;

import java.util.Map;

/**
 * Raiz de la jerarquia de excepciones de dominio. Cada excepcion porta su
 * {@link CodigoError} (que a su vez fija el estado HTTP) y, opcionalmente, un mapa de
 * {@code detalles} campo→mensaje que el sobre de error solo expone en validacion.
 */
public abstract class AntecedentesException extends RuntimeException {

    private final transient CodigoError codigoError;
    private final transient Map<String, String> detalles;

    protected AntecedentesException(CodigoError codigoError, String mensaje) {
        this(codigoError, mensaje, null);
    }

    protected AntecedentesException(CodigoError codigoError, String mensaje, Map<String, String> detalles) {
        super(mensaje);
        this.codigoError = codigoError;
        this.detalles = detalles;
    }

    public CodigoError codigoError() {
        return codigoError;
    }

    /** Detalles de validacion (campo→mensaje); puede ser nulo. */
    public Map<String, String> detalles() {
        return detalles;
    }
}
