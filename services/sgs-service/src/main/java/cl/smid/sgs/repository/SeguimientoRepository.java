package cl.smid.sgs.repository;

import cl.smid.sgs.entity.Seguimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeguimientoRepository extends JpaRepository<Seguimiento, Long> {

    List<Seguimiento> findByRecomendacionIdOrderByFechaSeguimientoDesc(Long recomendacionId);

    /** Último seguimiento por recomendación, en una sola query (evita N+1 en el tablero). */
    @Query("select s from Seguimiento s where s.id in " +
           "(select max(s2.id) from Seguimiento s2 where s2.recomendacion.id in :ids group by s2.recomendacion.id)")
    List<Seguimiento> findUltimosPorRecomendacion(@Param("ids") List<Long> ids);

    /** Export: una fila por seguimiento (decisión #16). Solo ToOne -> sin MultipleBagFetch. */
    @Query("select s from Seguimiento s " +
           "join fetch s.recomendacion r join fetch r.oficio o " +
           "left join fetch r.materia left join fetch r.categoria " +
           "left join fetch s.tipoSeguimiento left join fetch s.tipoRespuesta " +
           "where r.anulado = false " +
           "order by o.nroOficio asc, r.correlativo asc, s.fechaSeguimiento desc")
    List<Seguimiento> findAllForExport();

    // ---- Métricas de concordancia (decisión #12 ESNNA) ----
    @Query("select count(s) from Seguimiento s where s.evaluacionFinal is not null")
    long countConEvaluacionFinal();

    @Query("select count(s) from Seguimiento s where s.evaluacionFinal is not null " +
           "and s.evaluacionIA is not null and s.evaluacionFinal <> s.evaluacionIA")
    long countOverrides();

    @Query("select s.evaluacionIA, count(s), " +
           "sum(case when s.evaluacionFinal <> s.evaluacionIA then 1 else 0 end) " +
           "from Seguimiento s where s.evaluacionFinal is not null and s.evaluacionIA is not null " +
           "group by s.evaluacionIA")
    List<Object[]> overridesPorEvaluacionIA();
}
