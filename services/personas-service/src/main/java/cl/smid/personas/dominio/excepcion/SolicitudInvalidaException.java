package cl.smid.personas.dominio.excepcion;

import java.util.Map;

/**
 * Se lanza ante una solicitud semánticamente inválida detectada por el dominio (p. ej.
 * búsqueda sin criterios, o datos incompatibles entre sí). Código {@code PER-001} → HTTP 400.
 *
 * <p>La validación sintáctica de DTOs la realiza Bean Validation en la capa {@code api} y
 * produce este mismo código con el mapa de {@code detalles}; esta excepción cubre las reglas
 * que sólo el dominio puede juzgar.</p>
 */
public class SolicitudInvalidaException extends PersonaException {

    public SolicitudInvalidaException(String mensaje) {
        super(CodigoError.SOLICITUD_INVALIDA, mensaje);
    }

    public SolicitudInvalidaException(String mensaje, Map<String, String> detalles) {
        super(CodigoError.SOLICITUD_INVALIDA, mensaje, detalles);
    }
}
