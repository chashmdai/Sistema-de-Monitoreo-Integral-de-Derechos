package cl.smid.instituciones.infraestructura.seguridad;

import cl.smid.instituciones.dominio.modelo.ContextoSesion;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * Implementación de {@link ProveedorContexto} respaldada por los atributos de la
 * petición (no por {@code @RequestScope}), para evitar problemas de temporización
 * cuando el filtro de seguridad escribe el contexto antes de que el DispatcherServlet
 * enlace el ámbito de petición.
 */
@Component
public class ProveedorContextoRequest implements ProveedorContexto {

    @Override
    public ContextoSesion contextoActual() {
        RequestAttributes atributos = RequestContextHolder.getRequestAttributes();
        if (atributos == null) {
            return null;
        }
        Object valor = atributos.getAttribute(
                FiltroAutenticacionJwt.ATRIBUTO_CONTEXTO, RequestAttributes.SCOPE_REQUEST);
        return valor instanceof ContextoSesion contexto ? contexto : null;
    }
}
