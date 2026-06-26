package cl.smid.requerimientos.dominio.puerto.salida;

/**
 * Puerto de salida que resuelve el <b>código institucional de una sede</b> (p. ej. {@code RM})
 * a partir de su alt_key. Es necesario para componer el folio {@code {CODIGO_SEDE}-N/AÑO}
 * (Núcleo 6.5).
 *
 * <p>Decisión de diseño documentada: el token solo transporta {@code idSede} como alt_key; el
 * código institucional no viaja en los claims y no existe aún un endpoint estable de Identidad
 * que lo resuelva. Por eso la implementación por defecto es un catálogo local por configuración
 * ({@code smid.sedes.codigos.<alt_key>}), con un respaldo determinista. La interfaz deja la
 * costura para sustituirla, sin tocar el dominio, por un adaptador REST contra Identidad.</p>
 */
public interface DirectorioSedes {

    /**
     * Resuelve el código institucional de una sede.
     *
     * @param idSedeAlt alt_key de la sede
     * @return el código institucional (p. ej. {@code RM}); nunca nulo (usa respaldo si falta)
     */
    String codigoDeSede(String idSedeAlt);
}
