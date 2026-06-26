package cl.smid.instituciones.infraestructura.eventos;

import cl.smid.instituciones.dominio.modelo.EventoDominio;
import cl.smid.instituciones.dominio.puerto.salida.PublicadorEventos;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Publicador de eventos por <strong>log</strong> (transporte por defecto, override #8).
 * Serializa un sobre metadata-only a JSON y lo emite por el logger de auditoría
 * {@code AUDIT.SMID.INSTITUCIONES}. La publicación nunca interrumpe la operación de
 * negocio: cualquier fallo de serialización se registra como advertencia.
 *
 * <p>Se activa cuando {@code smid.eventos.transporte=log} o cuando la propiedad no está
 * definida ({@code matchIfMissing=true}).</p>
 */
@Component
@ConditionalOnProperty(prefix = "smid.eventos", name = "transporte", havingValue = "log", matchIfMissing = true)
public class PublicadorEventosLog implements PublicadorEventos {

    private static final Logger LOG = LoggerFactory.getLogger("AUDIT.SMID.INSTITUCIONES");

    private final ObjectMapper objectMapper;

    public PublicadorEventosLog(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void publicar(EventoDominio evento) {
        try {
            Map<String, Object> sobre = new LinkedHashMap<>();
            sobre.put("tipo", evento.tipo());
            sobre.put("altKey", evento.altKey());
            sobre.put("metadatos", evento.metadatos());
            sobre.put("ocurridoEn", evento.ocurridoEn() == null ? null : evento.ocurridoEn().toString());
            LOG.info("evento_dominio {}", objectMapper.writeValueAsString(sobre));
        } catch (Exception ex) {
            LOG.warn("No se pudo serializar el evento de dominio tipo={} altKey={}: {}",
                    evento.tipo(), evento.altKey(), ex.getMessage());
        }
    }
}
