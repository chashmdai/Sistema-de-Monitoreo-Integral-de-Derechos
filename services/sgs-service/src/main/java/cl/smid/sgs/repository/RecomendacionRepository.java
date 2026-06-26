package cl.smid.sgs.repository;

import cl.smid.sgs.entity.Recomendacion;
import cl.smid.sgs.enums.EstadoGestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface RecomendacionRepository extends JpaRepository<Recomendacion, Long>, JpaSpecificationExecutor<Recomendacion> {

    List<Recomendacion> findByOficioId(Long oficioId);

    /** Scheduler: recomendaciones activas (no anuladas, estado no terminal) con su oficio (REP-5). */
    @Query("select r from Recomendacion r join fetch r.oficio " +
           "where r.anulado = false and r.estado not in :terminales")
    List<Recomendacion> findActivasParaAlerta(@Param("terminales") Set<EstadoGestion> terminales);
}
