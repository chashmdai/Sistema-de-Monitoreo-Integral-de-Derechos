package cl.smid.antecedentes.infraestructura.persistencia;

import cl.smid.antecedentes.dominio.modelo.FiltroHallazgos;
import cl.smid.antecedentes.dominio.modelo.Hallazgo;
import cl.smid.antecedentes.dominio.modelo.Pagina;
import cl.smid.antecedentes.dominio.puerto.salida.RepositorioHallazgos;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Adaptador de persistencia del agregado {@link Hallazgo}. En el listado precomputa el mapa de
 * traduccion de instrumentos (id-&gt;alt_key) de la pagina para evitar N+1.
 */
@Component
public class AdaptadorRepositorioHallazgos implements RepositorioHallazgos {

    private final HallazgoRepository hallazgoRepo;
    private final InstrumentoRepository instrumentoRepo;
    private final MapeadorHallazgo mapeador;

    public AdaptadorRepositorioHallazgos(HallazgoRepository hallazgoRepo, InstrumentoRepository instrumentoRepo,
                                         MapeadorHallazgo mapeador) {
        this.hallazgoRepo = hallazgoRepo;
        this.instrumentoRepo = instrumentoRepo;
        this.mapeador = mapeador;
    }

    @Override
    public Hallazgo guardar(Hallazgo hallazgo) {
        HallazgoEntity entidad = hallazgoRepo.findByAltKey(hallazgo.altKey())
                .map(existente -> {
                    mapeador.actualizar(existente, hallazgo);
                    return existente;
                })
                .orElseGet(() -> mapeador.aEntidadNueva(hallazgo));
        return mapeador.aDominio(hallazgoRepo.save(entidad));
    }

    @Override
    public Optional<Hallazgo> buscarPorAltKey(String altKey) {
        return hallazgoRepo.findByAltKey(altKey).map(mapeador::aDominio);
    }

    @Override
    public boolean existePorAltKey(String altKey) {
        return altKey != null && !altKey.isBlank() && hallazgoRepo.existsByAltKey(altKey);
    }

    @Override
    public Pagina<Hallazgo> buscar(FiltroHallazgos filtro) {
        String texto = (filtro.texto() == null || filtro.texto().isBlank()) ? null : filtro.texto().trim();
        Pageable pageable = PageRequest.of(filtro.pagina(), filtro.tamano());
        Page<HallazgoEntity> page = hallazgoRepo.buscar(filtro.estado(), filtro.temporalidad(), texto, pageable);

        List<Long> instrumentoIds = page.getContent().stream()
                .map(HallazgoEntity::getInstrumentoId)
                .filter(java.util.Objects::nonNull)
                .distinct()
                .toList();
        Map<Long, String> instrumentoAltPorId = instrumentoIds.isEmpty() ? Map.of()
                : instrumentoRepo.findByIdIn(instrumentoIds).stream()
                        .collect(Collectors.toMap(InstrumentoEntity::getId, InstrumentoEntity::getAltKey));

        List<Hallazgo> contenido = page.getContent().stream()
                .map(e -> mapeador.aDominio(e, instrumentoAltPorId))
                .toList();
        return new Pagina<>(contenido, page.getNumber(), page.getSize(), page.getTotalElements());
    }
}
