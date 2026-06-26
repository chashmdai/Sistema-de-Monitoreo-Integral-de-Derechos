package cl.smid.requerimientos.infraestructura.eventos;

import cl.smid.requerimientos.dominio.modelo.EventoDominio;
import cl.smid.requerimientos.dominio.puerto.salida.EventoPublicador;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * Implementación por defecto del puerto {@link EventoPublicador} que solo registra el evento en el
 * log (transporte {@code log}). Útil en desarrollo y pruebas, donde no se levanta RabbitMQ. Publica
 * únicamente metadatos no sensibles (G7).
 */
@Component
@ConditionalOnProperty(name = "smid.eventos.transporte", havingValue = "log", matchIfMissing = true)
public class EventoPublicadorLog implements EventoPublicador {

    private static final Logger log = LoggerFactory.getLogger(EventoPublicadorLog.class);

    @Override
    public void publicar(EventoDominio evento) {
        // Solo metadatos: tipo, alt_key, instante y metadatos técnicos. Nunca datos personales.
        log.info("Evento de dominio [{}] requerimiento={} ocurridoEn={} metadatos={}",
                evento.tipo(), evento.altKey(), evento.ocurridoEn(), evento.metadatos());
    }
}
