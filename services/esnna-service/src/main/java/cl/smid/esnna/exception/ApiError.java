package cl.smid.esnna.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.slf4j.MDC;

import java.time.Instant;
import java.util.List;

/**
 * Forma única de error al cliente. Sin stack traces ni detalle interno.
 *
 * - traceId (ERR-2): correlación con los logs. Se toma del MDC poblado por
 *   Micrometer Tracing (actuator); null si no hay tracing activo.
 * - detalles (ERR-3): lista opcional para validación múltiple o para indicar
 *   qué documentos fallaron, sin inflar el contrato escalar.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiError(
        Instant timestamp,
        int status,
        String error,
        String message,
        String path,
        String traceId,
        List<String> detalles
) {
    public static ApiError of(int status, String error, String message, String path) {
        return new ApiError(Instant.now(), status, error, message, path, MDC.get("traceId"), null);
    }

    public static ApiError of(int status, String error, String message, String path, List<String> detalles) {
        return new ApiError(Instant.now(), status, error, message, path, MDC.get("traceId"),
                (detalles == null || detalles.isEmpty()) ? null : detalles);
    }
}
