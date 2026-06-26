package cl.smid.antecedentes.infraestructura.persistencia;

import cl.smid.antecedentes.dominio.modelo.EstadoHallazgo;
import cl.smid.antecedentes.dominio.modelo.Temporalidad;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * Repositorio Spring Data de hallazgos (lectura nacional). El listado pagina sobre la entidad
 * (sin fetch-join para no paginar en memoria); el adaptador resuelve el instrumento por lotes
 * (id-&gt;alt_key) y mapea cada hallazgo.
 */
public interface HallazgoRepository extends JpaRepository<HallazgoEntity, Long> {

    Optional<HallazgoEntity> findByAltKey(String altKey);

    boolean existsByAltKey(String altKey);

    boolean existsByFolio(String folio);

    @Query("""
            SELECT h FROM HallazgoEntity h
            WHERE (:estado IS NULL OR h.estado = :estado)
              AND (:temporalidad IS NULL OR h.temporalidad = :temporalidad)
              AND (:texto IS NULL OR LOWER(h.titulo) LIKE LOWER(CONCAT('%', :texto, '%'))
                                  OR LOWER(h.descripcion) LIKE LOWER(CONCAT('%', :texto, '%'))
                                  OR LOWER(h.folio) LIKE LOWER(CONCAT('%', :texto, '%')))
            ORDER BY h.creadoEn DESC
            """)
    Page<HallazgoEntity> buscar(@Param("estado") EstadoHallazgo estado,
                                @Param("temporalidad") Temporalidad temporalidad,
                                @Param("texto") String texto,
                                Pageable pageable);
}
