package cl.smid.requerimientos.dominio.puerto.salida;

import cl.smid.requerimientos.dominio.modelo.EventoDominio;

/**
 * Puerto de salida para publicar eventos de dominio (Núcleo 6.3). La implementación concreta
 * (log o RabbitMQ) se elige por configuración ({@code smid.eventos.transporte}). La publicación
 * es <b>tolerante a fallos</b>: un error de mensajería no debe deshacer la operación de negocio
 * ya confirmada.
 */
public interface EventoPublicador {

    /**
     * Publica un evento de dominio. Implementación tolerante a fallos (no propaga excepciones
     * de transporte).
     *
     * @param evento evento a publicar (solo metadatos no sensibles, G7)
     */
    void publicar(EventoDominio evento);
}
