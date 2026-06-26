package cl.smid.instituciones.dominio.puerto.salida;

import cl.smid.instituciones.dominio.modelo.FiltroInstituciones;
import cl.smid.instituciones.dominio.modelo.Institucion;
import cl.smid.instituciones.dominio.modelo.Pagina;
import cl.smid.instituciones.dominio.modelo.Paginado;
import cl.smid.instituciones.dominio.modelo.ResumenInstitucion;

import java.util.Optional;

/**
 * Puerto de salida para la persistencia de {@link Institucion}. El adaptador resuelve
 * la traducción entre el {@code tipoAlt} del dominio y la PK interna del tipo
 * ({@code tipo_id}) al guardar y al cargar.
 */
public interface RepositorioInstituciones {

    /**
     * Inserta o actualiza una institución identificándola por su {@code alt_key}.
     * El adaptador resuelve {@code tipoAlt} -> {@code tipo_id} (el servicio garantiza
     * previamente que el tipo existe y está vigente).
     *
     * @param institucion la institución a persistir
     * @return la institución persistida
     */
    Institucion guardar(Institucion institucion);

    /**
     * Busca una institución por su identificador público (modelo de escritura, con su
     * {@code tipoAlt} ya resuelto).
     *
     * @param altKey alt_key de la institución
     * @return la institución si existe
     */
    Optional<Institucion> buscarPorAlt(String altKey);

    /**
     * Lista instituciones como modelos de lectura (con nombre y ámbito del tipo
     * resueltos por lotes, sin N+1), aplicando filtros opcionales y paginación.
     *
     * @param filtro   criterios opcionales
     * @param paginado paginación solicitada
     * @return página de resúmenes
     */
    Pagina<ResumenInstitucion> listar(FiltroInstituciones filtro, Paginado paginado);

    /**
     * Indica si ya existe una institución con el código dado (solo entre los no nulos).
     *
     * @param codigo código institucional
     * @return {@code true} si existe
     */
    boolean existePorCodigo(String codigo);

    /**
     * Igual que {@link #existePorCodigo(String)} pero excluyendo un alt_key (para editar).
     *
     * @param codigo         código institucional
     * @param altKeyExcluido alt_key a excluir
     * @return {@code true} si existe otra institución con ese código
     */
    boolean existePorCodigoExcluyendo(String codigo, String altKeyExcluido);
}
