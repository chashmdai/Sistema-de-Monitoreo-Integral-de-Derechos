package cl.smid.requerimientos.dominio.excepcion;

/**
 * Catálogo estable de códigos de error del servicio de Requerimientos (Núcleo 2.5).
 *
 * <p>El {@code codigo} es estable por tipo de error para que el frontend reaccione sin
 * parsear el mensaje. Usa el prefijo {@code REQ-###} para los errores propios y conserva
 * los transversales {@code AUTZ-003} (401) y {@code AUTZ-004} (403) del ecosistema.</p>
 *
 * <p>Cada constante conoce su estado HTTP, de modo que el manejador global de excepciones
 * no necesita reglas dispersas.</p>
 */
public enum CodigoError {

    /** Solicitud inválida: validación de DTO o criterio ausente. */
    VALIDACION("REQ-001", 400, "La solicitud no es válida."),

    /** Recurso inexistente o fuera del alcance territorial (no se revela su existencia). */
    NO_ENCONTRADO("REQ-404", 404, "El requerimiento no existe o está fuera de su alcance."),

    /** Conflicto de estado (transición inválida) o de folio. */
    CONFLICTO("REQ-409", 409, "La operación entra en conflicto con el estado actual del requerimiento."),

    /** Regla de negocio violada (p. ej. faltan mínimos para ingresar). */
    REGLA_NEGOCIO("REQ-422", 422, "No se cumple una regla de negocio del requerimiento."),

    /** Error interno no esperado. Nunca filtra el detalle al cliente. */
    INTERNO("REQ-500", 500, "Ocurrió un error interno. Intente nuevamente más tarde."),

    /** No autenticado: token ausente, mal firmado, expirado o inválido. */
    NO_AUTENTICADO("AUTZ-003", 401, "No autenticado. Falta una credencial válida."),

    /** Autenticado pero sin el rol requerido (p. ej. Coordinación en admisibilidad). */
    ACCESO_DENEGADO("AUTZ-004", 403, "No tiene permiso para realizar esta acción.");

    private final String codigo;
    private final int httpStatus;
    private final String mensajePorDefecto;

    CodigoError(String codigo, int httpStatus, String mensajePorDefecto) {
        this.codigo = codigo;
        this.httpStatus = httpStatus;
        this.mensajePorDefecto = mensajePorDefecto;
    }

    /** @return identificador estable (p. ej. {@code REQ-409}). */
    public String codigo() {
        return codigo;
    }

    /** @return código de estado HTTP asociado. */
    public int httpStatus() {
        return httpStatus;
    }

    /** @return mensaje por defecto en español. */
    public String mensajePorDefecto() {
        return mensajePorDefecto;
    }
}
