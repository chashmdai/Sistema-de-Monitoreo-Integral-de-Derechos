package cl.smid.instituciones.dominio.excepcion;

/**
 * Catálogo de códigos de error del servicio. Cada entrada fija su código estable
 * (sobre el que conmutan el frontend y los servicios consumidores), su estado
 * HTTP y un título legible para el campo {@code error} del sobre unificado.
 *
 * <p>Los códigos {@code INS-xxx} son propios del dominio de instituciones; los
 * {@code AUTZ-xxx} son transversales del ecosistema (autenticación/autorización).</p>
 */
public enum CodigoError {

    /** Validación de DTO o de entrada; cuerpo ilegible; parámetro faltante. */
    VALIDACION("INS-001", 400, "Bad Request"),
    /** Recurso inexistente. */
    RECURSO_NO_ENCONTRADO("INS-404", 404, "Not Found"),
    /** Conflicto de unicidad (nombre de tipo o código de institución duplicado). */
    CONFLICTO("INS-409", 409, "Conflict"),
    /** Regla de negocio incumplida (tipo inexistente/no vigente, RUT inválido). */
    REGLA_NEGOCIO("INS-422", 422, "Unprocessable Entity"),
    /** Error interno no esperado (nunca filtra el detalle al cliente). */
    ERROR_INTERNO("INS-500", 500, "Internal Server Error"),
    /** No autenticado: token ausente, mal firmado, expirado o inválido. */
    NO_AUTENTICADO("AUTZ-003", 401, "Unauthorized"),
    /** Autenticado pero sin rol suficiente para la operación. */
    NO_AUTORIZADO("AUTZ-004", 403, "Forbidden");

    private final String codigo;
    private final int httpStatus;
    private final String titulo;

    CodigoError(String codigo, int httpStatus, String titulo) {
        this.codigo = codigo;
        this.httpStatus = httpStatus;
        this.titulo = titulo;
    }

    /** @return el código estable (por ejemplo {@code INS-422}). */
    public String codigo() {
        return codigo;
    }

    /** @return el estado HTTP asociado. */
    public int httpStatus() {
        return httpStatus;
    }

    /** @return el título legible (campo {@code error} del sobre). */
    public String titulo() {
        return titulo;
    }
}
