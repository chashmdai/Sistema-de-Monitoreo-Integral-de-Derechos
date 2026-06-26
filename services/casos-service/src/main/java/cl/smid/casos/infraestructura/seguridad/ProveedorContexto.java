package cl.smid.casos.infraestructura.seguridad;

import cl.smid.casos.dominio.modelo.ContextoTerritorial;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Expone el {@link ContextoTerritorial} del usuario autenticado a partir del contexto de seguridad,
 * para que los controladores no toquen directamente las APIs de Spring Security.
 */
@Component
public class ProveedorContexto {

    /**
     * Devuelve el contexto del usuario autenticado.
     *
     * @throws TokenInvalidoException si no hay un contexto autenticado disponible (no debería ocurrir
     *                                en endpoints protegidos, pero se protege por robustez).
     */
    public ContextoTerritorial actual() {
        Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
        if (autenticacion != null && autenticacion.getPrincipal() instanceof ContextoTerritorial ctx) {
            return ctx;
        }
        throw new TokenInvalidoException("No hay un contexto de usuario autenticado en la solicitud.");
    }
}
