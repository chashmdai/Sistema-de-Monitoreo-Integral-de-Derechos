package cl.smid.catalogo.infraestructura.persistencia.adaptador;

import cl.smid.catalogo.infraestructura.persistencia.CausaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repositorio Spring Data JPA de {@link CausaEntity}. Las operaciones del catálogo de causas
 * son simples y se resuelven con derivación de consultas por nombre de método.
 */
public interface SpringDataCausaRepository extends JpaRepository<CausaEntity, Long> {

    /** Causas vigentes de un derecho, en orden estable por código. */
    List<CausaEntity> findByIdDerechoAndVigenteTrueOrderByCodigoAsc(Long idDerecho);

    /** {@code true} si el derecho ya tiene una causa con ese código (unicidad por derecho). */
    boolean existsByIdDerechoAndCodigo(Long idDerecho, String codigo);
}
