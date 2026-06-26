package cl.smid.sgs.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Colas del procesamiento asíncrono GPT (decisión #5). DLQ para fallos no recuperables. */
@Configuration
public class RabbitConfig {

    public static final String EXCHANGE = "sgs.exchange";
    public static final String DLX = "sgs.dlx";

    public static final String Q_EXTRACCION = "sgs.extraccion.q";
    public static final String RK_EXTRACCION = "sgs.extraccion";
    public static final String Q_EVALUACION = "sgs.evaluacion.q";
    public static final String RK_EVALUACION = "sgs.evaluacion";
    public static final String Q_DLQ = "sgs.dlq";
    public static final String RK_DLQ = "sgs.dlq";

    @Bean
    public TopicExchange sgsExchange() { return new TopicExchange(EXCHANGE, true, false); }

    @Bean
    public TopicExchange sgsDlx() { return new TopicExchange(DLX, true, false); }

    @Bean
    public Queue extraccionQueue() {
        return QueueBuilder.durable(Q_EXTRACCION).withArgument("x-dead-letter-exchange", DLX)
                .withArgument("x-dead-letter-routing-key", RK_DLQ).build();
    }

    @Bean
    public Queue evaluacionQueue() {
        return QueueBuilder.durable(Q_EVALUACION).withArgument("x-dead-letter-exchange", DLX)
                .withArgument("x-dead-letter-routing-key", RK_DLQ).build();
    }

    @Bean
    public Queue dlq() { return QueueBuilder.durable(Q_DLQ).build(); }

    @Bean
    public Binding bindExtraccion() { return BindingBuilder.bind(extraccionQueue()).to(sgsExchange()).with(RK_EXTRACCION); }

    @Bean
    public Binding bindEvaluacion() { return BindingBuilder.bind(evaluacionQueue()).to(sgsExchange()).with(RK_EVALUACION); }

    @Bean
    public Binding bindDlq() { return BindingBuilder.bind(dlq()).to(sgsDlx()).with(RK_DLQ); }

    @Bean
    public MessageConverter jacksonMessageConverter() { return new Jackson2JsonMessageConverter(); }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory cf, MessageConverter mc) {
        RabbitTemplate t = new RabbitTemplate(cf);
        t.setMessageConverter(mc);
        return t;
    }
}
