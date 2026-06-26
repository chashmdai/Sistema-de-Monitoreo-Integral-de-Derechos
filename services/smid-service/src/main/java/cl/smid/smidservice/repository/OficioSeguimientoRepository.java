package cl.smid.smidservice.repository;

import cl.smid.smidservice.entity.OficioSeguimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OficioSeguimientoRepository extends JpaRepository<OficioSeguimiento, Long> {

    List<OficioSeguimiento> findByEvaluacionId(Long evaluacionId);
}
