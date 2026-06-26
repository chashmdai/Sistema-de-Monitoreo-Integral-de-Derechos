package cl.smid.personas.infraestructura.eventos;

import cl.smid.personas.dominio.modelo.EventoDominio;
import cl.smid.personas.dominio.puerto.salida.EventoPublicador;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Adaptador del puerto {@link EventoPublicador} que publica los eventos en RabbitMQ hacia la
 * futura Auditoría (Núcleo cap. 7). Se activa con {@code smid.eventos.transporte=rabbitmq}; en su
 * ausencia rige el adaptador de log.
 *
 * <p>Publica en el exchange de tipo <i>topic</i> {@code smid.eventos} usando como routing key el
 * nombre del evento (p. ej. {@code persona.creada}). El cuerpo es JSON con sólo metadatos no
 * sensibles (G7). Es tolerante a fallos del broker para no abortar la operación de negocio.</p>
 */
@Component
@ConditionalOnProperty(name = "smid.eventos.transporte", havingValue = "rabbitmq")
public class EventoPublicadorRabbitMQ implements EventoPublicador {

    private static final Logger LOG = LoggerFactory.getLogger(EventoPublicadorRabbitMQ.class);

    /** Exchange topic común del ecosistema para eventos de dominio. */
    public static final String EXCHANGE_EVENTOS = "smid.eventos";

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    public EventoPublicadorRabbitMQ(RabbitTemplate rabbitTemplate, ObjectMapper objectMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public void publicar(EventoDominio evento) {
        try {
            Map<String, Object> cuerpo = new LinkedHashMap<>();
            cuerpo.put("evento", evento.nombre());
            cuerpo.put("ocurridoEn", evento.ocurridoEn() == null ? null : evento.ocurridoEn().toString());
            cuerpo.put("actor", evento.actor());
            cuerpo.put("recurso", evento.recurso());
            cuerpo.put("idSede", evento.idSede());
            cuerpo.put("idUnidad", evento.idUnidad());
            cuerpo.put("datos", evento.datos());

            byte[] json = objectMapper.writeValueAsBytes(cuerpo);
            MessageProperties props = new MessageProperties();
            props.setContentType("application/json");
            props.setContentEncoding(StandardCharsets.UTF_8.name());

            org.springframework.amqp.core.Message mensaje =
                    new org.springframework.amqp.core.Message(json, props);
            rabbitTemplate.send(EXCHANGE_EVENTOS, evento.nombre(), mensaje);
        } catch (Exception e) {
            // No propagar: la publicación es best-effort respecto de la transacción de negocio.
            LOG.warn("No se pudo publicar el evento '{}' en RabbitMQ: {}",
                    evento.nombre(), e.getMessage());
        }
    }
}
