package cl.smid.antecedentes.infraestructura.persistencia;

import cl.smid.antecedentes.dominio.modelo.Calificacion;
import cl.smid.antecedentes.dominio.modelo.EstadoFicha;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * Repositorio Spring Data de fichas. La bandeja proyecta {@link FichaResumenRow} via expresion
 * {@code new} (sin colecciones ni relato) y aplica los filtros funcionales y la acotacion
 * territorial: {@code sedeAlt}/{@code unidadAlt} nulos significan "sin filtro" (alcance
 * NACIONAL), y se fija solo el que corresponde al alcance del solicitante.
 */
public interface FichaRepository extends JpaRepository<FichaAntecedenteEntity, Long> {

    Optional<FichaAntecedenteEntity> findByAltKey(String altKey);

    void deleteByAltKey(String altKey);

    boolean existsByFolio(String folio);

    @Query("""
            SELECT new cl.smid.antecedentes.infraestructura.persistencia.FichaResumenRow(
                f.altKey, f.folio, f.estado, f.calificacion, f.percepcionHallazgo,
                f.unidadAlt, f.sedeAlt, f.casoAlt, f.creadoEn, f.actualizadoEn)
            FROM FichaAntecedenteEntity f
            WHERE (:estado IS NULL OR f.estado = :estado)
              AND (:calificacion IS NULL OR f.calificacion = :calificacion)
              AND (:casoAlt IS NULL OR f.casoAlt = :casoAlt)
              AND (:procesoId IS NULL OR f.procesoId = :procesoId)
              AND (:texto IS NULL OR LOWER(f.descripcion) LIKE LOWER(CONCAT('%', :texto, '%'))
                                  OR LOWER(f.folio) LIKE LOWER(CONCAT('%', :texto, '%')))
              AND (:sedeAlt IS NULL OR f.sedeAlt = :sedeAlt)
              AND (:unidadAlt IS NULL OR f.unidadAlt = :unidadAlt)
            ORDER BY f.creadoEn DESC
            """)
    Page<FichaResumenRow> buscar(@Param("estado") EstadoFicha estado,
                                 @Param("calificacion") Calificacion calificacion,
                                 @Param("casoAlt") String casoAlt,
                                 @Param("procesoId") Long procesoId,
                                 @Param("texto") String texto,
                                 @Param("sedeAlt") String sedeAlt,
                                 @Param("unidadAlt") String unidadAlt,
                                 Pageable pageable);
}
