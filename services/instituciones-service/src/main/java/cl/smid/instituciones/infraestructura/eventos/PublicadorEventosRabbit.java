package cl.smid.instituciones.infraestructura.eventos;

import cl.smid.instituciones.config.ConfiguracionRabbitComun;
import cl.smid.instituciones.dominio.modelo.EventoDominio;
import cl.smid.instituciones.dominio.puerto.salida.PublicadorEventos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Publicador de eventos por <strong>RabbitMQ</strong> (override #8). Envía un sobre
 * metadata-only al exchange {@link ConfiguracionRabbitComun#EXCHANGE_EVENTOS} usando el
 * tipo del evento como <em>routing key</em>. Tolerante a fallos: un problema de
 * mensajería se registra como advertencia y no rompe la transacción de negocio.
 *
 * <p>Se activa solo cuando {@code smid.eventos.transporte=rabbitmq}.</p>
 */
@Component
@ConditionalOnProperty(prefix = "smid.eventos", name = "transporte", havingValue = "rabbitmq")
public class PublicadorEventosRabbit implements PublicadorEventos {

    private static final Logger LOG = LoggerFactory.getLogger("AUDIT.SMID.INSTITUCIONES");

    private final RabbitTemplate rabbitTemplate;

    public PublicadorEventosRabbit(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void publicar(EventoDominio evento) {
        Map<String, Object> sobre = new LinkedHashMap<>();
        sobre.put("tipo", evento.tipo());
        sobre.put("altKey", evento.altKey());
        sobre.put("metadatos", evento.metadatos());
        sobre.put("ocurridoEn", evento.ocurridoEn() == null ? null : evento.ocurridoEn().toString());
        try {
            rabbitTemplate.convertAndSend(ConfiguracionRabbitComun.EXCHANGE_EVENTOS, evento.tipo(), sobre);
        } catch (Exception ex) {
            LOG.warn("Falló la publicación del evento {} ({}) en RabbitMQ: {}",
                    evento.tipo(), evento.altKey(), ex.getMessage());
        }
    }
}
