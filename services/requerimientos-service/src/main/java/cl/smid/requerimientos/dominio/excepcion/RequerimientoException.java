package cl.smid.requerimientos.dominio.excepcion;

import java.util.Map;

/**
 * Excepción base del dominio de Requerimientos. Lleva un {@link CodigoError} (que conoce su
 * estado HTTP) y, opcionalmente, un mapa de detalles campo→mensaje para errores de validación.
 *
 * <p>El dominio lanza esta excepción (o sus subclases) sin conocer HTTP; el manejador global
 * en la capa {@code api} la traduce al sobre de error unificado.</p>
 */
public class RequerimientoException extends RuntimeException {

    private final CodigoError codigoError;
    private final transient Map<String, String> detalles;

    /**
     * @param codigoError código estable del error
     * @param mensaje     mensaje específico (si es nulo se usa el por defecto del código)
     */
    public RequerimientoException(CodigoError codigoError, String mensaje) {
        this(codigoError, mensaje, null);
    }

    /**
     * @param codigoError código estable del error
     * @param mensaje     mensaje específico (si es nulo se usa el por defecto del código)
     * @param detalles    detalles campo→mensaje (solo para validación; puede ser nulo)
     */
    public RequerimientoException(CodigoError codigoError, String mensaje, Map<String, String> detalles) {
        super((mensaje == null || mensaje.isBlank()) ? codigoError.mensajePorDefecto() : mensaje);
        this.codigoError = codigoError;
        this.detalles = detalles;
    }

    /** @return código estable del error. */
    public CodigoError codigoError() {
        return codigoError;
    }

    /** @return detalles campo→mensaje, o {@code null} si no aplica. */
    public Map<String, String> detalles() {
        return detalles;
    }
}
