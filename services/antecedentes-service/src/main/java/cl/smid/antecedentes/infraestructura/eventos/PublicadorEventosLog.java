package cl.smid.antecedentes.infraestructura.eventos;

import cl.smid.antecedentes.dominio.modelo.EventoDominio;
import cl.smid.antecedentes.dominio.puerto.salida.PublicadorEventos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * Transporte de eventos por log ({@code smid.eventos.transporte=log}, por defecto). Registra el
 * sobre metadata-only sin datos sensibles. Util en desarrollo y cuando no hay broker disponible.
 */
@Component
@ConditionalOnProperty(prefix = "smid.eventos", name = "transporte", havingValue = "log", matchIfMissing = true)
public class PublicadorEventosLog implements PublicadorEventos {

    private static final Logger log = LoggerFactory.getLogger(PublicadorEventosLog.class);

    @Override
    public void publicar(EventoDominio evento) {
        log.info("[evento] tipo={} altKey={} ocurridoEn={} metadatos={}",
                evento.tipo(), evento.altKey(), evento.ocurridoEn(), evento.metadatos());
    }
}
