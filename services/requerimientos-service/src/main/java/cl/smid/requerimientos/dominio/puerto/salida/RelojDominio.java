package cl.smid.requerimientos.dominio.puerto.salida;

import java.time.Instant;

/**
 * Puerto de salida que provee el instante actual en UTC. Abstraer el reloj mantiene el dominio
 * puro y determinista en pruebas (se inyecta un reloj fijo).
 */
public interface RelojDominio {

    /** @return el instante actual en UTC. */
    Instant ahora();
}
