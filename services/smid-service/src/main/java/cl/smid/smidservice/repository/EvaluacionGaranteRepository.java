package cl.smid.smidservice.repository;

import cl.smid.smidservice.entity.EvaluacionGarante;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EvaluacionGaranteRepository extends JpaRepository<EvaluacionGarante, Long> {

    List<EvaluacionGarante> findByInstitucionId(Long institucionId);
}
