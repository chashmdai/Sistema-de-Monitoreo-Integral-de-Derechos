package cl.smid.requerimientos.infraestructura.seguridad;

import cl.smid.requerimientos.dominio.excepcion.CodigoError;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Escribe el sobre de error unificado del ecosistema (Núcleo 2.5) directamente en la respuesta
 * HTTP. Lo usan los manejadores de seguridad (401/403), que actúan fuera del flujo del
 * {@code @RestControllerAdvice}. El campo de ruta se llama {@code ruta} (no {@code path}); el
 * mapa {@code detalles} se omite aquí (solo aplica a validación).
 */
@Component
public class EscritorRespuestaError {

    private final ObjectMapper objectMapper;

    public EscritorRespuestaError(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Serializa y envía el sobre de error con el estado HTTP del código indicado.
     *
     * @param request  petición en curso (para la ruta)
     * @param response respuesta a escribir
     * @param codigo   código de error del catálogo
     * @throws IOException si falla la escritura del cuerpo
     */
    public void escribir(HttpServletRequest request, HttpServletResponse response, CodigoError codigo)
            throws IOException {
        HttpStatus estado = HttpStatus.valueOf(codigo.httpStatus());

        Map<String, Object> sobre = new LinkedHashMap<>();
        sobre.put("status", estado.value());
        sobre.put("error", estado.getReasonPhrase());
        sobre.put("codigo", codigo.codigo());
        sobre.put("mensaje", codigo.mensajePorDefecto());
        sobre.put("ruta", request.getRequestURI());
        sobre.put("timestamp", Instant.now().toString());

        response.setStatus(estado.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.getWriter().write(objectMapper.writeValueAsString(sobre));
    }
}
