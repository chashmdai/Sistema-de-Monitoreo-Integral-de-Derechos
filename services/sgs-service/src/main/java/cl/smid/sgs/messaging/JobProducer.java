package cl.smid.sgs.messaging;

import cl.smid.sgs.config.RabbitConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/** Publica los jobs al exchange (decisión #5). */
@Component
public class JobProducer {

    private final RabbitTemplate rabbit;
    public JobProducer(RabbitTemplate rabbit) { this.rabbit = rabbit; }

    public void publicarExtraccion(ExtraccionJobMessage msg) {
        rabbit.convertAndSend(RabbitConfig.EXCHANGE, RabbitConfig.RK_EXTRACCION, msg);
    }

    public void publicarEvaluacion(EvaluacionJobMessage msg) {
        rabbit.convertAndSend(RabbitConfig.EXCHANGE, RabbitConfig.RK_EVALUACION, msg);
    }
}
