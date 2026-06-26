package cl.smid.requerimientos.dominio.modelo;

/**
 * Alcance territorial del usuario, transportado por el claim {@code alcance} del token
 * (Núcleo 2.3). Define el radio de visibilidad registro a registro.
 *
 * <p>El servicio <b>confía en el claim</b>: no recalcula el alcance.</p>
 */
public enum Alcance {

    /** Ve solo los registros de su unidad ({@code WHERE id_unidad = :idUnidad}). */
    UNIDAD,

    /** Ve todos los registros de su sede ({@code WHERE id_sede = :idSede}). */
    SEDE,

    /** Ve todo el país (sin filtro territorial). */
    NACIONAL
}
