package cl.smid.requerimientos.infraestructura.seguridad;

import cl.smid.requerimientos.dominio.modelo.Alcance;

import java.util.Set;

/**
 * Principal autenticado, proyección de los claims del JWT (Núcleo 2.3). Se almacena como
 * {@code principal} en el {@code SecurityContext} y alimenta el {@code ContextoUsuario} del caso
 * de uso. Todas las referencias territoriales son alt_key (UUID), nunca llaves internas.
 *
 * @param usuarioAlt alt_key del usuario (claim {@code sub})
 * @param idSedeAlt  alt_key de su sede (claim {@code idSede})
 * @param idUnidadAlt alt_key de su unidad (claim {@code idUnidad})
 * @param alcance    alcance territorial (claim {@code alcance})
 * @param roles      códigos de rol (claim {@code roles})
 * @param nombre     nombre legible (claim {@code nombre})
 */
public record UsuarioAutenticado(
        String usuarioAlt,
        String idSedeAlt,
        String idUnidadAlt,
        Alcance alcance,
        Set<String> roles,
        String nombre) {

    public UsuarioAutenticado {
        roles = (roles == null) ? Set.of() : Set.copyOf(roles);
    }
}
