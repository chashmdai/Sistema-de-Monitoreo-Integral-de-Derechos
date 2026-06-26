package cl.smid.catalogo.dominio.puerto.entrada;

/**
 * Contexto mínimo del actor que origina una operación de escritura, usado para enriquecer
 * los eventos de dominio (Núcleo 2.8) sin acoplar el núcleo a la capa de seguridad.
 *
 * <p>La capa api construye este record a partir del {@code ContextoSesion} (claims del JWT)
 * y lo pasa al caso de uso. Todos sus campos son identificadores opacos (alt_key) o nulos.</p>
 *
 * @param actor    alt_key del usuario autenticado (claim {@code sub})
 * @param idSede   alt_key de la sede del actor (claim {@code idSede}; puede ser nulo)
 * @param idUnidad alt_key de la unidad del actor (claim {@code idUnidad}; puede ser nulo)
 */
public record ActorEvento(String actor, String idSede, String idUnidad) {

    /** Actor del sistema (operaciones internas sin usuario), para usos excepcionales. */
    public static ActorEvento sistema() {
        return new ActorEvento("sistema", null, null);
    }
}
