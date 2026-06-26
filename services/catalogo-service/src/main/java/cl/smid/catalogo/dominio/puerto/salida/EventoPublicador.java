package cl.smid.catalogo.dominio.puerto.salida;

/**
 * Puerto de salida para publicar {@link EventoDominio}.
 *
 * <p>El dominio anuncia hechos ya ocurridos (creación, actualización, baja) sin conocer el
 * mecanismo de transporte. Según el contrato del Núcleo (§9), el servicio de Catálogo
 * <b>no exige</b> un bus de mensajería: por eso la implementación por defecto es un
 * adaptador que escribe los eventos al log de auditoría ({@code AUDIT.SMID.CATALOGO},
 * Núcleo 2.8). Migrar a RabbitMQ consiste únicamente en proveer otra implementación de
 * este puerto, sin tocar el dominio.</p>
 */
public interface EventoPublicador {

    /** Publica un hecho de dominio ya ocurrido. */
    void publicar(EventoDominio evento);
}
