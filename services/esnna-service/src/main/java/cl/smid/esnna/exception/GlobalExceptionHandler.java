package cl.smid.esnna.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.util.List;

/**
 * Handler global. Garantiza que el stack trace nunca llegue al cliente, que cada
 * excepción mapee a un código predecible y que el detalle interno quede en logs.
 *
 * Notas:
 *  - 401/403 del flujo de autenticación los resuelven el entry point / access
 *    denied handler (ERR-1), NO este advice. Aquí solo queda AccessDeniedException
 *    lanzada dentro de un método (@PreAuthorize), por si se usa a futuro.
 *  - PRIV-2: nunca se loggea contenido de documentos; solo metadato y URI.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // ---------- Dominio ----------

    @ExceptionHandler(PdfExtractionException.class)
    public ResponseEntity<ApiError> handlePdf(PdfExtractionException ex, HttpServletRequest req) {
        log.warn("PDF inválido en {}: {}", req.getRequestURI(), ex.getMessage());
        return build(HttpStatus.UNPROCESSABLE_ENTITY, "PDF_INVALIDO",
                "El archivo no pudo ser procesado.", req);
    }

    @ExceptionHandler(GptRateLimitException.class)
    public ResponseEntity<ApiError> handleRateLimit(GptRateLimitException ex, HttpServletRequest req) {
        log.warn("Rate limit de OpenAI en {}: {}", req.getRequestURI(), ex.getMessage());
        ResponseEntity.BodyBuilder builder = ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE);
        if (ex.getRetryAfterSeconds() != null) {
            builder.header(HttpHeaders.RETRY_AFTER, String.valueOf(ex.getRetryAfterSeconds()));
        }
        return builder.body(ApiError.of(HttpStatus.SERVICE_UNAVAILABLE.value(),
                "MOTOR_IA_SATURADO", "El motor de análisis está saturado. Reintente en unos momentos.",
                req.getRequestURI()));
    }

    @ExceptionHandler(GptClientException.class)
    public ResponseEntity<ApiError> handleGpt(GptClientException ex, HttpServletRequest req) {
        log.error("Fallo en motor IA en {}: {}", req.getRequestURI(), ex.getMessage(), ex);
        return build(HttpStatus.BAD_GATEWAY, "MOTOR_IA_NO_DISPONIBLE",
                "El motor de análisis no respondió correctamente.", req);
    }

    @ExceptionHandler(EsnnaProcessingException.class)
    public ResponseEntity<ApiError> handleProcessing(EsnnaProcessingException ex, HttpServletRequest req) {
        log.error("Fallo de procesamiento en {}: {}", req.getRequestURI(), ex.getMessage(), ex);
        return build(HttpStatus.UNPROCESSABLE_ENTITY, "PROCESAMIENTO_FALLIDO",
                "No se pudo consolidar el caso con los documentos provistos.", req);
    }

    @ExceptionHandler(EsnnaValidationException.class)
    public ResponseEntity<ApiError> handleValidacionDominio(EsnnaValidationException ex, HttpServletRequest req) {
        return build(HttpStatus.BAD_REQUEST, "SOLICITUD_INVALIDA", ex.getMessage(), req);
    }

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<ApiError> handleNotFound(RecursoNoEncontradoException ex, HttpServletRequest req) {
        return build(HttpStatus.NOT_FOUND, "NO_ENCONTRADO", ex.getMessage(), req);
    }

    @ExceptionHandler(TransicionInvalidaException.class)
    public ResponseEntity<ApiError> handleTransicion(TransicionInvalidaException ex, HttpServletRequest req) {
        return build(HttpStatus.CONFLICT, "TRANSICION_INVALIDA", ex.getMessage(), req);
    }

    @ExceptionHandler(org.springframework.orm.ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<ApiError> handleOptimisticLock(org.springframework.orm.ObjectOptimisticLockingFailureException ex,
                                                         HttpServletRequest req) {
        log.warn("Edición concurrente en {}: {}", req.getRequestURI(), ex.getMessage());
        return build(HttpStatus.CONFLICT, "EDICION_CONCURRENTE",
                "El caso fue modificado por otra persona. Recargue y reintente.", req);
    }

    // ---------- Multipart / binding (ERR-4: antes caían al genérico = 500) ----------

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ApiError> handleUpload(MaxUploadSizeExceededException ex, HttpServletRequest req) {
        return build(HttpStatus.PAYLOAD_TOO_LARGE, "ARCHIVO_DEMASIADO_GRANDE",
                "El conjunto de archivos excede el tamaño permitido.", req);
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<ApiError> handleMissingPart(MissingServletRequestPartException ex, HttpServletRequest req) {
        return build(HttpStatus.BAD_REQUEST, "ARCHIVO_AUSENTE",
                "Falta el campo de archivos en la solicitud.", req);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiError> handleMissingParam(MissingServletRequestParameterException ex, HttpServletRequest req) {
        return build(HttpStatus.BAD_REQUEST, "PARAMETRO_AUSENTE",
                "Falta un parámetro requerido: " + ex.getParameterName(), req);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ApiError> handleMediaType(HttpMediaTypeNotSupportedException ex, HttpServletRequest req) {
        return build(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "TIPO_NO_SOPORTADO",
                "El tipo de contenido no es soportado por este endpoint.", req);
    }

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<ApiError> handleMultipart(MultipartException ex, HttpServletRequest req) {
        log.warn("Error de multipart en {}: {}", req.getRequestURI(), ex.getMessage());
        return build(HttpStatus.BAD_REQUEST, "MULTIPART_INVALIDO",
                "La solicitud multipart está mal formada.", req);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        List<String> detalles = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .toList();
        return ResponseEntity.badRequest().body(ApiError.of(HttpStatus.BAD_REQUEST.value(),
                "VALIDACION_FALLIDA", "Uno o más campos son inválidos.", req.getRequestURI(), detalles));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegal(IllegalArgumentException ex, HttpServletRequest req) {
        log.warn("IllegalArgumentException en {}: {}", req.getRequestURI(), ex.getMessage());
        return build(HttpStatus.BAD_REQUEST, "ARGUMENTO_INVALIDO",
                "La solicitud contiene argumentos inválidos.", req);
    }

    // ---------- Seguridad a nivel de método (@PreAuthorize) ----------

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> handleForbidden(AccessDeniedException ex, HttpServletRequest req) {
        return build(HttpStatus.FORBIDDEN, "ACCESO_DENEGADO",
                "Permisos insuficientes para esta operación.", req);
    }

    // ---------- Catch-all ----------

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneric(Exception ex, HttpServletRequest req) {
        log.error("Excepción no controlada en {}: {}", req.getRequestURI(), ex.getMessage(), ex);
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "ERROR_INTERNO",
                "Ocurrió un error inesperado.", req);
    }

    private ResponseEntity<ApiError> build(HttpStatus status, String error, String message, HttpServletRequest req) {
        return ResponseEntity.status(status)
                .body(ApiError.of(status.value(), error, message, req.getRequestURI()));
    }
}
