package cl.smid.antecedentes.dominio.modelo;

/**
 * Tipos de tabla de referencia local del servicio (decision de diseno 5.3: viven en 6.8,
 * no en Catalogo 6.7). Las tres comparten estructura ({@code altKey}, {@code codigo} unico,
 * {@code nombre}, {@code vigente}); el tipo discrimina la tabla y el contexto de uso.
 */
public enum TipoReferencia {

    /** Categorias DDN (principal y secundarias de la ficha). */
    CATEGORIA,

    /** Procesos DDN del que emana la ficha. */
    PROCESO,

    /** Instrumentos (asociados a hallazgos). */
    INSTRUMENTO
}
