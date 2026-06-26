package cl.smid.antecedentes.config;

import cl.smid.antecedentes.infraestructura.eventos.PublicadorEventosRabbit;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuracion de RabbitMQ activa solo cuando {@code smid.eventos.transporte=rabbitmq}.
 * Declara el exchange de topicos {@code smid.eventos} (durable) y fija el conversor JSON que
 * usara el {@code RabbitTemplate} autoconfigurado. En modo {@code log} no se declara nada y no
 * se requiere broker.
 */
@Configuration
@ConditionalOnProperty(prefix = "smid.eventos", name = "transporte", havingValue = "rabbitmq")
public class ConfiguracionRabbit {

    @Bean
    public TopicExchange exchangeEventos() {
        return new TopicExchange(PublicadorEventosRabbit.EXCHANGE, true, false);
    }

    @Bean
    public MessageConverter conversorJsonRabbit() {
        return new Jackson2JsonMessageConverter();
    }
}
