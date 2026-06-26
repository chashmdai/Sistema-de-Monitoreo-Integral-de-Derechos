package cl.smid.sgs.repository;

import cl.smid.sgs.entity.JobEstado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRepository extends JpaRepository<JobEstado, String> {}
