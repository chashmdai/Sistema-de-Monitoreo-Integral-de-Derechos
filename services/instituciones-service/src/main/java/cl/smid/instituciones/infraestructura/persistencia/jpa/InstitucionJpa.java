package cl.smid.instituciones.infraestructura.persistencia.jpa;

import cl.smid.instituciones.infraestructura.persistencia.entidad.InstitucionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

/**
 * Repositorio Spring Data para {@link InstitucionEntity}. Las consultas de listado con
 * filtros opcionales se construyen como {@link org.springframework.data.jpa.domain.Specification}
 * en el adaptador.
 */
public interface InstitucionJpa
        extends JpaRepository<InstitucionEntity, Long>, JpaSpecificationExecutor<InstitucionEntity> {

    Optional<InstitucionEntity> findByAltKey(String altKey);

    boolean existsByCodigo(String codigo);

    boolean existsByCodigoAndAltKeyNot(String codigo, String altKey);
}
