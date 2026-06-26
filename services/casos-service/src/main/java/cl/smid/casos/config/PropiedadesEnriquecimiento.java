package cl.smid.casos.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

/**
 * Configuración del enriquecimiento on-demand contra requerimientos-service (6.3).
 *
 * @param activo           si está activo el cruce REST; por defecto {@code false} (caso esqueleto).
 * @param requerimientosUrl URL base de requerimientos-service (p. ej. {@code http://requerimientos:8089}).
 */
@ConfigurationProperties(prefix = "smid.enriquecimiento")
public record PropiedadesEnriquecimiento(@DefaultValue("false") boolean activo,
                                         @DefaultValue("") String requerimientosUrl) {
}
