package cl.smid.personas.infraestructura.seguridad;

import cl.smid.personas.dominio.excepcion.CodigoError;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Utilidad para emitir el sobre de error unificado (Núcleo 2.5) directamente sobre la respuesta
 * HTTP desde los componentes de la cadena de seguridad (que actúan antes del
 * {@code DispatcherServlet}, por lo que no pueden delegar en el {@code RestControllerAdvice}).
 *
 * <p>Construye el JSON con un {@link LinkedHashMap} de orden estable, evitando que la capa de
 * seguridad dependa del record {@code ErrorResponse} de la capa {@code api}; la forma resultante
 * es idéntica: {@code status, error, codigo, mensaje, ruta, timestamp}. Los handlers de
 * seguridad (401/403) no adjuntan {@code detalles}.</p>
 */
final class EscritorSobreError {

    private EscritorSobreError() {
        // Clase de utilidades: no instanciable.
    }

    /**
     * Serializa y escribe el sobre de error con el estado HTTP del código indicado.
     *
     * @param objectMapper serializador JSON de Spring (reutilizado)
     * @param response     respuesta HTTP a poblar
     * @param ruta         URI de la petición que originó el error
     * @param codigo       código de negocio (aporta estado HTTP, frase y código)
     * @param mensaje      mensaje legible para el cliente
     */
    static void escribir(ObjectMapper objectMapper,
                         HttpServletResponse response,
                         String ruta,
                         CodigoError codigo,
                         String mensaje) throws IOException {
        response.setStatus(codigo.httpStatus());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        Map<String, Object> cuerpo = new LinkedHashMap<>();
        cuerpo.put("status", codigo.httpStatus());
        cuerpo.put("error", codigo.fraseHttp());
        cuerpo.put("codigo", codigo.codigo());
        cuerpo.put("mensaje", mensaje);
        cuerpo.put("ruta", ruta);
        cuerpo.put("timestamp", Instant.now().toString());

        objectMapper.writeValue(response.getOutputStream(), cuerpo);
    }
}
