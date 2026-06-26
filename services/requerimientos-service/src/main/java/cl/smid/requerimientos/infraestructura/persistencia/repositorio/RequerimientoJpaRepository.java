package cl.smid.requerimientos.infraestructura.persistencia.repositorio;

import cl.smid.requerimientos.infraestructura.persistencia.entidad.RequerimientoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

/**
 * Repositorio Spring Data del agregado requerimiento. Extiende {@link JpaSpecificationExecutor}
 * para construir el filtro dinámico (estado, unidad y alcance territorial) con criterios tipados.
 */
public interface RequerimientoJpaRepository
        extends JpaRepository<RequerimientoEntity, Long>, JpaSpecificationExecutor<RequerimientoEntity> {

    /**
     * Busca un requerimiento vigente por su alt_key.
     *
     * @param altKey alt_key público
     * @return la entidad si existe y está vigente
     */
    Optional<RequerimientoEntity> findByAltKeyAndVigenteTrue(String altKey);
}
