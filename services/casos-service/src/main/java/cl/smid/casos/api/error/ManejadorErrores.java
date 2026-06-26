package cl.smid.casos.api.error;

import cl.smid.casos.dominio.excepcion.CodigoError;
import cl.smid.casos.dominio.excepcion.ExcepcionDominio;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Traducción centralizada de excepciones al sobre de error unificado. Toda excepción de dominio porta
 * su {@link CodigoError}, de modo que un único manejador resuelve estado, código y mensaje de forma
 * coherente. Los errores de validación añaden el mapa {@code detalles}.
 */
@RestControllerAdvice
public class ManejadorErrores {

    /** Excepciones de negocio: el código/estado provienen del propio {@link CodigoError}. */
    @ExceptionHandler(ExcepcionDominio.class)
    public ResponseEntity<SobreError> manejarDominio(ExcepcionDominio ex, HttpServletRequest request) {
        return construir(ex.codigoError(), ex.getMessage(), null, request);
    }

    /** Validación de DTO: 400 CAS-001 con detalle por campo. */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<SobreError> manejarValidacion(MethodArgumentNotValidException ex,
                                                        HttpServletRequest request) {
        Map<String, String> detalles = new LinkedHashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            detalles.putIfAbsent(error.getField(), error.getDefaultMessage());
        }
        return construir(CodigoError.CAS_001, "La solicitud contiene datos inválidos.", detalles, request);
    }

    /** Cuerpo ilegible o parámetros inválidos: 400 CAS-001 (sin detalle por campo). */
    @ExceptionHandler({HttpMessageNotReadableException.class,
            MissingServletRequestParameterException.class,
            MethodArgumentTypeMismatchException.class})
    public ResponseEntity<SobreError> manejarSolicitudInvalida(Exception ex, HttpServletRequest request) {
        return construir(CodigoError.CAS_001, "La solicitud no pudo interpretarse correctamente.",
                null, request);
    }

    /** Salvaguarda final: 500 CAS-500 sin filtrar detalles internos. */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<SobreError> manejarInesperado(Exception ex, HttpServletRequest request) {
        return construir(CodigoError.CAS_500, "Ocurrió un error interno al procesar la solicitud.",
                null, request);
    }

    private ResponseEntity<SobreError> construir(CodigoError codigo, String mensaje,
                                                 Map<String, String> detalles, HttpServletRequest request) {
        SobreError sobre = new SobreError(codigo.httpStatus(), codigo.error(), codigo.codigo(),
                mensaje, detalles, request.getRequestURI(), Instant.now());
        return ResponseEntity.status(codigo.httpStatus()).body(sobre);
    }
}
