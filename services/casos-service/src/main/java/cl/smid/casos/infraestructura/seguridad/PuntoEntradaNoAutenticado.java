package cl.smid.casos.infraestructura.seguridad;

import cl.smid.casos.dominio.excepcion.CodigoError;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Punto de entrada para solicitudes no autenticadas: escribe el sobre de error unificado con código
 * {@code AUTZ-003} (401). Se dispara cuando falta el token o la autenticación no se estableció.
 *
 * <p>El sobre respeta el contrato del Núcleo (2.5): {@code status, error, codigo, mensaje, ruta,
 * timestamp}, usando {@code ruta} (no "path") y {@code timestamp} en UTC ISO-8601.</p>
 */
@Component
public class PuntoEntradaNoAutenticado implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    public PuntoEntradaNoAutenticado(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        CodigoError codigo = CodigoError.AUTZ_003;
        response.setStatus(codigo.httpStatus());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        Map<String, Object> sobre = new LinkedHashMap<>();
        sobre.put("status", codigo.httpStatus());
        sobre.put("error", codigo.error());
        sobre.put("codigo", codigo.codigo());
        sobre.put("mensaje", "Autenticación requerida: falta un token válido.");
        sobre.put("ruta", request.getRequestURI());
        sobre.put("timestamp", Instant.now().toString());

        objectMapper.writeValue(response.getOutputStream(), sobre);
    }
}
