package cl.smid.casos.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.util.Map;

/**
 * Resolución de códigos de sede a partir del {@code alt_key}: el token no transporta el código corto.
 *
 * @param codigos       mapa {@code alt_key de sede → código corto} (p. ej. {@code RM}).
 * @param codigoDefecto código de respaldo cuando no hay mapeo para una sede.
 */
@ConfigurationProperties(prefix = "smid.sedes")
public record PropiedadesSedes(@DefaultValue Map<String, String> codigos,
                               @DefaultValue("SMID") String codigoDefecto) {
}
