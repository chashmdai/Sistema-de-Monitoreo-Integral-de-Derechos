package cl.smid.smidservice.repository;

import cl.smid.smidservice.entity.TramiteLegislativo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TramiteLegislativoRepository extends JpaRepository<TramiteLegislativo, Long> {

    List<TramiteLegislativo> findByLeyIdOrderByFechaTramiteAsc(Integer leyId);
}
