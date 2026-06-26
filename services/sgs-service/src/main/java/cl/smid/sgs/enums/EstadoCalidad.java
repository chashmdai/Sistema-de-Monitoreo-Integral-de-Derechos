package cl.smid.sgs.enums;

/** Calidad del resultado de un procesamiento por bloques (CTRL-5 ESNNA). */
public enum EstadoCalidad {
    COMPLETA,   // todos los bloques/documentos procesados
    PARCIAL,    // algunos omitidos (ver lista de omitidos)
    FALLIDA     // nada utilizable
}
