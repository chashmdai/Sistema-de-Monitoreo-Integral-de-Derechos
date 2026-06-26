package cl.smid.requerimientos.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.LocalDate;

/**
 * Propiedades de la política de folios (prefijo {@code smid.folio}).
 *
 * @param inicioOficial fecha desde la que los folios nuevos usan la serie oficial
 */
@ConfigurationProperties(prefix = "smid.folio")
public record PropiedadesFolio(LocalDate inicioOficial) {

    public PropiedadesFolio {
        inicioOficial = (inicioOficial == null) ? LocalDate.of(2027, 1, 1) : inicioOficial;
    }
}
