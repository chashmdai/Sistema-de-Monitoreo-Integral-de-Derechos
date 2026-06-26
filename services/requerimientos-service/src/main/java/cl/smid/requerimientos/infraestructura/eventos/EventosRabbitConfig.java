package cl.smid.requerimientos.infraestructura.eventos;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Declara la topología de mensajería cuando el transporte de eventos es {@code rabbitmq}. El
 * exchange es de tipo topic, durable, para que las claves de enrutamiento ({@code requerimiento.*})
 * lleguen a los consumidores suscritos (p. ej. Auditoría).
 */
@Configuration
@ConditionalOnProperty(name = "smid.eventos.transporte", havingValue = "rabbitmq")
public class EventosRabbitConfig {

    /**
     * @param propiedades propiedades de eventos (nombre del exchange)
     * @return el topic exchange durable de eventos del ecosistema
     */
    @Bean
    public TopicExchange exchangeEventos(PropiedadesEventos propiedades) {
        return new TopicExchange(propiedades.exchange(), true, false);
    }
}
