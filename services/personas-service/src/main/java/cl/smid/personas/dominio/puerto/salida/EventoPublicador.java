package cl.smid.personas.dominio.puerto.salida;

import cl.smid.personas.dominio.modelo.EventoDominio;

/**
 * Puerto de publicación de eventos de dominio hacia la futura Auditoría (Núcleo 2.8 / cap. 7).
 * El dominio publica a través de esta abstracción; el transporte concreto (log de auditoría o
 * RabbitMQ) lo decide la infraestructura por configuración ({@code smid.eventos.transporte}).
 */
public interface EventoPublicador {

    /**
     * Publica un evento de dominio. La implementación debe ser tolerante a fallos del transporte
     * de modo que un problema de mensajería no aborte la operación de negocio ya confirmada.
     *
     * @param evento evento a publicar (sólo metadatos no sensibles, G7)
     */
    void publicar(EventoDominio evento);
}
