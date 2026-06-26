package cl.smid.antecedentes.infraestructura.persistencia;

import cl.smid.antecedentes.dominio.modelo.Pagina;
import cl.smid.antecedentes.dominio.modelo.Referencia;
import cl.smid.antecedentes.dominio.modelo.TipoReferencia;
import cl.smid.antecedentes.dominio.puerto.salida.RepositorioReferencias;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Adaptador de persistencia para las tres tablas de referencia. Encapsula el ruteo por
 * {@link TipoReferencia} hacia el repositorio Spring Data correspondiente.
 */
@Component
public class AdaptadorRepositorioReferencias implements RepositorioReferencias {

    private final CategoriaDdnRepository categoriaRepo;
    private final ProcesoDdnRepository procesoRepo;
    private final InstrumentoRepository instrumentoRepo;
    private final MapeadorReferencia mapeador;

    public AdaptadorRepositorioReferencias(CategoriaDdnRepository categoriaRepo, ProcesoDdnRepository procesoRepo,
                                           InstrumentoRepository instrumentoRepo, MapeadorReferencia mapeador) {
        this.categoriaRepo = categoriaRepo;
        this.procesoRepo = procesoRepo;
        this.instrumentoRepo = instrumentoRepo;
        this.mapeador = mapeador;
    }

    @Override
    public Referencia guardar(Referencia referencia) {
        TipoReferencia tipo = referencia.tipo();
        return switch (tipo) {
            case CATEGORIA -> {
                CategoriaDdnEntity e = categoriaRepo.findByAltKey(referencia.altKey()).orElseGet(() -> {
                    CategoriaDdnEntity nueva = new CategoriaDdnEntity();
                    mapeador.poblarNueva(nueva, referencia);
                    return nueva;
                });
                if (e.getId() != null) {
                    mapeador.actualizar(e, referencia);
                }
                yield mapeador.aDominio(categoriaRepo.save(e), tipo);
            }
            case PROCESO -> {
                ProcesoDdnEntity e = procesoRepo.findByAltKey(referencia.altKey()).orElseGet(() -> {
                    ProcesoDdnEntity nueva = new ProcesoDdnEntity();
                    mapeador.poblarNueva(nueva, referencia);
                    return nueva;
                });
                if (e.getId() != null) {
                    mapeador.actualizar(e, referencia);
                }
                yield mapeador.aDominio(procesoRepo.save(e), tipo);
            }
            case INSTRUMENTO -> {
                InstrumentoEntity e = instrumentoRepo.findByAltKey(referencia.altKey()).orElseGet(() -> {
                    InstrumentoEntity nueva = new InstrumentoEntity();
                    mapeador.poblarNueva(nueva, referencia);
                    return nueva;
                });
                if (e.getId() != null) {
                    mapeador.actualizar(e, referencia);
                }
                yield mapeador.aDominio(instrumentoRepo.save(e), tipo);
            }
        };
    }

    @Override
    public Optional<Referencia> buscarPorAltKey(TipoReferencia tipo, String altKey) {
        return switch (tipo) {
            case CATEGORIA -> categoriaRepo.findByAltKey(altKey).map(e -> mapeador.aDominio(e, tipo));
            case PROCESO -> procesoRepo.findByAltKey(altKey).map(e -> mapeador.aDominio(e, tipo));
            case INSTRUMENTO -> instrumentoRepo.findByAltKey(altKey).map(e -> mapeador.aDominio(e, tipo));
        };
    }

    @Override
    public boolean existePorCodigo(TipoReferencia tipo, String codigo) {
        return switch (tipo) {
            case CATEGORIA -> categoriaRepo.existsByCodigo(codigo);
            case PROCESO -> procesoRepo.existsByCodigo(codigo);
            case INSTRUMENTO -> instrumentoRepo.existsByCodigo(codigo);
        };
    }

    @Override
    public boolean existeVigentePorAltKey(TipoReferencia tipo, String altKey) {
        if (altKey == null || altKey.isBlank()) {
            return false;
        }
        return switch (tipo) {
            case CATEGORIA -> categoriaRepo.existsByAltKeyAndVigenteTrue(altKey);
            case PROCESO -> procesoRepo.existsByAltKeyAndVigenteTrue(altKey);
            case INSTRUMENTO -> instrumentoRepo.existsByAltKeyAndVigenteTrue(altKey);
        };
    }

    @Override
    public Pagina<Referencia> listar(TipoReferencia tipo, String texto, Boolean vigente, int pagina, int tamano) {
        Pageable pageable = PageRequest.of(Math.max(pagina, 0), tamano <= 0 ? 20 : Math.min(tamano, 200));
        String filtro = (texto == null || texto.isBlank()) ? null : texto.trim();
        Page<? extends ReferenciaEntityBase> page = switch (tipo) {
            case CATEGORIA -> categoriaRepo.buscar(filtro, vigente, pageable);
            case PROCESO -> procesoRepo.buscar(filtro, vigente, pageable);
            case INSTRUMENTO -> instrumentoRepo.buscar(filtro, vigente, pageable);
        };
        List<Referencia> contenido = page.getContent().stream()
                .map(e -> mapeador.aDominio(e, tipo))
                .toList();
        return new Pagina<>(contenido, page.getNumber(), page.getSize(), page.getTotalElements());
    }
}
