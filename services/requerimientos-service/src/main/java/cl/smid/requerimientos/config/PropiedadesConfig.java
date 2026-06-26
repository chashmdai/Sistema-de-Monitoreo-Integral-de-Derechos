package cl.smid.requerimientos.config;

import cl.smid.requerimientos.infraestructura.cliente.PropiedadesIntegracion;
import cl.smid.requerimientos.infraestructura.eventos.PropiedadesEventos;
import cl.smid.requerimientos.infraestructura.seguridad.PropiedadesJwt;
import cl.smid.requerimientos.infraestructura.seguridad.PropiedadesSeguridad;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Habilita el enlace de las propiedades basadas en {@code record}. Se registra de forma explícita
 * (en lugar de {@code @ConfigurationPropertiesScan}) para no chocar con
 * {@code DirectorioSedesConfig}, que es a la vez {@code @Component} y {@code @ConfigurationProperties}
 * y se enlaza por sí mismo.
 */
@Configuration
@EnableConfigurationProperties({
        PropiedadesJwt.class,
        PropiedadesSeguridad.class,
        PropiedadesEventos.class,
        PropiedadesIntegracion.class,
        PropiedadesFolio.class
})
public class PropiedadesConfig {
}
