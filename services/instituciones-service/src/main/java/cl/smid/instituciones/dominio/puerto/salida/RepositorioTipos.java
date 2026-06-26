package cl.smid.instituciones.dominio.puerto.salida;

import cl.smid.instituciones.dominio.modelo.FiltroTipos;
import cl.smid.instituciones.dominio.modelo.Pagina;
import cl.smid.instituciones.dominio.modelo.Paginado;
import cl.smid.instituciones.dominio.modelo.TipoInstitucion;

import java.util.Optional;

/**
 * Puerto de salida para la persistencia de {@link TipoInstitucion}. La implementación
 * (adaptador JPA) realiza el upsert por {@code alt_key} y resuelve la unicidad de nombre.
 */
public interface RepositorioTipos {

    /**
     * Inserta o actualiza un tipo identificándolo por su {@code alt_key} (preservando
     * la PK interna y la fecha de creación si ya existía).
     *
     * @param tipo el tipo a persistir
     * @return el tipo persistido
     */
    TipoInstitucion guardar(TipoInstitucion tipo);

    /**
     * Busca un tipo por su identificador público.
     *
     * @param altKey alt_key del tipo
     * @return el tipo si existe
     */
    Optional<TipoInstitucion> buscarPorAlt(String altKey);

    /**
     * Indica si ya existe un tipo con el nombre dado (comparación insensible a
     * mayúsculas/acentos por la collation de la base).
     *
     * @param nombreNormalizado nombre ya normalizado
     * @return {@code true} si existe
     */
    boolean existePorNombre(String nombreNormalizado);

    /**
     * Igual que {@link #existePorNombre(String)} pero excluyendo un alt_key (para editar
     * sin chocar consigo mismo).
     *
     * @param nombreNormalizado nombre ya normalizado
     * @param altKeyExcluido    alt_key a excluir de la verificación
     * @return {@code true} si existe otro tipo con ese nombre
     */
    boolean existePorNombreExcluyendo(String nombreNormalizado, String altKeyExcluido);

    /**
     * Lista tipos aplicando filtros opcionales y paginación.
     *
     * @param filtro   criterios opcionales
     * @param paginado paginación solicitada
     * @return página de tipos
     */
    Pagina<TipoInstitucion> listar(FiltroTipos filtro, Paginado paginado);
}
