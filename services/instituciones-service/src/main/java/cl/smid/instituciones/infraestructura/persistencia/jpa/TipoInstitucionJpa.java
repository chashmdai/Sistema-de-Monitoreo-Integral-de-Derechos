package cl.smid.instituciones.infraestructura.persistencia.jpa;

import cl.smid.instituciones.infraestructura.persistencia.entidad.TipoInstitucionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio Spring Data para {@link TipoInstitucionEntity}. Incluye soporte de
 * especificaciones para los filtros dinámicos del listado y una carga por lotes para
 * resolver los tipos de un conjunto de instituciones sin incurrir en N+1.
 */
public interface TipoInstitucionJpa
        extends JpaRepository<TipoInstitucionEntity, Long>, JpaSpecificationExecutor<TipoInstitucionEntity> {

    Optional<TipoInstitucionEntity> findByAltKey(String altKey);

    boolean existsByNombre(String nombre);

    boolean existsByNombreAndAltKeyNot(String nombre, String altKey);

    List<TipoInstitucionEntity> findByIdIn(Collection<Long> ids);
}
