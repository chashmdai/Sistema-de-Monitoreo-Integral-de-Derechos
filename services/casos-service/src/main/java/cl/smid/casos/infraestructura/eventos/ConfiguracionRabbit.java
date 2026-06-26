package cl.smid.casos.infraestructura.eventos;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Topología de mensajería del servicio (consumo y reentrega):
 * <ul>
 *   <li>Exchange de tópico {@code smid.eventos} (compartido por el clúster).</li>
 *   <li>Cola durable {@code casos.requerimiento-asignado} enlazada con la clave
 *       {@code requerimiento.asignado}.</li>
 *   <li>DLX {@code casos.dlx} y DLQ {@code casos.requerimiento-asignado.dlq} para mensajes que, tras
 *       agotar los reintentos, no pueden procesarse (p. ej. eventos malformados).</li>
 * </ul>
 *
 * <p>Se activa cuando {@code smid.eventos.consumo=rabbitmq} (por defecto). En despliegues sin broker
 * puede desactivarse con otro valor (p. ej. {@code none}).</p>
 */
@Configuration
@ConditionalOnProperty(name = "smid.eventos.consumo", havingValue = "rabbitmq", matchIfMissing = true)
public class ConfiguracionRabbit {

    /** Exchange de tópico compartido por todo el clúster SMID. */
    public static final String EXCHANGE_EVENTOS = "smid.eventos";
    /** Clave de enrutamiento del evento que materializa casos. */
    public static final String RK_REQUERIMIENTO_ASIGNADO = "requerimiento.asignado";
    /** Cola de consumo de este servicio. */
    public static final String COLA_REQUERIMIENTO_ASIGNADO = "casos.requerimiento-asignado";

    /** Exchange de cartas muertas y su cola asociada. */
    public static final String DLX_CASOS = "casos.dlx";
    public static final String COLA_DLQ = "casos.requerimiento-asignado.dlq";
    public static final String RK_DLQ = "casos.requerimiento-asignado.dlq";

    @Bean
    public TopicExchange exchangeEventos() {
        return new TopicExchange(EXCHANGE_EVENTOS, true, false);
    }

    @Bean
    public DirectExchange exchangeCartasMuertas() {
        return new DirectExchange(DLX_CASOS, true, false);
    }

    @Bean
    public Queue colaRequerimientoAsignado() {
        return QueueBuilder.durable(COLA_REQUERIMIENTO_ASIGNADO)
                .withArgument("x-dead-letter-exchange", DLX_CASOS)
                .withArgument("x-dead-letter-routing-key", RK_DLQ)
                .build();
    }

    @Bean
    public Queue colaCartasMuertas() {
        return QueueBuilder.durable(COLA_DLQ).build();
    }

    @Bean
    public Binding enlaceConsumo(Queue colaRequerimientoAsignado, TopicExchange exchangeEventos) {
        return BindingBuilder.bind(colaRequerimientoAsignado)
                .to(exchangeEventos)
                .with(RK_REQUERIMIENTO_ASIGNADO);
    }

    @Bean
    public Binding enlaceCartasMuertas(Queue colaCartasMuertas, DirectExchange exchangeCartasMuertas) {
        return BindingBuilder.bind(colaCartasMuertas).to(exchangeCartasMuertas).with(RK_DLQ);
    }
}
