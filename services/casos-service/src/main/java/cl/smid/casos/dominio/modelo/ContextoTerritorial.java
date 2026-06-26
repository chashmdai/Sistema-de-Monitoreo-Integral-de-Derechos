package cl.smid.casos.dominio.modelo;

import java.util.Objects;
import java.util.Set;

/**
 * Contexto territorial e identitario del usuario autenticado, derivado de los claims del JWT
 * (Núcleo 2.3 / 2.4). Es un objeto de valor PURO del dominio: el adaptador de seguridad lo construye
 * a partir del token y lo entrega a los controladores, que lo propagan al servicio de dominio.
 *
 * <p>El dominio NO conoce tipos de Spring Security; razona únicamente sobre este objeto.</p>
 *
 * @param sujetoAlt alt_key del usuario ({@code sub}).
 * @param roles     roles del usuario ({@code roles[]}).
 * @param idSedeAlt alt_key de la sede del usuario ({@code idSede}).
 * @param idUnidadAlt alt_key de la unidad del usuario ({@code idUnidad}).
 * @param alcance   alcance territorial ({@code alcance}).
 * @param nombre    nombre legible del usuario ({@code nombre}).
 */
public record ContextoTerritorial(String sujetoAlt, Set<String> roles, String idSedeAlt,
                                  String idUnidadAlt, Alcance alcance, String nombre) {

    public ContextoTerritorial {
        Objects.requireNonNull(alcance, "alcance");
        roles = roles == null ? Set.of() : Set.copyOf(roles);
    }

    /** Indica si el usuario posee alguno de los roles indicados (comparación exacta por código). */
    public boolean tieneAlgunRol(Set<String> rolesRequeridos) {
        if (rolesRequeridos == null || rolesRequeridos.isEmpty()) {
            return false;
        }
        for (String r : roles) {
            if (rolesRequeridos.contains(r)) {
                return true;
            }
        }
        return false;
    }
}
