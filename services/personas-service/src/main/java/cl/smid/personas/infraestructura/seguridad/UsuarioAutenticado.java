package cl.smid.personas.infraestructura.seguridad;

import cl.smid.personas.dominio.modelo.Alcance;
import cl.smid.personas.dominio.modelo.ContextoTerritorial;

import java.util.List;

/**
 * Principal de seguridad con los datos relevantes del token ya validado (Núcleo 2.4). Se almacena
 * en el {@code Authentication} y el controlador lo proyecta a {@link ContextoTerritorial} para
 * entregárselo al dominio (que no conoce Spring Security).
 *
 * @param actor    identidad del usuario (claim {@code sub}, alt_key UUID)
 * @param nombre   nombre legible del usuario (claim {@code nombre})
 * @param alcance  alcance territorial (claim {@code alcance})
 * @param idSede   alt_key de la sede del usuario (claim {@code idSede})
 * @param idUnidad alt_key de la unidad del usuario (claim {@code idUnidad})
 * @param roles    roles del usuario (claim {@code roles})
 */
public record UsuarioAutenticado(
        String actor,
        String nombre,
        Alcance alcance,
        String idSede,
        String idUnidad,
        List<String> roles
) {

    public UsuarioAutenticado {
        roles = (roles == null) ? List.of() : List.copyOf(roles);
    }

    /** Proyecta el principal al contexto territorial que consume el dominio. */
    public ContextoTerritorial aContextoTerritorial() {
        return new ContextoTerritorial(actor, alcance, idSede, idUnidad);
    }
}
