package cl.smid.antecedentes.dominio.modelo;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Politica de roles del servicio: conjunto de roles habilitados para acciones de revision y
 * para escritura de tablas de referencia (admin). Se inyecta al dominio desde la configuracion
 * ({@code smid.seguridad.roles-revision} / {@code smid.seguridad.roles-admin}).
 */
public final class PoliticaRoles {

    private final Set<String> rolesRevision;
    private final Set<String> rolesAdmin;

    public PoliticaRoles(Set<String> rolesRevision, Set<String> rolesAdmin) {
        this.rolesRevision = inmutable(rolesRevision);
        this.rolesAdmin = inmutable(rolesAdmin);
    }

    private static Set<String> inmutable(Set<String> roles) {
        return (roles == null) ? Set.of() : Collections.unmodifiableSet(new LinkedHashSet<>(roles));
    }

    public Set<String> rolesRevision() {
        return rolesRevision;
    }

    public Set<String> rolesAdmin() {
        return rolesAdmin;
    }
}
