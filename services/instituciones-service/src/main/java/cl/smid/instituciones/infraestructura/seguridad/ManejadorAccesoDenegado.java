package cl.smid.instituciones.infraestructura.seguridad;

import cl.smid.instituciones.api.error.SobreError;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Manejador de acceso denegado a nivel de Spring Security: responde {@code 403} con el
 * sobre de error unificado y el código de negocio {@code AUTZ-004}.
 */
@Component
public class ManejadorAccesoDenegado implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    public ManejadorAccesoDenegado(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        SobreError sobre = SobreError.de(
                HttpStatus.FORBIDDEN.value(),
                HttpStatus.FORBIDDEN.getReasonPhrase(),
                "AUTZ-004",
                "No cuenta con permisos suficientes para realizar esta operación.",
                request.getRequestURI());
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(sobre));
    }
}
