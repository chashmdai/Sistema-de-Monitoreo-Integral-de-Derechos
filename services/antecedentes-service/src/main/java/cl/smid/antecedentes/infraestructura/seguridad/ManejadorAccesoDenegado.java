package cl.smid.antecedentes.infraestructura.seguridad;

import cl.smid.antecedentes.api.error.SobreError;
import cl.smid.antecedentes.dominio.excepcion.CodigoError;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Manejador de acceso denegado a nivel de Spring Security: responde {@code 403} con el sobre de
 * error unificado y codigo {@code AUTZ-004}. (Las denegaciones por rol que detecta el dominio se
 * traducen en el {@code @RestControllerAdvice}; este maneja las de la cadena de seguridad.)
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
        SobreError sobre = SobreError.de(CodigoError.AUTZ_004.codigo(),
                "No tiene permisos para realizar esta accion.", request.getRequestURI());
        response.setStatus(CodigoError.AUTZ_004.httpStatus());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        objectMapper.writeValue(response.getWriter(), sobre);
    }
}
