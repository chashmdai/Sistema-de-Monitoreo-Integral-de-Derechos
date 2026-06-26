package cl.smid.requerimientos.infraestructura.eventos;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Propiedades de los eventos de dominio (prefijo {@code smid.eventos}).
 *
 * @param transporte transporte activo: {@code log} (por defecto) o {@code rabbitmq}
 * @param exchange   nombre del topic exchange de destino ({@code smid.eventos})
 */
@ConfigurationProperties(prefix = "smid.eventos")
public record PropiedadesEventos(String transporte, String exchange) {

    public PropiedadesEventos {
        transporte = (transporte == null || transporte.isBlank()) ? "log" : transporte;
        exchange = (exchange == null || exchange.isBlank()) ? "smid.eventos" : exchange;
    }
}
