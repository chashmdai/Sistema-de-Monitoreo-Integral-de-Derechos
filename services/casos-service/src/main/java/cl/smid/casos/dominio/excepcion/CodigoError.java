package cl.smid.casos.dominio.excepcion;

/**
 * Catálogo de códigos de error estables del servicio de Casos, con prefijo propio {@code CAS-xxx}
 * más los códigos de autorización compartidos {@code AUTZ-003}/{@code AUTZ-004} (Núcleo 2.5).
 *
 * <p>El {@code codigo} es un identificador estable por tipo de error, pensado para que el frontend
 * reaccione sin parsear el mensaje.</p>
 */
public enum CodigoError {

    /** Validación de DTO/parámetros o cuerpo ilegible. */
    CAS_001("CAS-001", 400, "Bad Request"),
    /** Inexistente o fuera de alcance territorial (no se revela la existencia). */
    CAS_404("CAS-404", 404, "Not Found"),
    /** Transición de estado en conflicto con el estado actual del caso. */
    CAS_409("CAS-409", 409, "Conflict"),
    /** Regla de negocio incumplida. */
    CAS_422("CAS-422", 422, "Unprocessable Entity"),
    /** Error interno (sin filtrar detalle). */
    CAS_500("CAS-500", 500, "Internal Server Error"),

    /** No autenticado: token ausente, mal firmado o expirado. */
    AUTZ_003("AUTZ-003", 401, "Unauthorized"),
    /** Autenticado pero sin el rol requerido (Coordinación en acciones administrativas). */
    AUTZ_004("AUTZ-004", 403, "Forbidden");

    private final String codigo;
    private final int httpStatus;
    private final String error;

    CodigoError(String codigo, int httpStatus, String error) {
        this.codigo = codigo;
        this.httpStatus = httpStatus;
        this.error = error;
    }

    public String codigo() { return codigo; }
    public int httpStatus() { return httpStatus; }
    public String error() { return error; }
}
