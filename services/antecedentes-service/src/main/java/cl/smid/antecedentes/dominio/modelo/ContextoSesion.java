package cl.smid.antecedentes.dominio.modelo;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Contexto de sesion derivado del JWT validado en el borde. Es un VO inmutable del
 * dominio (sin dependencias de framework); lo construye el adaptador de seguridad a
 * partir de los claims (Nucleo 2.3 / 2.4) y lo consume el dominio para autorizar.
 *
 * @param usuarioAlt alt_key del usuario (claim {@code sub})
 * @param nombre     nombre legible del usuario (claim {@code nombre})
 * @param roles      roles del usuario (claim {@code roles})
 * @param idSede     alt_key de la sede del usuario (claim {@code idSede})
 * @param idUnidad   alt_key de la unidad del usuario (claim {@code idUnidad})
 * @param alcance    alcance territorial (claim {@code alcance})
 */
public record ContextoSesion(
        String usuarioAlt,
        String nombre,
        Set<String> roles,
        String idSede,
        String idUnidad,
        Alcance alcance) {

    public ContextoSesion {
        roles = (roles == null) ? Set.of() : Collections.unmodifiableSet(new LinkedHashSet<>(roles));
        alcance = (alcance == null) ? Alcance.UNIDAD : alcance;
    }

    /**
     * Indica si el usuario posee al menos uno de los roles requeridos.
     *
     * @param rolesRequeridos conjunto de roles habilitantes
     * @return {@code true} si hay interseccion no vacia
     */
    public boolean tieneAlgunRol(Set<String> rolesRequeridos) {
        if (rolesRequeridos == null || rolesRequeridos.isEmpty()) {
            return false;
        }
        for (String rol : roles) {
            if (rolesRequeridos.contains(rol)) {
                return true;
            }
        }
        return false;
    }
}
