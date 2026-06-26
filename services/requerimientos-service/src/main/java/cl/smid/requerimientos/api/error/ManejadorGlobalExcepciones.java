package cl.smid.requerimientos.api.error;

import cl.smid.requerimientos.dominio.excepcion.CodigoError;
import cl.smid.requerimientos.dominio.excepcion.RequerimientoException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Manejador global que traduce las excepciones al sobre de error unificado (Núcleo 2.5). El dominio
 * lanza {@link RequerimientoException} sin conocer HTTP; aquí se mapea su {@link CodigoError} al
 * estado correspondiente. Los errores de (de)serialización y validación se unifican como
 * {@code REQ-001}; cualquier excepción no prevista se degrada a {@code REQ-500} sin filtrar el
 * detalle interno.
 */
@RestControllerAdvice
public class ManejadorGlobalExcepciones {

    private static final Logger log = LoggerFactory.getLogger(ManejadorGlobalExcepciones.class);

    /** Excepciones de dominio: el código ya determina el estado HTTP y el mensaje. */
    @ExceptionHandler(RequerimientoException.class)
    public ResponseEntity<ErrorResponse> manejarDominio(RequerimientoException ex, HttpServletRequest req) {
        CodigoError codigo = ex.codigoError();
        if (codigo == CodigoError.INTERNO) {
            log.error("Error interno de dominio en {}", req.getRequestURI(), ex);
        }
        return construir(codigo, ex.getMessage(), ex.detalles(), req);
    }

    /** Validación de @Valid sobre el cuerpo: arma el mapa campo→mensaje. */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> manejarValidacionCuerpo(MethodArgumentNotValidException ex,
                                                                 HttpServletRequest req) {
        Map<String, String> detalles = new LinkedHashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            detalles.putIfAbsent(error.getField(), mensajeCampo(error));
        }
        return construir(CodigoError.VALIDACION, "La solicitud tiene campos inválidos.", detalles, req);
    }

    /** Validación de @Validated sobre parámetros (p. ej. de consulta). */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> manejarViolaciones(ConstraintViolationException ex,
                                                             HttpServletRequest req) {
        Map<String, String> detalles = new LinkedHashMap<>();
        for (ConstraintViolation<?> v : ex.getConstraintViolations()) {
            detalles.putIfAbsent(v.getPropertyPath().toString(), v.getMessage());
        }
        return construir(CodigoError.VALIDACION, "La solicitud tiene parámetros inválidos.", detalles, req);
    }

    /** Cuerpo ilegible o valor de enumerado inválido en el JSON. */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> manejarCuerpoIlegible(HttpMessageNotReadableException ex,
                                                               HttpServletRequest req) {
        return construir(CodigoError.VALIDACION,
                "El cuerpo de la solicitud es ilegible o contiene un valor no permitido.", null, req);
    }

    /** Tipo incompatible en un parámetro (p. ej. estado no perteneciente al enumerado). */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> manejarTipoParametro(MethodArgumentTypeMismatchException ex,
                                                              HttpServletRequest req) {
        String detalle = "El parámetro '" + ex.getName() + "' tiene un valor no permitido.";
        return construir(CodigoError.VALIDACION, detalle, null, req);
    }

    /** Autenticado, pero sin permiso suficiente en seguridad por método (@PreAuthorize). */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> manejarAccesoDenegado(AccessDeniedException ex,
                                                               HttpServletRequest req) {
        return construir(CodigoError.ACCESO_DENEGADO, null, null, req);
    }

    /** Red de seguridad: cualquier otra excepción se degrada a 500 sin filtrar detalles. */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> manejarNoPrevisto(Exception ex, HttpServletRequest req) {
        log.error("Error no previsto en {}", req.getRequestURI(), ex);
        return construir(CodigoError.INTERNO, null, null, req);
    }

    private ResponseEntity<ErrorResponse> construir(CodigoError codigo, String mensaje,
                                                    Map<String, String> detalles, HttpServletRequest req) {
        HttpStatus estado = HttpStatus.valueOf(codigo.httpStatus());
        String mensajeFinal = (mensaje == null || mensaje.isBlank()) ? codigo.mensajePorDefecto() : mensaje;
        Map<String, String> detallesFinal = (detalles == null || detalles.isEmpty()) ? null : detalles;
        ErrorResponse cuerpo = new ErrorResponse(
                estado.value(),
                estado.getReasonPhrase(),
                codigo.codigo(),
                mensajeFinal,
                detallesFinal,
                req.getRequestURI(),
                Instant.now());
        return ResponseEntity.status(estado).body(cuerpo);
    }

    private String mensajeCampo(FieldError error) {
        String mensaje = error.getDefaultMessage();
        return (mensaje == null || mensaje.isBlank()) ? "valor inválido" : mensaje;
    }
}
