package cl.smid.catalogo.dominio.excepcion;

/**
 * Catálogo estable de códigos de error del servicio (sobre unificado 2.5 del Núcleo).
 *
 * <p>El frontend y los servicios consumidores <b>conmutan sobre {@code codigo}</b> (estable),
 * nunca sobre el mensaje. La convención del ecosistema usa un prefijo por servicio:
 * {@code AUTZ} (auth), {@code CAT} (catálogo), {@code PER} (personas), {@code REQ}
 * (requerimientos). Por eso:</p>
 * <ul>
 *   <li>Los errores propios del dominio Catálogo usan el prefijo <b>{@code CAT-}</b>.</li>
 *   <li>Los errores de <b>autenticación/autorización</b> se normalizan reutilizando los
 *       códigos <b>{@code AUTZ-}</b> ya definidos por auth (README-auth §7), de modo que el
 *       frontend reaccione igual sin importar qué servicio los emita.</li>
 * </ul>
 *
 * <p>Cada constante lleva su {@code codigo} estable, el {@code httpStatus} asociado y un
 * mensaje por defecto en español (que el llamador puede sobrescribir con detalle puntual).</p>
 */
public enum CodigoError {

    // ----- Reutilizados del dominio de autorización (AUTZ-, README-auth §7) -----
    /** Falta el token, está mal formado, su firma es inválida, su iss/aud no corresponden o expiró. */
    NO_AUTENTICADO("AUTZ-003", 401, "No autenticado: se requiere un token válido."),
    /** El usuario está autenticado pero carece del rol de Administración exigido para escribir. */
    ACCESO_DENEGADO("AUTZ-004", 403, "Acceso denegado: se requiere rol de Administración."),

    // ----- Propios del dominio Catálogo (CAT-) -----
    /** No existe un derecho vigente o histórico con el alt_key indicado. */
    DERECHO_NO_ENCONTRADO("CAT-001", 404, "El derecho solicitado no existe."),
    /** Ya existe un derecho con ese código (el código es único en todo el catálogo). */
    CODIGO_DERECHO_DUPLICADO("CAT-002", 409, "Ya existe un derecho con ese código."),
    /** Ya existe una causa con ese código dentro del derecho (unicidad por derecho). */
    CODIGO_CAUSA_DUPLICADO("CAT-003", 409, "Ya existe una causa con ese código para este derecho."),
    /** Operación inválida sobre el árbol: padre inexistente, ciclo o mover un nodo bajo su descendencia. */
    ARBOL_INVALIDO("CAT-004", 422, "La operación produce un árbol inválido (padre inexistente o ciclo)."),
    /** La solicitud no supera la validación de datos; acompaña un mapa campo→mensaje en 'detalles'. */
    VALIDACION("CAT-005", 400, "La solicitud no supera la validación de datos."),
    /** Se intentó modificar el código de un derecho, que es inmutable. */
    CODIGO_INMUTABLE("CAT-006", 409, "El código de un derecho es inmutable y no puede modificarse."),
    /** Error interno no controlado; el detalle real queda en el log y nunca se filtra al cliente. */
    ERROR_INTERNO("CAT-500", 500, "Error interno del servicio de catálogo.");

    private final String codigo;
    private final int httpStatus;
    private final String mensajePorDefecto;

    CodigoError(String codigo, int httpStatus, String mensajePorDefecto) {
        this.codigo = codigo;
        this.httpStatus = httpStatus;
        this.mensajePorDefecto = mensajePorDefecto;
    }

    public String getCodigo() { return codigo; }

    public int getHttpStatus() { return httpStatus; }

    public String getMensajePorDefecto() { return mensajePorDefecto; }
}
