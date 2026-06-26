package cl.smid.personas.infraestructura.eventos;

import cl.smid.personas.dominio.modelo.EventoDominio;
import cl.smid.personas.dominio.puerto.salida.EventoPublicador;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Adaptador <b>por defecto</b> del puerto {@link EventoPublicador}: serializa el evento a JSON y
 * lo escribe en el canal de auditoría {@code AUDIT.SMID.PERSONAS}, igual que el servicio de
 * Catálogo. Es el transporte activo salvo que se configure {@code smid.eventos.transporte=rabbitmq}.
 *
 * <p>Tolerante a fallos: un problema serializando o registrando jamás aborta la operación de
 * negocio ya confirmada. Sólo se emiten metadatos no sensibles (G7).</p>
 */
@Component
@ConditionalOnProperty(name = "smid.eventos.transporte", havingValue = "log", matchIfMissing = true)
public class EventoPublicadorLog implements EventoPublicador {

    /** Canal de auditoría dedicado (configurable por logback hacia un destino central). */
    private static final Logger AUDITORIA = LoggerFactory.getLogger("AUDIT.SMID.PERSONAS");

    private final ObjectMapper objectMapper;

    public EventoPublicadorLog(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void publicar(EventoDominio evento) {
        try {
            Map<String, Object> registro = new LinkedHashMap<>();
            registro.put("evento", evento.nombre());
            registro.put("ocurridoEn", evento.ocurridoEn() == null ? null : evento.ocurridoEn().toString());
            registro.put("actor", evento.actor());
            registro.put("recurso", evento.recurso());
            registro.put("idSede", evento.idSede());
            registro.put("idUnidad", evento.idUnidad());
            registro.put("datos", evento.datos());
            AUDITORIA.info(objectMapper.writeValueAsString(registro));
        } catch (Exception e) {
            // No propagar: la auditoría es best-effort respecto de la transacción de negocio.
            AUDITORIA.warn("No se pudo serializar el evento '{}' para auditoría: {}",
                    evento.nombre(), e.getMessage());
        }
    }
}
