package cl.smid.casos.infraestructura.eventos;

import cl.smid.casos.dominio.modelo.EventoRequerimientoAsignado;
import cl.smid.casos.dominio.puerto.entrada.MaterializarCaso;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;

/**
 * Consumidor del evento {@code requerimiento.asignado}: materializa el Caso correspondiente.
 *
 * <p><strong>Idempotencia (entrega at-least-once).</strong> La materialización es idempotente por
 * diseño: el servicio comprueba la existencia previa por requerimiento de origen (no-op en reentrega)
 * y la clave única en base de datos es la garantía durable. La política de reintentos del contenedor
 * (configurada en {@code application.yml}) reejecuta ante una carrera entre consumidores, resolviéndose
 * entonces como no-op.</p>
 *
 * <p><strong>Demarcación transaccional.</strong> El método es {@code @Transactional}: la reserva de
 * correlativo, el alta del caso y el asiento de apertura se confirman juntos o se revierten juntos.</p>
 *
 * <p><strong>Identidad asíncrona.</strong> El listener NO porta token de usuario ni contexto de
 * seguridad: la materialización es un "esqueleto" con referencias del evento; el enriquecimiento se
 * difiere a la consulta on-demand con el token del usuario.</p>
 *
 * <p>Eventos malformados o inválidos se rechazan sin reencolar ({@link AmqpRejectAndDontRequeueException}),
 * y el broker los deriva a la DLQ para inspección.</p>
 */
@Component
@ConditionalOnProperty(name = "smid.eventos.consumo", havingValue = "rabbitmq", matchIfMissing = true)
public class ListenerRequerimientoAsignado {

    private static final Logger log = LoggerFactory.getLogger(ListenerRequerimientoAsignado.class);

    private final MaterializarCaso materializarCaso;
    private final ObjectMapper objectMapper;

    public ListenerRequerimientoAsignado(MaterializarCaso materializarCaso, ObjectMapper objectMapper) {
        this.materializarCaso = materializarCaso;
        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = ConfiguracionRabbit.COLA_REQUERIMIENTO_ASIGNADO)
    @Transactional
    public void alRecibir(Message mensaje) {
        EventoRequerimientoAsignado evento;
        try {
            String cuerpo = new String(mensaje.getBody(), StandardCharsets.UTF_8);
            PayloadEventoEntrante payload = objectMapper.readValue(cuerpo, PayloadEventoEntrante.class);
            evento = payload.aDominio();
        } catch (Exception parseo) {
            // Evento ilegible o inválido: reintentarlo no ayuda → DLQ directo.
            log.warn("Evento requerimiento.asignado descartado a DLQ por formato inválido: {}",
                    parseo.getMessage());
            throw new AmqpRejectAndDontRequeueException("Evento inválido", parseo);
        }

        // Idempotente: en reentrega es no-op; en carrera real lanza y el reintento lo resuelve.
        materializarCaso.materializar(evento);
        log.debug("Evento requerimiento.asignado procesado para origen {}.",
                evento.requerimientoOrigenAlt());
    }
}
