package cl.smid.antecedentes.api.error;

import cl.smid.antecedentes.dominio.excepcion.AntecedentesException;
import cl.smid.antecedentes.dominio.excepcion.CodigoError;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Traduce las excepciones a un {@link SobreError} unificado. Las de dominio portan su
 * {@link CodigoError}; los fallos de validacion de entrada se mapean a {@code ANT-001} con
 * {@code detalles}; cualquier otro error no controlado a {@code ANT-500} (sin filtrar el detalle
 * interno al cliente).
 */
@RestControllerAdvice
public class ManejadorGlobalExcepciones {

    private static final Logger log = LoggerFactory.getLogger(ManejadorGlobalExcepciones.class);

    /** Excepciones de dominio: usan su propio codigo y estado HTTP. */
    @ExceptionHandler(AntecedentesException.class)
    public ResponseEntity<SobreError> manejarDominio(AntecedentesException ex, HttpServletRequest request) {
        CodigoError codigo = ex.codigoError();
        if (codigo.httpStatus() >= 500) {
            log.error("Error de dominio {} en {}", codigo.codigo(), request.getRequestURI(), ex);
        }
        SobreError sobre = SobreError.de(codigo.codigo(), ex.getMessage(), request.getRequestURI(), ex.detalles());
        return ResponseEntity.status(codigo.httpStatus()).body(sobre);
    }

    /** Validacion de @Valid sobre el cuerpo: ANT-001 con detalles campo->mensaje. */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<SobreError> manejarValidacionCuerpo(MethodArgumentNotValidException ex,
                                                              HttpServletRequest request) {
        Map<String, String> detalles = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                detalles.putIfAbsent(error.getField(), error.getDefaultMessage()));
        return error400("Datos de entrada invalidos.", request, detalles);
    }

    /** Validacion de parametros (@RequestParam/@PathVariable con restricciones). */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<SobreError> manejarRestricciones(ConstraintViolationException ex,
                                                           HttpServletRequest request) {
        Map<String, String> detalles = new LinkedHashMap<>();
        ex.getConstraintViolations().forEach(v ->
                detalles.putIfAbsent(v.getPropertyPath().toString(), v.getMessage()));
        return error400("Parametros de entrada invalidos.", request, detalles);
    }

    /** Cuerpo JSON ilegible o enum no valido. */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<SobreError> manejarCuerpoIlegible(HttpMessageNotReadableException ex,
                                                            HttpServletRequest request) {
        return error400("Cuerpo de la peticion ilegible o con valores no validos.", request, null);
    }

    /** Parametro requerido ausente. */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<SobreError> manejarParametroFaltante(MissingServletRequestParameterException ex,
                                                               HttpServletRequest request) {
        return error400("Falta el parametro requerido: " + ex.getParameterName() + ".", request, null);
    }

    /** Tipo de parametro incompatible. */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<SobreError> manejarTipoParametro(MethodArgumentTypeMismatchException ex,
                                                           HttpServletRequest request) {
        return error400("Valor invalido para el parametro: " + ex.getName() + ".", request, null);
    }

    /** Cualquier otro error no controlado: ANT-500 sin filtrar el detalle interno. */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<SobreError> manejarInterno(Exception ex, HttpServletRequest request) {
        log.error("Error interno no controlado en {}", request.getRequestURI(), ex);
        SobreError sobre = SobreError.de(CodigoError.ANT_500.codigo(),
                "Error interno del servicio.", request.getRequestURI());
        return ResponseEntity.status(CodigoError.ANT_500.httpStatus()).body(sobre);
    }

    private ResponseEntity<SobreError> error400(String mensaje, HttpServletRequest request,
                                                Map<String, String> detalles) {
        SobreError sobre = SobreError.de(CodigoError.ANT_001.codigo(), mensaje, request.getRequestURI(),
                (detalles == null || detalles.isEmpty()) ? null : detalles);
        return ResponseEntity.status(CodigoError.ANT_001.httpStatus()).body(sobre);
    }
}
