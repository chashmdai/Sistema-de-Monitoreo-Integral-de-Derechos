package cl.smid.antecedentes.dominio.puerto.salida;

import cl.smid.antecedentes.dominio.modelo.Pagina;
import cl.smid.antecedentes.dominio.modelo.Referencia;
import cl.smid.antecedentes.dominio.modelo.TipoReferencia;

import java.util.Optional;

/**
 * Puerto de salida para las tablas de referencia locales (categorias, procesos, instrumentos).
 * El {@link TipoReferencia} discrimina la tabla destino.
 */
public interface RepositorioReferencias {

    /** Persiste (alta o actualizacion) una referencia y devuelve su estado persistido. */
    Referencia guardar(Referencia referencia);

    /** Recupera una referencia por tipo y alt_key. */
    Optional<Referencia> buscarPorAltKey(TipoReferencia tipo, String altKey);

    /** Indica si ya existe una referencia con ese codigo (unicidad por tipo). */
    boolean existePorCodigo(TipoReferencia tipo, String codigo);

    /** Indica si existe una referencia vigente con ese alt_key (validacion de asociacion). */
    boolean existeVigentePorAltKey(TipoReferencia tipo, String altKey);

    /** Lista referencias de un tipo con filtro de texto y vigencia. */
    Pagina<Referencia> listar(TipoReferencia tipo, String texto, Boolean vigente, int pagina, int tamano);
}
