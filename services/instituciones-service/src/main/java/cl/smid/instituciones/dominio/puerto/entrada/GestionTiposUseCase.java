package cl.smid.instituciones.dominio.puerto.entrada;

import cl.smid.instituciones.dominio.modelo.ContextoSesion;
import cl.smid.instituciones.dominio.modelo.FiltroTipos;
import cl.smid.instituciones.dominio.modelo.Pagina;
import cl.smid.instituciones.dominio.modelo.Paginado;
import cl.smid.instituciones.dominio.modelo.TipoInstitucion;
import cl.smid.instituciones.dominio.puerto.entrada.comando.DatosTipo;

/**
 * Puerto de entrada (caso de uso) para la gestión de tipos de institución.
 *
 * <p>Las operaciones de <strong>lectura</strong> no requieren contexto (datos de
 * referencia nacionales). Las de <strong>escritura</strong> exigen rol administrador
 * y reciben el {@link ContextoSesion} para autorizar en el dominio (override #6).</p>
 */
public interface GestionTiposUseCase {

    /**
     * Crea un tipo de institución (requiere rol administrador).
     *
     * @param contexto sesión autenticada
     * @param datos    datos del tipo
     * @return el tipo creado
     */
    TipoInstitucion crearTipo(ContextoSesion contexto, DatosTipo datos);

    /**
     * Edita un tipo de institución (requiere rol administrador).
     *
     * @param contexto sesión autenticada
     * @param altKey   alt_key del tipo
     * @param datos    nuevos datos
     * @return el tipo editado
     */
    TipoInstitucion editarTipo(ContextoSesion contexto, String altKey, DatosTipo datos);

    /**
     * Activa o desactiva (baja lógica) un tipo (requiere rol administrador).
     *
     * @param contexto sesión autenticada
     * @param altKey   alt_key del tipo
     * @param vigente  nuevo estado de vigencia
     * @return el tipo actualizado
     */
    TipoInstitucion cambiarVigenciaTipo(ContextoSesion contexto, String altKey, boolean vigente);

    /**
     * Obtiene el detalle de un tipo por su alt_key (lectura pública autenticada).
     *
     * @param altKey alt_key del tipo
     * @return el tipo
     */
    TipoInstitucion obtenerTipo(String altKey);

    /**
     * Lista tipos con filtros y paginación (lectura pública autenticada).
     *
     * @param filtro   criterios opcionales
     * @param paginado paginación
     * @return página de tipos
     */
    Pagina<TipoInstitucion> listarTipos(FiltroTipos filtro, Paginado paginado);
}
