package cl.smid.sgs.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

/** Serializa 403 como ApiError (ERR-1). */
@Component
public class SgsAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper om;
    public SgsAccessDeniedHandler(ObjectMapper om) { this.om = om; }

    @Override
    public void handle(HttpServletRequest req, HttpServletResponse res, AccessDeniedException ex) throws IOException {
        res.setStatus(HttpServletResponse.SC_FORBIDDEN);
        res.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ApiError body = ApiError.of(403, "Forbidden", "No tiene permisos para esta operación.",
                req.getRequestURI(), UUID.randomUUID().toString().substring(0, 8));
        om.writeValue(res.getOutputStream(), body);
    }
}
