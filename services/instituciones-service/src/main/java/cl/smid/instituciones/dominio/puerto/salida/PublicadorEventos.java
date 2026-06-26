package cl.smid.instituciones.dominio.puerto.salida;

import cl.smid.instituciones.dominio.modelo.EventoDominio;

/**
 * Puerto de salida para publicar eventos de dominio. El transporte concreto (log o
 * RabbitMQ) lo decide la configuración (override #8); la publicación es tolerante a
 * fallos: un error de mensajería nunca debe romper la transacción de negocio.
 */
public interface PublicadorEventos {

    /**
     * Publica un evento de dominio.
     *
     * @param evento el evento a publicar
     */
    void publicar(EventoDominio evento);
}
