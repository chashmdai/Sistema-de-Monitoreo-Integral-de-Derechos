package cl.smid.requerimientos.dominio.excepcion;

import java.util.Map;

/**
 * Solicitud inválida (REQ-001, HTTP 400): validación de entrada o criterio ausente.
 */
public class SolicitudInvalida extends RequerimientoException {

    public SolicitudInvalida(String mensaje) {
        super(CodigoError.VALIDACION, mensaje);
    }

    public SolicitudInvalida(String mensaje, Map<String, String> detalles) {
        super(CodigoError.VALIDACION, mensaje, detalles);
    }
}
