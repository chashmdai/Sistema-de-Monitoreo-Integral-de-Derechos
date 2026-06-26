package cl.smid.catalogo.api.error;

import cl.smid.catalogo.dominio.excepcion.CatalogoException;
import cl.smid.catalogo.dominio.excepcion.CodigoError;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Traduce las excepciones de la aplicación al {@link ErrorResponse} unificado (Núcleo 2.5).
 *
 * <p>Concentra aquí el mapeo excepción → sobre de error para que los controladores queden
 * limpios y las respuestas de error sean homogéneas. El detalle técnico de los errores
 * inesperados se registra en el log pero <b>nunca</b> se filtra al cliente (se responde con un
 * mensaje genérico y el código {@code CAT-500}).</p>
 *
 * <p>Los errores de autenticación (401) y autorización (403) no se manejan aquí, sino en los
 * componentes de Spring Security (punto de entrada y manejador de acceso denegado), que
 * escriben el mismo sobre antes de que la petición llegue a un controlador.</p>
 */
@RestControllerAdvice
public class ManejadorGlobalExcepciones {

    private static final Logger LOG = LoggerFactory.getLogger(ManejadorGlobalExcepciones.class);

    /** Errores de negocio del catálogo: el código ya trae su estado HTTP y mensaje. */
    @ExceptionHandler(CatalogoException.class)
    public ResponseEntity<ErrorResponse> manejarCatalogo(CatalogoException ex, HttpServletRequest req) {
        CodigoError codigo = ex.getCodigoError();
        ErrorResponse cuerpo = ErrorResponse.de(
                codigo, ex.getMessage(), req.getRequestURI(), ex.getDetalles(), Instant.now());
        return ResponseEntity.status(codigo.getHttpStatus()).body(cuerpo);
    }

    /** Validación de DTO con {@code @Valid} en el cuerpo: arma el mapa campo→mensaje. */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> manejarValidacionCuerpo(MethodArgumentNotValidException ex,
                                                                 HttpServletRequest req) {
        Map<String, String> detalles = new LinkedHashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            // Si un campo acumula varios errores, se conserva el primero (mensaje más relevante).
            detalles.putIfAbsent(error.getField(), error.getDefaultMessage());
        }
        return responder(CodigoError.VALIDACION,
                CodigoError.VALIDACION.getMensajePorDefecto(), req, detalles);
    }

    /** Validación de parámetros/anotaciones a nivel de método. */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> manejarViolaciones(ConstraintViolationException ex,
                                                            HttpServletRequest req) {
        Map<String, String> detalles = new LinkedHashMap<>();
        for (ConstraintViolation<?> v : ex.getConstraintViolations()) {
            detalles.putIfAbsent(v.getPropertyPath().toString(), v.getMessage());
        }
        return responder(CodigoError.VALIDACION,
                CodigoError.VALIDACION.getMensajePorDefecto(), req, detalles);
    }

    /** Cuerpo JSON ilegible o mal formado. */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> manejarCuerpoIlegible(HttpMessageNotReadableException ex,
                                                              HttpServletRequest req) {
        return responder(CodigoError.VALIDACION,
                "El cuerpo de la solicitud es ilegible o está mal formado.", req, null);
    }

    /** Salvaguarda final: cualquier error no controlado se registra y se responde genérico. */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> manejarInesperado(Exception ex, HttpServletRequest req) {
        LOG.error("Error interno no controlado al procesar {} {}",
                req.getMethod(), req.getRequestURI(), ex);
        return responder(CodigoError.ERROR_INTERNO,
                CodigoError.ERROR_INTERNO.getMensajePorDefecto(), req, null);
    }

    private ResponseEntity<ErrorResponse> responder(CodigoError codigo, String mensaje,
                                                    HttpServletRequest req, Map<String, String> detalles) {
        ErrorResponse cuerpo = ErrorResponse.de(
                codigo, mensaje, req.getRequestURI(), detalles, Instant.now());
        return ResponseEntity.status(codigo.getHttpStatus()).body(cuerpo);
    }
}
