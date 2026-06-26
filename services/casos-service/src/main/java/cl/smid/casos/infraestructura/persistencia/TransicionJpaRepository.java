package cl.smid.casos.infraestructura.persistencia;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repositorio Spring Data para {@link TransicionEntity}. Carga los asientos de un caso por su FK,
 * ordenados cronológicamente, para reconstruir el historial del agregado.
 */
public interface TransicionJpaRepository extends JpaRepository<TransicionEntity, Long> {

    List<TransicionEntity> findByIdCasoOrderByOcurridoEnAsc(Long idCaso);
}
