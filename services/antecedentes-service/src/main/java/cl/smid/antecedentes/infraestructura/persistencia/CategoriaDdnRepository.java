package cl.smid.antecedentes.infraestructura.persistencia;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio Spring Data de categorias DDN. La consulta {@code buscar} aplica filtros
 * opcionales de texto (codigo/nombre) y vigencia tolerando nulos.
 */
public interface CategoriaDdnRepository extends JpaRepository<CategoriaDdnEntity, Long> {

    Optional<CategoriaDdnEntity> findByAltKey(String altKey);

    boolean existsByCodigo(String codigo);

    boolean existsByAltKeyAndVigenteTrue(String altKey);

    List<CategoriaDdnEntity> findByIdIn(List<Long> ids);

    @Query("""
            SELECT r FROM CategoriaDdnEntity r
            WHERE (:texto IS NULL OR LOWER(r.nombre) LIKE LOWER(CONCAT('%', :texto, '%'))
                                  OR LOWER(r.codigo) LIKE LOWER(CONCAT('%', :texto, '%')))
              AND (:vigente IS NULL OR r.vigente = :vigente)
            """)
    Page<CategoriaDdnEntity> buscar(@Param("texto") String texto,
                                    @Param("vigente") Boolean vigente,
                                    Pageable pageable);
}
