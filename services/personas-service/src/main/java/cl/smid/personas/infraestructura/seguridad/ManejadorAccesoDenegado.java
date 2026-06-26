package cl.smid.personas.infraestructura.seguridad;

import cl.smid.personas.dominio.excepcion.CodigoError;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Manejador de acceso denegado: se invoca cuando un usuario <b>autenticado</b> intenta una
 * operación para la que no tiene permiso por rol. Devuelve el sobre unificado con código
 * {@code AUTZ-004} y HTTP 403 (Núcleo 2.4/2.5).
 *
 * <p><b>Nota de diseño.</b> En esta entrega ningún endpoint exige un rol específico, y la
 * denegación territorial de Personas se expresa como 404 (no 403) para no revelar la existencia
 * de registros fuera de alcance. Por lo tanto este manejador queda <i>cableado pero no
 * alcanzable</i> con los endpoints actuales; se incluye para mantener el contrato de seguridad
 * idéntico al del ecosistema y para soportar, sin cambios, una futura autorización por rol.</p>
 */
@Component
public class ManejadorAccesoDenegado implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    public ManejadorAccesoDenegado(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        EscritorSobreError.escribir(
                objectMapper,
                response,
                request.getRequestURI(),
                CodigoError.ACCESO_DENEGADO,
                "No tiene permisos para realizar esta operación.");
    }
}
