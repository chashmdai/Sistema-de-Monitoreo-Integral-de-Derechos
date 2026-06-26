package cl.smid.catalogo.config;

import cl.smid.catalogo.dominio.excepcion.CodigoError;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Manejador de acceso denegado de Spring Security: se invoca cuando un usuario <b>autenticado</b>
 * intenta una operación para la que no tiene permisos (p. ej. escribir sin rol de Administración).
 * Responde con el sobre 403 ({@code AUTZ-004}).
 */
@Component
public class ManejadorAccesoDenegado implements AccessDeniedHandler {

    private final EscritorErrorSeguridad escritorError;

    public ManejadorAccesoDenegado(EscritorErrorSeguridad escritorError) {
        this.escritorError = escritorError;
    }

    @Override
    public void handle(HttpServletRequest solicitud, HttpServletResponse respuesta,
                       AccessDeniedException excepcion) throws IOException {
        escritorError.escribir(solicitud, respuesta,
                CodigoError.ACCESO_DENEGADO, CodigoError.ACCESO_DENEGADO.getMensajePorDefecto());
    }
}
