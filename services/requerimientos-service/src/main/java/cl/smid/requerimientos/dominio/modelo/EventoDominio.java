package cl.smid.requerimientos.dominio.modelo;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;

/**
 * Evento de dominio emitido por el requerimiento hacia Auditoría/otros servicios (Núcleo 6.3, G7).
 *
 * <p><b>Restricción G7 (protección de datos):</b> el evento transporta <b>solo metadatos no
 * sensibles</b>. Jamás incluye nombres, RUT, relatos ni contenido. Lo que viaja es: el tipo de
 * evento (clave de enrutamiento, p. ej. {@code requerimiento.ingresado}), el {@code alt_key}
 * público del requerimiento, el instante UTC, y un mapa acotado de metadatos técnicos
 * (estado, folio, complejidad, banderas).</p>
 *
 * @param tipo       clave de enrutamiento del evento (routing key del topic exchange)
 * @param altKey     alt_key público del requerimiento afectado
 * @param ocurridoEn instante UTC en que ocurrió el evento
 * @param metadatos  metadatos técnicos no sensibles (inmutable; nunca datos personales)
 */
public record EventoDominio(String tipo, String altKey, Instant ocurridoEn, Map<String, Object> metadatos) {

    public EventoDominio {
        Objects.requireNonNull(tipo, "El tipo de evento es obligatorio");
        Objects.requireNonNull(altKey, "El alt_key del evento es obligatorio");
        Objects.requireNonNull(ocurridoEn, "El instante del evento es obligatorio");
        metadatos = (metadatos == null) ? Map.of() : Map.copyOf(metadatos);
    }

    /**
     * Construye un evento marcando metadatos no sensibles.
     *
     * @param tipo       clave de enrutamiento (p. ej. {@code requerimiento.asignado})
     * @param altKey     alt_key del requerimiento
     * @param ocurridoEn instante UTC
     * @param metadatos  metadatos técnicos (nulable)
     * @return el evento de dominio
     */
    public static EventoDominio de(String tipo, String altKey, Instant ocurridoEn, Map<String, Object> metadatos) {
        return new EventoDominio(tipo, altKey, ocurridoEn, metadatos);
    }
}
