package cl.smid.antecedentes.infraestructura.seguridad;

import cl.smid.antecedentes.api.error.SobreError;
import cl.smid.antecedentes.dominio.excepcion.CodigoError;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Punto de entrada para peticiones no autenticadas: responde {@code 401} con el sobre de error
 * unificado y codigo {@code AUTZ-003}.
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
        SobreError sobre = SobreError.de(CodigoError.AUTZ_003.codigo(),
                "Autenticacion requerida.", request.getRequestURI());
        response.setStatus(CodigoError.AUTZ_003.httpStatus());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        objectMapper.writeValue(response.getWriter(), sobre);
    }
}
