package cl.smid.personas.dominio.puerto.salida;

import java.time.Instant;

/**
 * Puerto de tiempo. Inyectar el reloj (en vez de llamar a {@code Instant.now()} en el dominio)
 * mantiene el núcleo puro y hace deterministas las pruebas (se puede fijar el instante).
 */
public interface RelojDominio {

    /** Instante actual en UTC. */
    Instant ahora();
}
