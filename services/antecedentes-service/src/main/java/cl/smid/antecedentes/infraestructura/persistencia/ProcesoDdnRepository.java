package cl.smid.antecedentes.infraestructura.persistencia;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio Spring Data de procesos DDN.
 */
public interface ProcesoDdnRepository extends JpaRepository<ProcesoDdnEntity, Long> {

    Optional<ProcesoDdnEntity> findByAltKey(String altKey);

    boolean existsByCodigo(String codigo);

    boolean existsByAltKeyAndVigenteTrue(String altKey);

    List<ProcesoDdnEntity> findByIdIn(List<Long> ids);

    @Query("""
            SELECT r FROM ProcesoDdnEntity r
            WHERE (:texto IS NULL OR LOWER(r.nombre) LIKE LOWER(CONCAT('%', :texto, '%'))
                                  OR LOWER(r.codigo) LIKE LOWER(CONCAT('%', :texto, '%')))
              AND (:vigente IS NULL OR r.vigente = :vigente)
            """)
    Page<ProcesoDdnEntity> buscar(@Param("texto") String texto,
                                  @Param("vigente") Boolean vigente,
                                  Pageable pageable);
}
