package cl.smid.instituciones.infraestructura.seguridad;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Set;

/**
 * Propiedades de autorización (prefijo {@code smid.seguridad}). Define el conjunto de
 * roles considerados administradores para las operaciones de escritura sobre datos de
 * referencia (override #6). Se realiza una copia defensiva inmutable.
 *
 * @param rolesAdmin roles que habilitan escritura (lectura es pública autenticada)
 */
@ConfigurationProperties(prefix = "smid.seguridad")
public record PropiedadesSeguridad(Set<String> rolesAdmin) {

    public PropiedadesSeguridad {
        rolesAdmin = rolesAdmin == null ? Set.of() : Set.copyOf(rolesAdmin);
    }
}
