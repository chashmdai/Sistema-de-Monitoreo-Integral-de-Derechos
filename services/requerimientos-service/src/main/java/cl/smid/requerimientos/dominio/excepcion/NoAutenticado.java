package cl.smid.requerimientos.dominio.excepcion;

/**
 * No autenticado: token ausente, mal firmado, expirado o inválido (AUTZ-003, HTTP 401).
 *
 * <p>Aunque la negativa de autenticación la materializa el {@code AuthenticationEntryPoint}
 * de la capa de infraestructura, se modela también como excepción de dominio para mantener
 * un catálogo de errores unificado.</p>
 */
public class NoAutenticado extends RequerimientoException {

    public NoAutenticado(String mensaje) {
        super(CodigoError.NO_AUTENTICADO, mensaje);
    }
}
