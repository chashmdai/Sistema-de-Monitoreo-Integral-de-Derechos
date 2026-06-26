package cl.smid.casos.dominio.modelo;

/**
 * Alcance territorial del usuario, transportado por el claim {@code alcance} del JWT (Núcleo 2.3).
 * Determina el radio de visibilidad sobre los casos:
 * <ul>
 *   <li>{@link #UNIDAD}: solo casos de su unidad.</li>
 *   <li>{@link #SEDE}: todos los casos de su sede.</li>
 *   <li>{@link #NACIONAL}: todo el país (sin filtro territorial).</li>
 * </ul>
 * Los servicios de negocio <strong>confían en el claim</strong>; no recalculan el alcance.
 */
public enum Alcance {
    UNIDAD,
    SEDE,
    NACIONAL
}
