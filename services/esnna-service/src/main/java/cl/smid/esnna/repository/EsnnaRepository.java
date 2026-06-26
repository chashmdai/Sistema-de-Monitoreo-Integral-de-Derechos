package cl.smid.esnna.repository;

import cl.smid.esnna.entity.EsnnaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * Repositorio del caso. Los filtros del tablero se arman dinámicamente con
 * Specifications (EsnnaSpecifications), reemplazando las queries muertas del
 * modelo original (REPO-1). El listado se proyecta a DTO en el servicio.
 */
public interface EsnnaRepository extends JpaRepository<EsnnaEntity, Long>,
        JpaSpecificationExecutor<EsnnaEntity> {

    /**
     * Posibles duplicados (no anulados) por nro de oficio, RUC o cédula del NNA.
     * Alimenta la advertencia de duplicado al guardar (FEAT higiene operacional).
     */
    @Query("""
            select c from EsnnaEntity c
            where c.anulado = false and (
                (:nroOficio is not null and c.nroOficio = :nroOficio) or
                (:ruc is not null and c.rucAsociados = :ruc) or
                (:cedula is not null and c.cedulaNna = :cedula)
            )
            """)
    List<EsnnaEntity> buscarPosiblesDuplicados(@Param("nroOficio") String nroOficio,
                                               @Param("ruc") String ruc,
                                               @Param("cedula") String cedula);

    // ---- Métricas de concordancia (decisión #12) ----

    @Query("select count(c) from EsnnaEntity c where c.anulado = false and c.semaforoFinal is not null")
    long countConSemaforoFinal();

    @Query("""
            select count(c) from EsnnaEntity c
            where c.anulado = false and c.semaforoFinal is not null
              and c.semaforoFinal <> c.semaforoIa
            """)
    long countOverrides();

    @Query("""
            select c.semaforoIa, c.semaforoFinal, count(c) from EsnnaEntity c
            where c.anulado = false and c.semaforoFinal is not null
            group by c.semaforoIa, c.semaforoFinal
            """)
    List<Object[]> matrizConfusionRaw();

    /**
     * Export: trae imputados con fetch join para evitar N+1 al aplanar a Excel.
     * Filtro opcional por semaforoFinal; excluye anulados.
     */
    @Query("""
            select distinct c from EsnnaEntity c
            left join fetch c.imputados
            where c.anulado = false and (:semaforo is null or c.semaforoFinal = :semaforo)
            order by c.fechaIngreso desc
            """)
    List<EsnnaEntity> findParaExport(@Param("semaforo") cl.smid.esnna.domain.Semaforo semaforo);
}
