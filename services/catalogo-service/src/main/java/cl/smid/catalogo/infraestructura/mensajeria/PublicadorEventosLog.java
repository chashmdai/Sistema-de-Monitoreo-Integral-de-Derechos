package cl.smid.catalogo.infraestructura.mensajeria;

import cl.smid.catalogo.dominio.puerto.salida.EventoDominio;
import cl.smid.catalogo.dominio.puerto.salida.EventoPublicador;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Implementación por defecto del {@link EventoPublicador}: serializa cada evento como una
 * línea JSON y la escribe en el <b>log de auditoría</b> {@code AUDIT.SMID.CATALOGO}, con la
 * forma del Núcleo 2.8.
 *
 * <h2>Por qué un log y no RabbitMQ</h2>
 * <p>El contrato del catálogo (Núcleo §9) marca los eventos como «solo si el contrato lo
 * exige»: al ser datos de referencia, no hay consumidores obligatorios hoy. Este adaptador
 * deja la <b>costura</b> lista: cuando se requiera publicar a RabbitMQ, basta con proveer otra
 * implementación de {@link EventoPublicador} (un {@code PublicadorEventosRabbitMQ}) y
 * registrarla como bean primario; el dominio no cambia en absoluto.</p>
 */
@Component
public class PublicadorEventosLog implements EventoPublicador {

    private static final Logger AUDITORIA = LoggerFactory.getLogger("AUDIT.SMID.CATALOGO");

    private final ObjectMapper objectMapper;

    public PublicadorEventosLog(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void publicar(EventoDominio evento) {
        Map<String, Object> registro = new LinkedHashMap<>();
        registro.put("evento", evento.evento());
        registro.put("ocurridoEn", evento.ocurridoEn() == null ? null : evento.ocurridoEn().toString());
        registro.put("actor", evento.actor());
        registro.put("recurso", evento.recurso());
        registro.put("idSede", evento.idSede());
        registro.put("idUnidad", evento.idUnidad());
        registro.put("datos", evento.datos());
        try {
            AUDITORIA.info(objectMapper.writeValueAsString(registro));
        } catch (JsonProcessingException e) {
            // Nunca se interrumpe la operación de negocio por un fallo de auditoría.
            AUDITORIA.warn("No se pudo serializar el evento '{}': {}", evento.evento(), e.getMessage());
        }
    }
}
