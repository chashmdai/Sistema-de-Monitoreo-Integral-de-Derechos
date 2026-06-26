package cl.smid.instituciones.config;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración común de RabbitMQ, activa solo cuando el transporte de eventos es
 * {@code rabbitmq} (override #8). Declara el exchange de eventos del ecosistema y el
 * convertidor JSON que Spring Boot aplica automáticamente al {@code RabbitTemplate}.
 */
@Configuration
@ConditionalOnProperty(prefix = "smid.eventos", name = "transporte", havingValue = "rabbitmq")
public class ConfiguracionRabbitComun {

    /** Exchange de tópicos donde se publican los eventos de dominio del ecosistema SMID. */
    public static final String EXCHANGE_EVENTOS = "smid.eventos";

    @Bean
    TopicExchange exchangeEventos() {
        return new TopicExchange(EXCHANGE_EVENTOS, true, false);
    }

    @Bean
    Jackson2JsonMessageConverter convertidorMensajesJson() {
        Jackson2JsonMessageConverter convertidor = new Jackson2JsonMessageConverter();
        DefaultJackson2JavaTypeMapper mapeadorTipos = new DefaultJackson2JavaTypeMapper();
        mapeadorTipos.setTypePrecedence(Jackson2JavaTypeMapper.TypePrecedence.INFERRED);
        mapeadorTipos.setTrustedPackages("*");
        convertidor.setJavaTypeMapper(mapeadorTipos);
        return convertidor;
    }
}
