package cl.smid.personas.dominio.excepcion;

/**
 * Catálogo central de códigos de error del servicio de Personas (Núcleo 2.5).
 *
 * <p>Cada código liga un identificador estable de negocio (consumible por clientes y
 * por la futura Auditoría) con el estado HTTP que la API debe devolver y la frase HTTP
 * estándar. El dominio es puro: no conoce las clases de Spring, sólo el <i>número</i> de
 * estado, que la capa {@code api} traduce a {@code HttpStatus} en el sobre de error.</p>
 *
 * <p>Se incluyen los códigos transversales {@code AUTZ-003} (401) y {@code AUTZ-004} (403)
 * para mantener idéntico el contrato del ecosistema ya congelado (auth/catálogo), aunque
 * la denegación territorial de Personas se exprese como 404 por diseño (§2.3).</p>
 */
public enum CodigoError {

    /** Falla de validación de DTO de entrada (campos obligatorios, formato). */
    SOLICITUD_INVALIDA("PER-001", 400, "Bad Request"),

    /** El RUT no supera la validación de módulo 11 (Núcleo 5.5). */
    RUT_INVALIDO("PER-002", 422, "Unprocessable Entity"),

    /** Conflicto de unicidad: ya existe una persona vigente con el mismo RUT. */
    RUT_DUPLICADO("PER-003", 409, "Conflict"),

    /** La persona no existe o está fuera del alcance territorial del solicitante (§2.3). */
    PERSONA_NO_ENCONTRADA("PER-404", 404, "Not Found"),

    /** Error interno no esperado; no se filtra detalle al cliente. */
    ERROR_INTERNO("PER-500", 500, "Internal Server Error"),

    /** No autenticado / token ausente, mal firmado o inválido (defensa en profundidad). */
    NO_AUTENTICADO("AUTZ-003", 401, "Unauthorized"),

    /** Autenticado pero sin permiso por rol para la operación. */
    ACCESO_DENEGADO("AUTZ-004", 403, "Forbidden");

    private final String codigo;
    private final int httpStatus;
    private final String fraseHttp;

    CodigoError(String codigo, int httpStatus, String fraseHttp) {
        this.codigo = codigo;
        this.httpStatus = httpStatus;
        this.fraseHttp = fraseHttp;
    }

    /** Identificador estable del error (p. ej. {@code "PER-002"}). */
    public String codigo() {
        return codigo;
    }

    /** Estado HTTP asociado, como entero (el dominio no depende de tipos de Spring). */
    public int httpStatus() {
        return httpStatus;
    }

    /** Frase HTTP estándar (campo {@code error} del sobre, p. ej. {@code "Conflict"}). */
    public String fraseHttp() {
        return fraseHttp;
    }
}
