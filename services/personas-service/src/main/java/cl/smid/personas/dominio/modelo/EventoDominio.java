package cl.smid.personas.dominio.modelo;

import java.time.Instant;
import java.util.Collections;
import java.util.Map;

/**
 * Evento de dominio emitido hacia la futura Auditoría (Núcleo 2.8 / cap. 7). Se publica a
 * través del puerto {@code EventoPublicador}; el dominio no sabe si terminará en un log o en
 * RabbitMQ.
 *
 * <p><b>G7 — sólo metadatos.</b> {@code datos} jamás contiene contenido sensible (nombres,
 * RUT, contactos): a lo sumo el tipo de persona y banderas como "tenía RUT". El recurso se
 * identifica por su {@code altKey} público, nunca por la PK interna.</p>
 *
 * @param nombre    nombre del evento (p. ej. {@code "persona.creada"}); también routing key
 * @param ocurridoEn instante UTC de ocurrencia
 * @param actor     alt_key del usuario que originó el evento (claim {@code sub})
 * @param recurso   alt_key de la persona afectada
 * @param idSede    alt_key de la sede de contexto
 * @param idUnidad  alt_key de la unidad de contexto
 * @param datos     metadatos no sensibles (mapa inmutable)
 */
public record EventoDominio(
        String nombre,
        Instant ocurridoEn,
        String actor,
        String recurso,
        String idSede,
        String idUnidad,
        Map<String, Object> datos
) {

    public EventoDominio {
        datos = (datos == null) ? Collections.emptyMap() : Map.copyOf(datos);
    }
}
