package cl.smid.requerimientos.infraestructura.seguridad;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Componente de autorización por rol, invocable desde {@code @PreAuthorize}. Centraliza la
 * facultad de Coordinación exigida para decidir la admisibilidad (USR.02). El conjunto de roles
 * que conceden Coordinación es configurable ({@code smid.seguridad.roles-coordinacion}).
 *
 * <p>Uso en el controlador:
 * {@code @PreAuthorize("@autorizacionRoles.tieneRolCoordinacion(authentication)")}. Si la
 * autoridad falta, Spring lanza {@code AccessDeniedException} y el
 * {@link ManejadorAccesoDenegado} emite {@code AUTZ-004} (403).</p>
 */
@Component("autorizacionRoles")
public class AutorizacionRoles {

    private final Set<String> rolesCoordinacion;

    public AutorizacionRoles(PropiedadesSeguridad propiedades) {
        this.rolesCoordinacion = propiedades.rolesCoordinacion().stream()
                .map(String::toUpperCase)
                .collect(Collectors.toUnmodifiableSet());
    }

    /**
     * Indica si el usuario autenticado posee la facultad de Coordinación.
     *
     * @param authentication autenticación en curso
     * @return {@code true} si alguno de sus roles está en el conjunto de Coordinación
     */
    public boolean tieneRolCoordinacion(Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof UsuarioAutenticado usuario)) {
            return false;
        }
        return usuario.roles().stream()
                .map(String::toUpperCase)
                .anyMatch(rolesCoordinacion::contains);
    }
}
