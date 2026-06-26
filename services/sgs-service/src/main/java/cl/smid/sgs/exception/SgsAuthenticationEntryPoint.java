package cl.smid.sgs.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

/** Serializa 401 como ApiError (ERR-1: el filtro de seguridad intercepta antes que el advice). */
@Component
public class SgsAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper om;
    public SgsAuthenticationEntryPoint(ObjectMapper om) { this.om = om; }

    @Override
    public void commence(HttpServletRequest req, HttpServletResponse res, AuthenticationException ex) throws IOException {
        res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        res.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ApiError body = ApiError.of(401, "Unauthorized", "Token ausente o inválido.",
                req.getRequestURI(), UUID.randomUUID().toString().substring(0, 8));
        om.writeValue(res.getOutputStream(), body);
    }
}
