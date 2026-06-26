package cl.smid.instituciones.api.error;

import cl.smid.instituciones.dominio.excepcion.CodigoError;
import cl.smid.instituciones.dominio.excepcion.ExcepcionDominio;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.ArrayList;
import java.util.List;

/**
 * Traduce las excepciones a respuestas HTTP con el {@link SobreError} unificado.
 *
 * <p>Las excepciones de dominio llevan su propio {@link CodigoError} (estado, título y
 * código estable). Los errores de validación y de formato de la petición se mapean a
 * {@code INS-001 / 400}; las violaciones de integridad a {@code INS-409 / 409}; y
 * cualquier error inesperado a {@code INS-500 / 500} sin filtrar el detalle interno.</p>
 */
@RestControllerAdvice
public class ManejadorGlobalExcepciones {

    private static final Logger LOG = LoggerFactory.getLogger(ManejadorGlobalExcepciones.class);

    @ExceptionHandler(ExcepcionDominio.class)
    public ResponseEntity<SobreError> manejarDominio(ExcepcionDominio ex, HttpServletRequest peticion) {
        CodigoError codigo = ex.codigo();
        SobreError sobre = SobreError.de(
                codigo.httpStatus(), codigo.titulo(), codigo.codigo(), ex.getMessage(), peticion.getRequestURI());
        return ResponseEntity.status(codigo.httpStatus()).body(sobre);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<SobreError> manejarValidacion(MethodArgumentNotValidException ex,
                                                        HttpServletRequest peticion) {
        List<String> detalles = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            detalles.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            detalles.add(error.getDefaultMessage());
        }
        CodigoError codigo = CodigoError.VALIDACION;
        SobreError sobre = SobreError.conDetalles(
                codigo.httpStatus(), codigo.titulo(), codigo.codigo(),
                "La solicitud contiene errores de validación.", detalles, peticion.getRequestURI());
        return ResponseEntity.status(codigo.httpStatus()).body(sobre);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<SobreError> manejarCuerpoIlegible(HttpMessageNotReadableException ex,
                                                           HttpServletRequest peticion) {
        return respuestaValidacion("El cuerpo de la petición es ilegible o está ausente.", peticion);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<SobreError> manejarParametroFaltante(MissingServletRequestParameterException ex,
                                                              HttpServletRequest peticion) {
        return respuestaValidacion("Falta el parámetro obligatorio '" + ex.getParameterName() + "'.", peticion);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<SobreError> manejarTipoInvalido(MethodArgumentTypeMismatchException ex,
                                                         HttpServletRequest peticion) {
        return respuestaValidacion("El parámetro '" + ex.getName() + "' tiene un valor inválido.", peticion);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<SobreError> manejarIntegridad(DataIntegrityViolationException ex,
                                                       HttpServletRequest peticion) {
        LOG.warn("Violación de integridad de datos en {}: {}", peticion.getRequestURI(), ex.getMostSpecificCause().getMessage());
        CodigoError codigo = CodigoError.CONFLICTO;
        SobreError sobre = SobreError.de(
                codigo.httpStatus(), codigo.titulo(), codigo.codigo(),
                "La operación viola una restricción de integridad de datos.", peticion.getRequestURI());
        return ResponseEntity.status(codigo.httpStatus()).body(sobre);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<SobreError> manejarInesperado(Exception ex, HttpServletRequest peticion) {
        LOG.error("Error interno no controlado en {}", peticion.getRequestURI(), ex);
        CodigoError codigo = CodigoError.ERROR_INTERNO;
        SobreError sobre = SobreError.de(
                codigo.httpStatus(), codigo.titulo(), codigo.codigo(),
                "Ocurrió un error interno al procesar la solicitud.", peticion.getRequestURI());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(sobre);
    }

    private ResponseEntity<SobreError> respuestaValidacion(String mensaje, HttpServletRequest peticion) {
        CodigoError codigo = CodigoError.VALIDACION;
        SobreError sobre = SobreError.de(
                codigo.httpStatus(), codigo.titulo(), codigo.codigo(), mensaje, peticion.getRequestURI());
        return ResponseEntity.status(codigo.httpStatus()).body(sobre);
    }
}
