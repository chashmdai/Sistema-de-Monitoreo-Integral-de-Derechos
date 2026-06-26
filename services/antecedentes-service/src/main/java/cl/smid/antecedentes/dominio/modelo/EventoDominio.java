package cl.smid.antecedentes.dominio.modelo;

import java.time.Instant;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Sobre de un evento de dominio (metadata-only, override #7). El {@code altKey} del recurso
 * viaja en el sobre, <strong>no</strong> en los metadatos. Los metadatos nunca contienen
 * relatos, nombres de personas ni RUT de personas. Transporte conmutable (log/RabbitMQ); la
 * clave de enrutamiento del exchange de topicos es el {@code tipo}.
 *
 * @param tipo       tipo del evento (p. ej. {@code ficha.creada}); es la routing key
 * @param altKey     alt_key del recurso afectado
 * @param ocurridoEn instante UTC de ocurrencia
 * @param metadatos  metadatos no sensibles del evento
 */
public record EventoDominio(String tipo, String altKey, Instant ocurridoEn, Map<String, Object> metadatos) {

    public EventoDominio {
        if (tipo == null || tipo.isBlank()) {
            throw new IllegalArgumentException("El tipo del evento es obligatorio");
        }
        if (altKey == null || altKey.isBlank()) {
            throw new IllegalArgumentException("El alt_key del evento es obligatorio");
        }
        if (ocurridoEn == null) {
            throw new IllegalArgumentException("El instante del evento es obligatorio");
        }
        metadatos = (metadatos == null) ? Map.of() : Collections.unmodifiableMap(new LinkedHashMap<>(metadatos));
    }

    /** Crea un evento sin metadatos. */
    public static EventoDominio de(String tipo, String altKey, Instant ocurridoEn) {
        return new EventoDominio(tipo, altKey, ocurridoEn, Map.of());
    }
}
