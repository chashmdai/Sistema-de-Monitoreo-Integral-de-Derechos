package cl.smid.requerimientos.dominio.puerto.salida;

/**
 * Puerto de salida hacia catalogo-service (6.7). Verifica que un derecho (y su causa opcional)
 * referenciados por alt_key existan en el Catálogo, en los momentos críticos (alta de NNA con
 * derechos imputados). El dominio no conoce HTTP.
 */
public interface CatalogoDerechos {

    /**
     * Verifica que un derecho exista en el Catálogo.
     *
     * @param idDerechoAlt alt_key del derecho
     * @return {@code true} si el derecho existe y es referenciable
     */
    boolean existeDerecho(String idDerechoAlt);

    /**
     * Verifica que una causa exista y pertenezca al derecho indicado.
     *
     * @param idDerechoAlt alt_key del derecho padre
     * @param idCausaAlt   alt_key de la causa
     * @return {@code true} si la causa existe bajo ese derecho
     */
    boolean existeCausa(String idDerechoAlt, String idCausaAlt);
}
