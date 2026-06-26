package cl.smid.casos.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.util.List;

/**
 * Propiedades de autorización del dominio. Los roles de Coordinación habilitan las acciones
 * administrativas del expediente (cerrar, reabrir, archivar). La configuración JWT vive aparte en
 * {@code PropiedadesJwt} (prefijo {@code smid.seguridad.jwt}).
 *
 * @param rolesCoordinacion códigos de rol considerados de Coordinación.
 */
@ConfigurationProperties(prefix = "smid.seguridad")
public record PropiedadesSeguridad(
        @DefaultValue({"COORDINADOR", "COORDINACION", "ADMIN_SEDE", "ADMIN_NACIONAL"})
        List<String> rolesCoordinacion) {
}
