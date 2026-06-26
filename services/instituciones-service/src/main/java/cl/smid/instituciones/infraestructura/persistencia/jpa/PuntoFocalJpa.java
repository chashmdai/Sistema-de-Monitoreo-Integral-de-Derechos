package cl.smid.instituciones.infraestructura.persistencia.jpa;

import cl.smid.instituciones.infraestructura.persistencia.entidad.PuntoFocalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio Spring Data para {@link PuntoFocalEntity}.
 */
public interface PuntoFocalJpa extends JpaRepository<PuntoFocalEntity, Long> {

    Optional<PuntoFocalEntity> findByAltKey(String altKey);

    /** Puntos focales de una institución, con el principal primero. */
    List<PuntoFocalEntity> findByInstitucionIdOrderByPrincipalDescIdAsc(Long institucionId);

    /**
     * Desmarca como principal a todos los puntos focales <strong>activos</strong> de una
     * institución salvo el indicado. Operación masiva para sostener la invariante de un
     * solo principal activo por institución.
     *
     * @param institucionId PK interna de la institución
     * @param altKeyActual  alt_key del punto focal que conserva la condición de principal
     * @return número de filas afectadas
     */
    @Modifying
    @Query("UPDATE PuntoFocalEntity p SET p.principal = false "
            + "WHERE p.institucionId = :institucionId AND p.activo = true "
            + "AND p.principal = true AND p.altKey <> :altKeyActual")
    int desmarcarOtrosPrincipales(@Param("institucionId") Long institucionId,
                                  @Param("altKeyActual") String altKeyActual);
}
