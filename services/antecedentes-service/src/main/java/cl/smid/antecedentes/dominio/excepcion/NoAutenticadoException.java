package cl.smid.antecedentes.dominio.excepcion;

/**
 * No autenticado ({@code AUTZ-003}, HTTP 401): falta el contexto de sesion (token ausente,
 * invalido o expirado) al intentar resolver el solicitante.
 */
public class NoAutenticadoException extends AntecedentesException {

    public NoAutenticadoException(String mensaje) {
        super(CodigoError.AUTZ_003, mensaje);
    }
}
