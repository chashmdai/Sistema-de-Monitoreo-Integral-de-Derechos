package cl.smid.sgs.repository;

import cl.smid.sgs.entity.Oficio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OficioRepository extends JpaRepository<Oficio, Long>, JpaSpecificationExecutor<Oficio> {

    /** REP-2: un oficio puede repetirse en distintas ingestas; retorna lista, no entidad única. */
    List<Oficio> findByNroOficio(String nroOficio);

    boolean existsByNroOficio(String nroOficio);
}
