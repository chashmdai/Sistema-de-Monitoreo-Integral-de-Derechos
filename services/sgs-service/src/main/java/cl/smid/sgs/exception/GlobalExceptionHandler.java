package cl.smid.sgs.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.List;
import java.util.UUID;

/** Advice global. Mapea cada excepción a su status real (CTRL-5) y nunca filtra contenido sensible (PRIV-2). */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private String traceId() { return UUID.randomUUID().toString().substring(0, 8); }

    private ResponseEntity<ApiError> build(HttpStatus status, String message, HttpServletRequest req) {
        String tid = traceId();
        log.warn("[{}] {} {} -> {}: {}", tid, req.getMethod(), req.getRequestURI(), status.value(), message);
        return ResponseEntity.status(status)
                .body(ApiError.of(status.value(), status.getReasonPhrase(), message, req.getRequestURI(), tid));
    }

    @ExceptionHandler(SgsValidationException.class)
    public ResponseEntity<ApiError> validation(SgsValidationException e, HttpServletRequest req) {
        return build(HttpStatus.BAD_REQUEST, e.getMessage(), req);
    }

    @ExceptionHandler(SgsNotFoundException.class)
    public ResponseEntity<ApiError> notFound(SgsNotFoundException e, HttpServletRequest req) {
        return build(HttpStatus.NOT_FOUND, e.getMessage(), req);
    }

    @ExceptionHandler(GptRateLimitException.class)
    public ResponseEntity<ApiError> rateLimit(GptRateLimitException e, HttpServletRequest req) {
        return build(HttpStatus.TOO_MANY_REQUESTS, "El motor de IA está saturado, reintente en unos minutos.", req);
    }

    @ExceptionHandler({OpenAiException.class, SgsEvaluacionException.class})
    public ResponseEntity<ApiError> motor(RuntimeException e, HttpServletRequest req) {
        return build(HttpStatus.BAD_GATEWAY, "Fallo en el motor de IA.", req);
    }

    @ExceptionHandler(PdfExtractionException.class)
    public ResponseEntity<ApiError> pdf(PdfExtractionException e, HttpServletRequest req) {
        return build(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), req);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> illegal(IllegalArgumentException e, HttpServletRequest req) {
        return build(HttpStatus.BAD_REQUEST, e.getMessage(), req);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ApiError> maxSize(MaxUploadSizeExceededException e, HttpServletRequest req) {
        return build(HttpStatus.PAYLOAD_TOO_LARGE, "El archivo excede el tamaño máximo permitido.", req);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> beanValidation(MethodArgumentNotValidException e, HttpServletRequest req) {
        List<String> detalles = e.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage()).toList();
        String tid = traceId();
        return ResponseEntity.badRequest().body(new ApiError(
                java.time.LocalDateTime.now(), 400, "Bad Request", "Validación fallida",
                req.getRequestURI(), tid, detalles));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> generic(Exception e, HttpServletRequest req) {
        String tid = traceId();
        log.error("[{}] error no controlado en {} {}", tid, req.getMethod(), req.getRequestURI(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiError.of(500, "Internal Server Error", "Error interno del servicio.", req.getRequestURI(), tid));
    }
}
