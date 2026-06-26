package cl.smid.casos.dominio.excepcion;

/**
 * Parámetro o valor de entrada inválido a nivel semántico (p. ej. un nombre de estado o de acción que
 * no corresponde a ningún valor del enum). Responde {@code 400} (CAS-001).
 */
public class SolicitudInvalidaException extends ExcepcionDominio {

    public SolicitudInvalidaException(String mensaje) {
        super(CodigoError.CAS_001, mensaje);
    }
}
