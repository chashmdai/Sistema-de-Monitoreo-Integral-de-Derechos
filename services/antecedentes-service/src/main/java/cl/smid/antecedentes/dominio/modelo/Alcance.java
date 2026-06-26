package cl.smid.antecedentes.dominio.modelo;

/**
 * Alcance territorial del solicitante, transportado en el claim {@code alcance} del JWT
 * (Nucleo 2.3). Define el radio de visibilidad sobre las fichas (datos territoriales).
 */
public enum Alcance {

    /** Solo registros de la unidad del solicitante. */
    UNIDAD,

    /** Todos los registros de la sede del solicitante. */
    SEDE,

    /** Todos los registros del pais (sin filtro territorial). */
    NACIONAL;

    /**
     * Convierte un valor textual del claim a {@link Alcance}, tolerando nulos y
     * espacios. Un valor desconocido degrada al alcance mas restrictivo ({@link #UNIDAD}).
     *
     * @param valor texto del claim {@code alcance}
     * @return alcance correspondiente; nunca nulo
     */
    public static Alcance desde(String valor) {
        if (valor == null || valor.isBlank()) {
            return UNIDAD;
        }
        try {
            return Alcance.valueOf(valor.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return UNIDAD;
        }
    }
}
