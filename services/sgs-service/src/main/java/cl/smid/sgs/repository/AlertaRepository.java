package cl.smid.sgs.repository;

import cl.smid.sgs.entity.AlertaSeguimiento;
import cl.smid.sgs.enums.TipoAlerta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertaRepository extends JpaRepository<AlertaSeguimiento, Long> {

    /** Dedup: no regenerar una alerta del mismo tipo aún no atendida para la misma recomendación. */
    boolean existsByRecomendacionIdAndTipoAndAtendidaFalse(Long recomendacionId, TipoAlerta tipo);

    List<AlertaSeguimiento> findByAtendidaFalseOrderByFechaLimiteAsc();

    List<AlertaSeguimiento> findByAtendidaFalseAndNotificadaTelegramFalse();
}
