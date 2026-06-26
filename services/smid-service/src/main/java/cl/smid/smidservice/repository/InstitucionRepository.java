package cl.smid.smidservice.repository;

import cl.smid.smidservice.entity.Institucion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstitucionRepository extends JpaRepository<Institucion, Long> {
}
