package cl.smid.casos.infraestructura.persistencia;

import cl.smid.casos.dominio.modelo.EstadoCaso;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * Repositorio Spring Data para {@link CasoEntity}. Incluye la consulta territorial paginada que
 * traduce el alcance del solicitante (NACIONAL / SEDE / UNIDAD) directamente en SQL.
 */
public interface CasoJpaRepository extends JpaRepository<CasoEntity, Long> {

    Optional<CasoEntity> findByAltKey(String altKey);

    Optional<CasoEntity> findByIdRequerimientoOrigenAlt(String idRequerimientoOrigenAlt);

    boolean existsByIdRequerimientoOrigenAlt(String idRequerimientoOrigenAlt);

    /**
     * Lista paginada aplicando filtros opcionales (estado, unidad) y el recorte territorial. El
     * recorte se expresa con el parámetro {@code alcance}: NACIONAL no filtra; SEDE exige coincidencia
     * de sede; UNIDAD exige coincidencia de unidad. Spring Data deriva la consulta de conteo.
     */
    @Query("""
            SELECT c FROM CasoEntity c
            WHERE c.vigente = true
              AND (:estado IS NULL OR c.estado = :estado)
              AND (:unidadFiltro IS NULL OR c.idUnidadAlt = :unidadFiltro)
              AND ( :alcance = 'NACIONAL'
                 OR (:alcance = 'SEDE' AND c.idSedeAlt = :ctxSede)
                 OR (:alcance = 'UNIDAD' AND c.idUnidadAlt = :ctxUnidad) )
            """)
    Page<CasoEntity> buscarTerritorial(@Param("estado") EstadoCaso estado,
                                       @Param("unidadFiltro") String unidadFiltro,
                                       @Param("alcance") String alcance,
                                       @Param("ctxSede") String ctxSede,
                                       @Param("ctxUnidad") String ctxUnidad,
                                       Pageable pageable);
}
