package cl.smid.instituciones.dominio.puerto.entrada;

import cl.smid.instituciones.dominio.modelo.ContextoSesion;
import cl.smid.instituciones.dominio.modelo.DetalleInstitucion;
import cl.smid.instituciones.dominio.modelo.FiltroInstituciones;
import cl.smid.instituciones.dominio.modelo.Pagina;
import cl.smid.instituciones.dominio.modelo.Paginado;
import cl.smid.instituciones.dominio.modelo.ResumenInstitucion;
import cl.smid.instituciones.dominio.puerto.entrada.comando.DatosInstitucion;

/**
 * Puerto de entrada (caso de uso) para la gestión de instituciones.
 *
 * <p>Lecturas sin contexto; escrituras con rol administrador (override #6). Las
 * escrituras devuelven el {@link DetalleInstitucion} para que la API serialice
 * siempre la misma forma de recurso.</p>
 */
public interface GestionInstitucionesUseCase {

    /**
     * Crea una institución (requiere rol administrador). Valida que el tipo exista y
     * esté vigente ({@code INS-422}) y la unicidad de código si viene ({@code INS-409}).
     *
     * @param contexto sesión autenticada
     * @param datos    datos de la institución
     * @return el detalle de la institución creada (sin puntos focales aún)
     */
    DetalleInstitucion crearInstitucion(ContextoSesion contexto, DatosInstitucion datos);

    /**
     * Edita una institución (requiere rol administrador).
     *
     * @param contexto sesión autenticada
     * @param altKey   alt_key de la institución
     * @param datos    nuevos datos
     * @return el detalle de la institución editada
     */
    DetalleInstitucion editarInstitucion(ContextoSesion contexto, String altKey, DatosInstitucion datos);

    /**
     * Activa o desactiva (baja lógica) una institución (requiere rol administrador).
     *
     * @param contexto sesión autenticada
     * @param altKey   alt_key de la institución
     * @param activa   nuevo estado
     * @return el detalle de la institución actualizada
     */
    DetalleInstitucion cambiarActivacionInstitucion(ContextoSesion contexto, String altKey, boolean activa);

    /**
     * Obtiene el detalle de una institución, incluidos sus puntos focales (lectura
     * pública autenticada).
     *
     * @param altKey alt_key de la institución
     * @return el detalle
     */
    DetalleInstitucion obtenerInstitucion(String altKey);

    /**
     * Lista instituciones con filtros y paginación (lectura pública autenticada).
     *
     * @param filtro   criterios opcionales (incluye búsqueda por RUT)
     * @param paginado paginación
     * @return página de resúmenes
     */
    Pagina<ResumenInstitucion> listarInstituciones(FiltroInstituciones filtro, Paginado paginado);
}
