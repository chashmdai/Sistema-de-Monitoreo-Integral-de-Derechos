package cl.smid.instituciones.dominio.puerto.salida;

import java.time.Instant;

/**
 * Puerto de salida para obtener el instante actual (UTC). Se inyecta para que el
 * dominio sea determinista y verificable con un reloj fijo en las pruebas.
 */
public interface RelojDominio {

    /**
     * @return el instante actual en UTC.
     */
    Instant ahora();
}
