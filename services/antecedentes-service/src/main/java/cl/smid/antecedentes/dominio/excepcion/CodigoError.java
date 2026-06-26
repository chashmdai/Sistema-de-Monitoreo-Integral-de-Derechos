package cl.smid.antecedentes.dominio.excepcion;

/**
 * Catalogo de codigos de error del servicio (prefijo {@code ANT-}) mas los codigos de
 * autorizacion compartidos del cluster ({@code AUTZ-003}/{@code AUTZ-004}). Cada codigo
 * fija su estado HTTP (como entero, para no acoplar el dominio a Spring). El sobre de
 * error del cluster conmuta sobre {@code codigo} (estable), no sobre el mensaje.
 */
public enum CodigoError {

    /** Validacion / cuerpo ilegible / parametro invalido. */
    ANT_001("ANT-001", 400),

    /** Recurso inexistente o denegacion territorial (no se revela existencia). */
    ANT_404("ANT-404", 404),

    /** Conflicto de unicidad (folio, codigo de referencia). */
    ANT_409("ANT-409", 409),

    /** Regla de negocio (transicion invalida, incoherencia de hallazgo, referencia no vigente). */
    ANT_422("ANT-422", 422),

    /** Error interno (nunca filtra el detalle al cliente). */
    ANT_500("ANT-500", 500),

    /** No autenticado (token ausente/invalido/expirado). */
    AUTZ_003("AUTZ-003", 401),

    /** Autenticado sin rol suficiente (revision/admin). */
    AUTZ_004("AUTZ-004", 403);

    private final String codigo;
    private final int httpStatus;

    CodigoError(String codigo, int httpStatus) {
        this.codigo = codigo;
        this.httpStatus = httpStatus;
    }

    public String codigo() {
        return codigo;
    }

    public int httpStatus() {
        return httpStatus;
    }
}
