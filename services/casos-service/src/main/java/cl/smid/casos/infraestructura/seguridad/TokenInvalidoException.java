package cl.smid.casos.infraestructura.seguridad;

/**
 * Señala que un JWT no superó la validación (firma, emisor, audiencia, expiración o claims
 * obligatorios ausentes). El filtro de autenticación la traduce en una respuesta 401 (AUTZ-003).
 */
public class TokenInvalidoException extends RuntimeException {

    public TokenInvalidoException(String mensaje) {
        super(mensaje);
    }

    public TokenInvalidoException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
