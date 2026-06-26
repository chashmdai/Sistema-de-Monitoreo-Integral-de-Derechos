package cl.smid.casos.dominio.puerto.salida;

import java.time.Instant;
import java.util.Map;

/**
 * Puerto de salida para publicar eventos de dominio emitidos por Casos
 * ({@code caso.abierto}, {@code caso.cerrado}, {@code caso.archivado}, ...).
 *
 * <p>La publicación es <strong>tolerante a fallos</strong>: no deshace la operación de negocio si el
 * transporte no está disponible. El transporte es conmutable (log / RabbitMQ). La carga útil lleva
 * SOLO metadatos no sensibles (G7): nunca nombres, RUT ni relatos.</p>
 */
public interface PublicadorEventos {

    /** Publica un evento de dominio. Cualquier fallo de transporte se absorbe y registra. */
    void publicar(EventoDominio evento);

    /**
     * Evento de dominio emitido por Casos. El {@code tipo} actúa como clave de enrutamiento
     * ({@code caso.*}); {@code recursoAlt} es el alt_key del caso; {@code metadatos} es un mapa de
     * datos no sensibles.
     *
     * @param tipo       tipo del evento (p. ej. {@code caso.abierto}).
     * @param recursoAlt alt_key del caso afectado.
     * @param ocurridoEn instante UTC del hecho.
     * @param idSedeAlt  alt_key de la sede (para enrutamiento/segmentación de Auditoría).
     * @param idUnidadAlt alt_key de la unidad.
     * @param metadatos  metadatos no sensibles del evento.
     */
    record EventoDominio(String tipo, String recursoAlt, Instant ocurridoEn, String idSedeAlt,
                         String idUnidadAlt, Map<String, Object> metadatos) {

        public EventoDominio {
            metadatos = metadatos == null ? Map.of() : Map.copyOf(metadatos);
        }
    }
}
