package cl.smid.instituciones.dominio.puerto.entrada;

import cl.smid.instituciones.dominio.modelo.ContextoSesion;
import cl.smid.instituciones.dominio.modelo.PuntoFocal;
import cl.smid.instituciones.dominio.puerto.entrada.comando.DatosPuntoFocal;

/**
 * Puerto de entrada (caso de uso) para la gestión de puntos focales de una institución.
 * Todas las operaciones son escrituras y requieren rol administrador (override #6).
 */
public interface GestionPuntosFocalesUseCase {

    /**
     * Crea un punto focal en una institución (requiere rol administrador). Si se marca
     * como principal, desmarca a los demás principales activos en la misma transacción.
     *
     * @param contexto       sesión autenticada
     * @param institucionAlt alt_key de la institución
     * @param datos          datos del punto focal
     * @return el punto focal creado
     */
    PuntoFocal crearPuntoFocal(ContextoSesion contexto, String institucionAlt, DatosPuntoFocal datos);

    /**
     * Edita un punto focal (requiere rol administrador). Mantiene la invariante de un
     * solo principal activo por institución.
     *
     * @param contexto sesión autenticada
     * @param altKey   alt_key del punto focal
     * @param datos    nuevos datos
     * @return el punto focal editado
     */
    PuntoFocal editarPuntoFocal(ContextoSesion contexto, String altKey, DatosPuntoFocal datos);

    /**
     * Activa o desactiva un punto focal (requiere rol administrador).
     *
     * @param contexto sesión autenticada
     * @param altKey   alt_key del punto focal
     * @param activo   nuevo estado
     * @return el punto focal actualizado
     */
    PuntoFocal cambiarActivacionPuntoFocal(ContextoSesion contexto, String altKey, boolean activo);
}
