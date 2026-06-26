package cl.smid.personas.api.error;

import cl.smid.personas.dominio.excepcion.CodigoError;
import cl.smid.personas.dominio.excepcion.PersonaException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Traduce las excepciones a la representación HTTP unificada (Núcleo 2.5). Centraliza el mapeo
 * código de negocio → estado HTTP y garantiza que toda salida de error tenga la misma forma.
 *
 * <p>Tres familias:</p>
 * <ol>
 *   <li>{@link PersonaException}: errores de negocio del dominio; el {@link CodigoError} ya
 *       aporta estado, frase y código (PER-002/003/404, etc.).</li>
 *   <li>{@link MethodArgumentNotValidException}: fallos de Bean Validation en los DTOs; se
 *       devuelven como {@code PER-001} con el mapa {@code detalles} campo→mensaje.</li>
 *   <li>Cualquier otra excepción: se registra y se devuelve {@code PER-500} sin filtrar detalles
 *       internos al cliente.</li>
 * </ol>
 *
 * <p>Las excepciones de autenticación/autorización (401/403) NO se manejan aquí: las resuelven
 * el {@code AuthenticationEntryPoint} y el {@code AccessDeniedHandler} de la cadena de seguridad,
 * antes de llegar al {@code DispatcherServlet}.</p>
 */
@RestControllerAdvice
public class ManejadorGlobalExcepciones {

    private static final Logger log = LoggerFactory.getLogger(ManejadorGlobalExcepciones.class);

    /** Errores de negocio del dominio (mapeo directo por {@link CodigoError}). */
    @ExceptionHandler(PersonaException.class)
    public ResponseEntity<ErrorResponse> manejarPersonaException(PersonaException ex, HttpServletRequest req) {
        CodigoError ce = ex.codigoError();
        // Sólo en PER-001 (validación) se adjunta el mapa de detalles; en el resto va nulo y se omite.
        Map<String, String> detalles = ex.detalles().isEmpty() ? null : ex.detalles();

        ErrorResponse cuerpo = new ErrorResponse(
                ce.httpStatus(),
                ce.fraseHttp(),
                ce.codigo(),
                ex.getMessage(),
                detalles,
                req.getRequestURI(),
                Instant.now());

        // Los 5xx se registran como error; los 4xx (esperables) como advertencia ligera.
        if (ce.httpStatus() >= 500) {
            log.error("Error de negocio {} en {}: {}", ce.codigo(), req.getRequestURI(), ex.getMessage(), ex);
        } else {
            log.warn("Solicitud rechazada {} en {}: {}", ce.codigo(), req.getRequestURI(), ex.getMessage());
        }

        return ResponseEntity.status(ce.httpStatus()).body(cuerpo);
    }

    /** Fallos de validación de Bean Validation en el cuerpo de la petición → {@code PER-001}. */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> manejarValidacion(MethodArgumentNotValidException ex,
                                                            HttpServletRequest req) {
        // Se preserva el orden de aparición con un LinkedHashMap; campo → primer mensaje.
        Map<String, String> detalles = new LinkedHashMap<>();
        for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
            detalles.putIfAbsent(fe.getField(),
                    fe.getDefaultMessage() == null ? "valor inválido" : fe.getDefaultMessage());
        }

        CodigoError ce = CodigoError.SOLICITUD_INVALIDA;
        ErrorResponse cuerpo = new ErrorResponse(
                ce.httpStatus(),
                ce.fraseHttp(),
                ce.codigo(),
                "La solicitud contiene campos inválidos.",
                detalles,
                req.getRequestURI(),
                Instant.now());

        return ResponseEntity.status(ce.httpStatus()).body(cuerpo);
    }

    /** Red de seguridad final: cualquier excepción no prevista → {@code PER-500} sin fugas. */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> manejarGenerica(Exception ex, HttpServletRequest req) {
        CodigoError ce = CodigoError.ERROR_INTERNO;
        log.error("Error interno no controlado en {}", req.getRequestURI(), ex);

        ErrorResponse cuerpo = new ErrorResponse(
                ce.httpStatus(),
                ce.fraseHttp(),
                ce.codigo(),
                "Ocurrió un error interno al procesar la solicitud.",
                null,
                req.getRequestURI(),
                Instant.now());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(cuerpo);
    }
}
