package cl.smid.antecedentes.dominio.excepcion;

import java.util.Map;

/**
 * Error de validacion de entrada ({@code ANT-001}, HTTP 400). Puede portar un mapa de
 * {@code detalles} campo→mensaje.
 */
public class ValidacionException extends AntecedentesException {

    public ValidacionException(String mensaje) {
        super(CodigoError.ANT_001, mensaje);
    }

    public ValidacionException(String mensaje, Map<String, String> detalles) {
        super(CodigoError.ANT_001, mensaje, detalles);
    }
}
