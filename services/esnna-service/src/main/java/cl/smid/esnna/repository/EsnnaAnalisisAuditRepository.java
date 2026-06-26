package cl.smid.esnna.repository;

import cl.smid.esnna.entity.EsnnaAnalisisAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Auditoría inmutable de análisis (BIZ-3). Solo escritura (al guardar) y lectura
 * para trazabilidad; nunca update/delete desde la app.
 */
public interface EsnnaAnalisisAuditRepository extends JpaRepository<EsnnaAnalisisAudit, Long> {

    List<EsnnaAnalisisAudit> findByCasoIdOrderByTimestampDesc(Long casoId);
}
