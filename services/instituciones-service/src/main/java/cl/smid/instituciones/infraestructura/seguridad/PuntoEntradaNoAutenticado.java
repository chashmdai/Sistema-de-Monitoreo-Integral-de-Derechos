package cl.smid.instituciones.infraestructura.seguridad;

import cl.smid.instituciones.api.error.SobreError;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Punto de entrada para peticiones no autenticadas: responde {@code 401} con el sobre de
 * error unificado y el código de negocio {@code AUTZ-003}.
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
        SobreError sobre = SobreError.de(
                HttpStatus.UNAUTHORIZED.value(),
                HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                "AUTZ-003",
                "Se requiere autenticación para acceder a este recurso.",
                request.getRequestURI());
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(sobre));
    }
}
