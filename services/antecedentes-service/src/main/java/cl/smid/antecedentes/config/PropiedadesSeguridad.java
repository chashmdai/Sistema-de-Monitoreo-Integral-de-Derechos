package cl.smid.antecedentes.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Set;

/**
 * Roles habilitados para acciones de revision y para escritura de tablas de referencia (admin).
 * Se traducen a una {@link cl.smid.antecedentes.dominio.modelo.PoliticaRoles} en el cableado.
 *
 * @param rolesRevision roles que pueden devolver/aprobar/rechazar fichas y gestionar hallazgos
 * @param rolesAdmin    roles que pueden crear/editar/activar tablas de referencia
 */
@ConfigurationProperties(prefix = "smid.seguridad")
public record PropiedadesSeguridad(List<String> rolesRevision, List<String> rolesAdmin) {

    public PropiedadesSeguridad {
        rolesRevision = (rolesRevision == null) ? List.of() : List.copyOf(rolesRevision);
        rolesAdmin = (rolesAdmin == null) ? List.of() : List.copyOf(rolesAdmin);
    }

    public Set<String> rolesRevisionSet() {
        return Set.copyOf(rolesRevision);
    }

    public Set<String> rolesAdminSet() {
        return Set.copyOf(rolesAdmin);
    }
}
