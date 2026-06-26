package cl.smid.instituciones.dominio.modelo;

import java.time.Instant;
import java.util.Map;

/**
 * Evento de dominio <strong>metadata-only</strong>: describe un hecho de negocio sin
 * transportar el estado completo del agregado. El {@code altKey} viaja en el sobre del
 * evento (no dentro de {@code metadatos}); los {@code metadatos} llevan únicamente los
 * pocos atributos de contexto que los consumidores necesitan para enrutar/decidir.
 *
 * <p>El {@code tipo} (por ejemplo {@code institucion.creada}) actúa como
 * <em>routing key</em> en el exchange {@code smid.eventos} cuando el transporte es
 * RabbitMQ.</p>
 *
 * @param tipo      nombre del evento / routing key (por ejemplo {@code institucion.creada})
 * @param altKey    identificador público del agregado afectado
 * @param metadatos atributos de contexto mínimos (inmutable; nunca nulo)
 * @param ocurridoEn instante de ocurrencia (UTC)
 */
public record EventoDominio(String tipo, String altKey, Map<String, Object> metadatos, Instant ocurridoEn) {

    public EventoDominio {
        metadatos = metadatos == null ? Map.of() : Map.copyOf(metadatos);
    }

    /**
     * Crea un evento con sus metadatos.
     *
     * @param tipo       routing key del evento
     * @param altKey     alt_key del agregado
     * @param metadatos  atributos de contexto (puede ser nulo => vacío)
     * @param ocurridoEn instante de ocurrencia (UTC)
     * @return el evento
     */
    public static EventoDominio de(String tipo, String altKey, Map<String, Object> metadatos, Instant ocurridoEn) {
        return new EventoDominio(tipo, altKey, metadatos, ocurridoEn);
    }
}
