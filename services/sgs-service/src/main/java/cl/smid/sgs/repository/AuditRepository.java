package cl.smid.sgs.repository;

import cl.smid.sgs.entity.SgsAnalisisAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditRepository extends JpaRepository<SgsAnalisisAudit, Long> {}
