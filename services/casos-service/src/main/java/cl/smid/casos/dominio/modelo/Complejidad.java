package cl.smid.casos.dominio.modelo;

/**
 * Complejidad del requerimiento de origen, propagada al Caso a través del evento.
 * Es metadato no sensible: viaja en el evento y se conserva como referencia para la operación.
 */
public enum Complejidad {
    BAJA,
    MEDIANA,
    ALTA,
    FAST_TRACK
}
