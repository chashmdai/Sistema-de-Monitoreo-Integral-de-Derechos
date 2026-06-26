package cl.smid.sgs.service;

import cl.smid.sgs.dto.out.CatalogoDTO;
import cl.smid.sgs.enums.TipoCatalogo;
import cl.smid.sgs.mapper.SgsMapper;
import cl.smid.sgs.repository.CatalogoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CatalogoService {

    private final CatalogoRepository repo;
    private final SgsMapper mapper;

    public CatalogoService(CatalogoRepository repo, SgsMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public List<CatalogoDTO> listar(TipoCatalogo tipo) {
        return repo.findByTipoAndActivoTrueOrderByEtiquetaAsc(tipo).stream().map(mapper::toCatalogo).toList();
    }
}
