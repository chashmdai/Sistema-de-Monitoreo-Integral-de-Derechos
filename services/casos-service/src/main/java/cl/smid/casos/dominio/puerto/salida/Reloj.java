package cl.smid.casos.dominio.puerto.salida;

import java.time.Instant;

/**
 * Puerto de salida para obtener el instante actual. Se abstrae para permitir relojes deterministas
 * en pruebas de dominio (sin Spring ni dependencias de infraestructura).
 */
public interface Reloj {

    /** Instante actual en UTC. */
    Instant ahora();
}
