package cl.smid.instituciones.dominio.modelo;

import java.util.Collection;
import java.util.Set;

/**
 * Contexto de la sesión corporativa extraído del JWT, expresado en términos del
 * dominio. <strong>No</strong> transporta el token bruto (la propagación de
 * credenciales no aplica a un servicio hoja).
 *
 * <p>Los identificadores territoriales ({@code idSede}, {@code idUnidad}) y el
 * {@code alcance} se conservan por coherencia del contrato del ecosistema, pero
 * en este servicio <strong>no se usan para filtrar lecturas</strong> (override #6:
 * las instituciones son datos de referencia nacionales). Lo único que el dominio
 * consulta aquí son los {@code roles}, para autorizar escrituras.</p>
 *
 * @param altKeyUsuario alt_key del usuario autenticado (claim {@code sub})
 * @param roles         roles del usuario (claim {@code roles})
 * @param idSede        alt_key de la sede (claim {@code idSede}); informativo
 * @param idUnidad      alt_key de la unidad (claim {@code idUnidad}); informativo
 * @param alcance       alcance territorial (UNIDAD|SEDE|NACIONAL); informativo
 * @param nombre        nombre legible del usuario (claim {@code nombre})
 */
public record ContextoSesion(
        String altKeyUsuario,
        Set<String> roles,
        String idSede,
        String idUnidad,
        String alcance,
        String nombre) {

    public ContextoSesion {
        roles = roles == null ? Set.of() : Set.copyOf(roles);
    }

    /**
     * Indica si la sesión posee al menos uno de los roles indicados.
     *
     * @param rolesRequeridos conjunto de roles que habilitan una operación
     * @return {@code true} si hay intersección no vacía con los roles de la sesión
     */
    public boolean poseeAlgunRol(Collection<String> rolesRequeridos) {
        if (rolesRequeridos == null || rolesRequeridos.isEmpty()) {
            return false;
        }
        for (String requerido : rolesRequeridos) {
            if (roles.contains(requerido)) {
                return true;
            }
        }
        return false;
    }
}
