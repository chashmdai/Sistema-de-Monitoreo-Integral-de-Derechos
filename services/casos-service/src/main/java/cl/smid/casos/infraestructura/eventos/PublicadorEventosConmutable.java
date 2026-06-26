package cl.smid.casos.infraestructura.eventos;

import cl.smid.casos.config.PropiedadesEventos;
import cl.smid.casos.dominio.puerto.salida.PublicadorEventos;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Publicador de eventos de dominio con transporte CONMUTABLE ({@code smid.eventos.transporte}):
 * <ul>
 *   <li>{@code log}: registra el evento serializado (útil en desarrollo y pruebas sin broker).</li>
 *   <li>{@code rabbitmq}: publica en el exchange {@code smid.eventos} con clave igual al tipo.</li>
 * </ul>
 *
 * <p>La publicación es <strong>tolerante a fallos</strong>: cualquier problema de transporte se
 * registra pero NO interrumpe la operación de negocio (la materialización/transición ya está
 * confirmada). El sobre emitido lleva solo metadatos no sensibles (G7).</p>
 */
@Component
public class PublicadorEventosConmutable implements PublicadorEventos {

    private static final Logger log = LoggerFactory.getLogger(PublicadorEventosConmutable.class);
    private static final String TRANSPORTE_RABBITMQ = "rabbitmq";

    private final PropiedadesEventos propiedades;
    private final ObjectMapper objectMapper;
    private final ObjectProvider<RabbitTemplate> rabbitTemplate;

    public PublicadorEventosConmutable(PropiedadesEventos propiedades, ObjectMapper objectMapper,
                                       ObjectProvider<RabbitTemplate> rabbitTemplate) {
        this.propiedades = propiedades;
        this.objectMapper = objectMapper;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void publicar(EventoDominio evento) {
        try {
            byte[] cuerpo = objectMapper.writeValueAsBytes(aSobre(evento));
            if (TRANSPORTE_RABBITMQ.equalsIgnoreCase(propiedades.transporte())) {
                publicarEnRabbit(evento.tipo(), cuerpo);
            } else {
                log.info("[evento:{}] {}", evento.tipo(), new String(cuerpo, StandardCharsets.UTF_8));
            }
        } catch (Exception ex) {
            // Tolerante: no se revierte el negocio por un fallo de publicación.
            log.warn("No se pudo publicar el evento '{}' del caso {}: {}",
                    evento.tipo(), evento.recursoAlt(), ex.getMessage());
        }
    }

    private void publicarEnRabbit(String tipo, byte[] cuerpo) {
        RabbitTemplate plantilla = rabbitTemplate.getIfAvailable();
        if (plantilla == null) {
            log.warn("Transporte 'rabbitmq' configurado pero no hay RabbitTemplate disponible; "
                    + "se omite la publicación del evento '{}'.", tipo);
            return;
        }
        Message mensaje = MessageBuilder.withBody(cuerpo)
                .setContentType(MessageProperties.CONTENT_TYPE_JSON)
                .setContentEncoding(StandardCharsets.UTF_8.name())
                .setDeliveryMode(MessageDeliveryMode.PERSISTENT)
                .build();
        plantilla.send(ConfiguracionRabbit.EXCHANGE_EVENTOS, tipo, mensaje);
    }

    /** Sobre de salida: {@code { tipo, altKey, ocurridoEn, metadatos }} (altKey = alt_key del caso). */
    private Map<String, Object> aSobre(EventoDominio evento) {
        Map<String, Object> sobre = new LinkedHashMap<>();
        sobre.put("tipo", evento.tipo());
        sobre.put("altKey", evento.recursoAlt());
        sobre.put("ocurridoEn", evento.ocurridoEn() == null ? null : evento.ocurridoEn().toString());
        sobre.put("metadatos", evento.metadatos());
        return sobre;
    }
}
