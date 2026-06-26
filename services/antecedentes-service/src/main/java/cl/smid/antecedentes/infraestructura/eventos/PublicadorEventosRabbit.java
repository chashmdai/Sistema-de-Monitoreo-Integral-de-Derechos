package cl.smid.antecedentes.infraestructura.eventos;

import cl.smid.antecedentes.dominio.modelo.EventoDominio;
import cl.smid.antecedentes.dominio.puerto.salida.PublicadorEventos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Transporte de eventos por RabbitMQ ({@code smid.eventos.transporte=rabbitmq}). Publica al
 * exchange de topicos {@value #EXCHANGE} con routing key = tipo del evento. El sobre es
 * metadata-only (nunca relatos, nombres ni RUT de personas). Un fallo de publicacion se registra
 * y se ignora: jamas deshace la operacion de negocio (override #7).
 */
@Component
@ConditionalOnProperty(prefix = "smid.eventos", name = "transporte", havingValue = "rabbitmq")
public class PublicadorEventosRabbit implements PublicadorEventos {

    /** Exchange de topicos compartido del cluster. */
    public static final String EXCHANGE = "smid.eventos";

    private static final Logger log = LoggerFactory.getLogger(PublicadorEventosRabbit.class);

    private final RabbitTemplate rabbitTemplate;

    public PublicadorEventosRabbit(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void publicar(EventoDominio evento) {
        try {
            Map<String, Object> sobre = new LinkedHashMap<>();
            sobre.put("tipo", evento.tipo());
            sobre.put("altKey", evento.altKey());
            sobre.put("ocurridoEn", DateTimeFormatter.ISO_INSTANT.format(evento.ocurridoEn()));
            sobre.put("metadatos", evento.metadatos());
            rabbitTemplate.convertAndSend(EXCHANGE, evento.tipo(), sobre);
        } catch (Exception e) {
            log.error("No se pudo publicar el evento '{}' (altKey={}); se continua sin deshacer la operacion.",
                    evento.tipo(), evento.altKey(), e);
        }
    }
}
