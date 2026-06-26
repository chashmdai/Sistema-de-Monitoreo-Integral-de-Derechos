package cl.smid.catalogo.dominio.excepcion;

import java.util.Collections;
import java.util.Map;

/**
 * Excepción de negocio del catálogo. Transporta un {@link CodigoError} estable y,
 * opcionalmente, un mapa de {@code detalles} campo→mensaje (para errores de validación).
 *
 * <p>El {@code ManejadorGlobalExcepciones} de la capa api la traduce al sobre de error
 * unificado 2.5 (status, error, codigo, mensaje, detalles?, ruta, timestamp).</p>
 *
 * <p>Es una {@link RuntimeException} a propósito: el dominio no obliga a los llamadores a
 * declararla, y aborta la unidad de trabajo (rollback transaccional) cuando se lanza.</p>
 */
public class CatalogoException extends RuntimeException {

    private final CodigoError codigoError;
    private final transient Map<String, String> detalles;

    public CatalogoException(CodigoError codigoError) {
        this(codigoError, codigoError.getMensajePorDefecto(), null);
    }

    public CatalogoException(CodigoError codigoError, String mensaje) {
        this(codigoError, mensaje, null);
    }

    public CatalogoException(CodigoError codigoError, String mensaje, Map<String, String> detalles) {
        super(mensaje);
        this.codigoError = codigoError;
        this.detalles = detalles;
    }

    // --- Fábricas de conveniencia para los casos más frecuentes ---

    public static CatalogoException derechoNoEncontrado(String altKey) {
        return new CatalogoException(CodigoError.DERECHO_NO_ENCONTRADO,
                "No existe un derecho con el identificador %s.".formatted(altKey));
    }

    public static CatalogoException codigoDerechoDuplicado(String codigo) {
        return new CatalogoException(CodigoError.CODIGO_DERECHO_DUPLICADO,
                "Ya existe un derecho con el código '%s'.".formatted(codigo));
    }

    public static CatalogoException codigoCausaDuplicado(String codigo) {
        return new CatalogoException(CodigoError.CODIGO_CAUSA_DUPLICADO,
                "Ya existe una causa con el código '%s' para este derecho.".formatted(codigo));
    }

    public static CatalogoException arbolInvalido(String mensaje) {
        return new CatalogoException(CodigoError.ARBOL_INVALIDO, mensaje);
    }

    public static CatalogoException codigoInmutable() {
        return new CatalogoException(CodigoError.CODIGO_INMUTABLE,
                CodigoError.CODIGO_INMUTABLE.getMensajePorDefecto());
    }

    public CodigoError getCodigoError() {
        return codigoError;
    }

    /** Detalles campo→mensaje (puede ser {@code null} si no aplica). */
    public Map<String, String> getDetalles() {
        return detalles == null ? null : Collections.unmodifiableMap(detalles);
    }
}
