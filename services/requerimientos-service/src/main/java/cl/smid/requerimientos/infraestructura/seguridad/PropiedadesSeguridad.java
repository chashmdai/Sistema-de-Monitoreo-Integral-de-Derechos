package cl.smid.requerimientos.infraestructura.seguridad;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * Propiedades de autorización por rol (prefijo {@code smid.seguridad}).
 *
 * @param rolesCoordinacion códigos de rol que conceden la facultad de Coordinación (decidir la
 *                          admisibilidad). La comparación es insensible a mayúsculas.
 */
@ConfigurationProperties(prefix = "smid.seguridad")
public record PropiedadesSeguridad(List<String> rolesCoordinacion) {

    public PropiedadesSeguridad {
        rolesCoordinacion = (rolesCoordinacion == null || rolesCoordinacion.isEmpty())
                ? List.of("COORDINADOR", "COORDINACION", "ADMIN_SEDE", "ADMIN_NACIONAL")
                : List.copyOf(rolesCoordinacion);
    }
}
