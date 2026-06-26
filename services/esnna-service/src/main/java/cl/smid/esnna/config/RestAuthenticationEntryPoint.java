package cl.smid.esnna.config;

import cl.smid.esnna.exception.ApiError;
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
 * ERR-1: en Spring Security 6 los fallos de autenticación los resuelve el filtro
 * antes del DispatcherServlet, así que el @RestControllerAdvice nunca los ve.
 * Este entry point garantiza que un 401 devuelva el mismo contrato ApiError que
 * el resto de los errores, en vez del body vacío por defecto.
 */
@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    public RestAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException ex) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        ApiError body = ApiError.of(HttpStatus.UNAUTHORIZED.value(),
                "NO_AUTENTICADO", "Token inválido o ausente.", request.getRequestURI());
        objectMapper.writeValue(response.getWriter(), body);
    }
}
