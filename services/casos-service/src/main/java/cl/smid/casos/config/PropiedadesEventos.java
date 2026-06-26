package cl.smid.casos.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

/**
 * Propiedades de mensajería del servicio.
 *
 * @param transporte transporte de publicación de eventos de dominio: {@code log} o {@code rabbitmq}.
 * @param consumo    modo de consumo de eventos entrantes: {@code rabbitmq} (por defecto) u otro valor
 *                   para desactivar el listener (despliegues sin broker).
 */
@ConfigurationProperties(prefix = "smid.eventos")
public record PropiedadesEventos(@DefaultValue("log") String transporte,
                                 @DefaultValue("rabbitmq") String consumo) {
}
