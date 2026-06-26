package cl.smid.casos.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.time.LocalDate;

/**
 * Política de numeración del expediente. Cuando el evento no declara la bandera {@code esBeta}, la
 * serie se decide por fecha: antes de {@code inicioOficial} se asigna BETA (marcha blanca); desde esa
 * fecha, OFICIAL.
 *
 * @param inicioOficial fecha (UTC) a partir de la cual la numeración es OFICIAL.
 */
@ConfigurationProperties(prefix = "smid.expediente")
public record PropiedadesExpediente(@DefaultValue("2027-01-01") LocalDate inicioOficial) {
}
