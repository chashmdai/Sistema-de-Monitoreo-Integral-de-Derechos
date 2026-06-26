package cl.smid.smidservice.repository;

import cl.smid.smidservice.entity.Requerimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface RequerimientoRepository extends JpaRepository<Requerimiento, String> {

    @Query("SELECT r FROM Requerimiento r LEFT JOIN FETCH r.evaluaciones WHERE r.codigo = :codigo")
    Optional<Requerimiento> findByIdWithEvaluaciones(@Param("codigo") String codigo);
}
