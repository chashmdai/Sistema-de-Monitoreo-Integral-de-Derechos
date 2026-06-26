package cl.smid.instituciones.dominio.puerto.salida;

import cl.smid.instituciones.dominio.modelo.PuntoFocal;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida para la persistencia de {@link PuntoFocal}. El adaptador resuelve la
 * traducción entre el {@code institucionAlt} del dominio y la PK interna de la
 * institución ({@code institucion_id}).
 */
public interface RepositorioPuntosFocales {

    /**
     * Inserta o actualiza un punto focal identificándolo por su {@code alt_key}.
     *
     * @param puntoFocal el punto focal a persistir
     * @return el punto focal persistido
     */
    PuntoFocal guardar(PuntoFocal puntoFocal);

    /**
     * Busca un punto focal por su identificador público.
     *
     * @param altKey alt_key del punto focal
     * @return el punto focal si existe
     */
    Optional<PuntoFocal> buscarPorAlt(String altKey);

    /**
     * Lista los puntos focales de una institución (ordenados con el principal primero).
     *
     * @param institucionAlt alt_key de la institución
     * @return lista de puntos focales (posiblemente vacía)
     */
    List<PuntoFocal> listarPorInstitucion(String institucionAlt);

    /**
     * Desmarca como principal a todos los puntos focales <strong>activos</strong> de una
     * institución, excepto el indicado. Operación masiva ejecutada dentro de la
     * transacción del controlador para sostener la invariante "a lo sumo un principal
     * activo por institución".
     *
     * @param institucionAlt alt_key de la institución
     * @param altKeyActual   alt_key del punto focal que conserva la condición de principal
     */
    void desmarcarOtrosPrincipales(String institucionAlt, String altKeyActual);
}
