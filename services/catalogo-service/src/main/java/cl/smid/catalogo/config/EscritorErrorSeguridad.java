package cl.smid.catalogo.config;

import cl.smid.catalogo.api.error.ErrorResponse;
import cl.smid.catalogo.dominio.excepcion.CodigoError;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

/**
 * Escribe el {@link ErrorResponse} unificado (Núcleo 2.5) directamente sobre la respuesta HTTP.
 *
 * <p>Los errores de seguridad (401 y 403) se producen en filtros y manejadores que actúan
 * <b>antes</b> de los controladores y del {@code @RestControllerAdvice}; por eso necesitan un
 * componente propio que serialice el mismo sobre de error, garantizando que el cliente reciba
 * un formato homogéneo independientemente de dónde se origine el fallo.</p>
 */
@Component
public class EscritorErrorSeguridad {

    private final ObjectMapper objectMapper;

    public EscritorErrorSeguridad(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /** Serializa el sobre con el código indicado y fija el estado HTTP correspondiente. */
    public void escribir(HttpServletRequest solicitud, HttpServletResponse respuesta,
                         CodigoError codigo, String mensaje) throws IOException {
        ErrorResponse cuerpo = ErrorResponse.de(
                codigo, mensaje, solicitud.getRequestURI(), null, Instant.now());

        respuesta.setStatus(codigo.getHttpStatus());
        respuesta.setContentType(MediaType.APPLICATION_JSON_VALUE);
        respuesta.setCharacterEncoding(StandardCharsets.UTF_8.name());
        objectMapper.writeValue(respuesta.getWriter(), cuerpo);
    }
}
