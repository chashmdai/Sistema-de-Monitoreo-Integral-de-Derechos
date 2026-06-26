package cl.smid.sgs.repository;

import cl.smid.sgs.entity.Catalogo;
import cl.smid.sgs.enums.TipoCatalogo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CatalogoRepository extends JpaRepository<Catalogo, Long> {
    List<Catalogo> findByTipoAndActivoTrueOrderByEtiquetaAsc(TipoCatalogo tipo);
    Optional<Catalogo> findByTipoAndCodigo(TipoCatalogo tipo, String codigo);
}
