package cl.smid.sgs.exception;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.List;

/** Cuerpo de error uniforme (ERR-1/ERR-2). Reemplaza el JSON por concatenación de String del controller (CTRL-4). */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiError(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String path,
        String traceId,
        List<String> detalles
) {
    public static ApiError of(int status, String error, String message, String path, String traceId) {
        return new ApiError(LocalDateTime.now(), status, error, message, path, traceId, null);
    }
}
