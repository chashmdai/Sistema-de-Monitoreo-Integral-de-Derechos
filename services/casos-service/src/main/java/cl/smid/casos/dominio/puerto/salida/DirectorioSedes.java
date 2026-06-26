package cl.smid.casos.dominio.puerto.salida;

/**
 * Puerto de salida para resolver el código corto de una sede a partir de su {@code alt_key}.
 *
 * <p>El token solo transporta el {@code alt_key} de la sede, no su código (p. ej. {@code RM}). El
 * adaptador por defecto lo resuelve por configuración ({@code smid.sedes.codigos.<alt_key>}); queda
 * como costura para un futuro adaptador REST contra Identidad (6.1).</p>
 */
public interface DirectorioSedes {

    /**
     * Devuelve el código corto de la sede; si no hay mapeo configurado, retorna el código de respaldo.
     *
     * @param idSedeAlt alt_key de la sede.
     * @return código corto de sede (p. ej. {@code RM}).
     */
    String codigoDe(String idSedeAlt);
}
