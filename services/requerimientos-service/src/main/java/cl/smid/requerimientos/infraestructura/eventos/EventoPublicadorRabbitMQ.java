package cl.smid.requerimientos.infraestructura.eventos;

import cl.smid.requerimientos.dominio.modelo.EventoDominio;
import cl.smid.requerimientos.dominio.puerto.salida.EventoPublicador;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Implementación del puerto {@link EventoPublicador} sobre RabbitMQ (transporte {@code rabbitmq}).
 * Publica el evento en el topic exchange con la clave de enrutamiento igual al tipo del evento.
 *
 * <p>Es <b>tolerante a fallos</b>: si la publicación falla, registra una advertencia y no propaga
 * la excepción, para no deshacer la operación de negocio ya confirmada. El cuerpo es JSON con solo
 * metadatos no sensibles (G7).</p>
 */
@Component
@ConditionalOnProperty(name = "smid.eventos.transporte", havingValue = "rabbitmq")
public class EventoPublicadorRabbitMQ implements EventoPublicador {

    private static final Logger log = LoggerFactory.getLogger(EventoPublicadorRabbitMQ.class);

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;
    private final PropiedadesEventos propiedades;

    public EventoPublicadorRabbitMQ(RabbitTemplate rabbitTemplate, ObjectMapper objectMapper,
                                    PropiedadesEventos propiedades) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
        this.propiedades = propiedades;
    }

    @Override
    public void publicar(EventoDominio evento) {
        try {
            Map<String, Object> cuerpo = new LinkedHashMap<>();
            cuerpo.put("tipo", evento.tipo());
            cuerpo.put("altKey", evento.altKey());
            cuerpo.put("ocurridoEn", evento.ocurridoEn().toString());
            cuerpo.put("metadatos", evento.metadatos());
            String json = objectMapper.writeValueAsString(cuerpo);
            rabbitTemplate.convertAndSend(propiedades.exchange(), evento.tipo(), json);
        } catch (Exception e) {
            // Publicación tolerante a fallos: no romper la operación de negocio por un fallo de mensajería.
            log.warn("No se pudo publicar el evento [{}] del requerimiento {}: {}",
                    evento.tipo(), evento.altKey(), e.getMessage());
        }
    }
}
