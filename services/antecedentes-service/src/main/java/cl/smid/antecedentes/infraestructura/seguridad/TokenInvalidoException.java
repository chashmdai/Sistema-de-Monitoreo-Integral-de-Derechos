package cl.smid.antecedentes.infraestructura.seguridad;

/**
 * Excepcion interna de la capa de seguridad para tokens invalidos (firma, expiracion, emisor o
 * audiencia). El filtro la captura y deja la peticion sin autenticar, lo que deriva en 401
 * ({@code AUTZ-003}) en el punto de entrada de seguridad.
 */
public class TokenInvalidoException extends RuntimeException {

    public TokenInvalidoException(String mensaje) {
        super(mensaje);
    }
}
