package cl.smid.antecedentes.dominio.puerto.salida;

import cl.smid.antecedentes.dominio.modelo.EventoDominio;

/**
 * Puerto de salida para la publicacion de eventos de dominio (metadata-only). La
 * implementacion es conmutable (log/RabbitMQ) y tolerante a fallos: un fallo de publicacion
 * nunca deshace la operacion de negocio.
 */
public interface PublicadorEventos {

    void publicar(EventoDominio evento);
}
