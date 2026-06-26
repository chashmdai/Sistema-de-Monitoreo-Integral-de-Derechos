package cl.smid.antecedentes.infraestructura.persistencia;

import cl.smid.antecedentes.dominio.modelo.CriterioTerritorial;
import cl.smid.antecedentes.dominio.modelo.FichaAntecedente;
import cl.smid.antecedentes.dominio.modelo.FiltroFichas;
import cl.smid.antecedentes.dominio.modelo.Pagina;
import cl.smid.antecedentes.dominio.modelo.ResumenFicha;
import cl.smid.antecedentes.dominio.puerto.salida.RepositorioFichas;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Adaptador de persistencia del agregado {@link FichaAntecedente}. Traduce el filtro de proceso
 * (alt_key -&gt; id) y materializa la acotacion territorial: si el alcance exige sede/unidad y el
 * claim no la trae, el listado queda vacio (no se filtra "todo" por error).
 */
@Component
public class AdaptadorRepositorioFichas implements RepositorioFichas {

    private final FichaRepository fichaRepo;
    private final ProcesoDdnRepository procesoRepo;
    private final MapeadorFicha mapeador;

    public AdaptadorRepositorioFichas(FichaRepository fichaRepo, ProcesoDdnRepository procesoRepo,
                                      MapeadorFicha mapeador) {
        this.fichaRepo = fichaRepo;
        this.procesoRepo = procesoRepo;
        this.mapeador = mapeador;
    }

    @Override
    public FichaAntecedente guardar(FichaAntecedente ficha) {
        FichaAntecedenteEntity entidad = fichaRepo.findByAltKey(ficha.altKey())
                .map(existente -> {
                    mapeador.actualizar(existente, ficha);
                    return existente;
                })
                .orElseGet(() -> mapeador.aEntidadNueva(ficha));
        FichaAntecedenteEntity guardada = fichaRepo.save(entidad);
        return mapeador.aDominio(guardada);
    }

    @Override
    public Optional<FichaAntecedente> buscarPorAltKey(String altKey) {
        return fichaRepo.findByAltKey(altKey).map(mapeador::aDominio);
    }

    @Override
    public void eliminarPorAltKey(String altKey) {
        fichaRepo.deleteByAltKey(altKey);
    }

    @Override
    public Pagina<ResumenFicha> buscar(FiltroFichas filtro, CriterioTerritorial territorio) {
        // Filtro por proceso: traducir alt_key -> id; si no resuelve, no hay coincidencias.
        Long procesoId = null;
        if (filtro.procesoAlt() != null && !filtro.procesoAlt().isBlank()) {
            Optional<Long> id = procesoRepo.findByAltKey(filtro.procesoAlt()).map(ProcesoDdnEntity::getId);
            if (id.isEmpty()) {
                return new Pagina<>(List.of(), filtro.pagina(), filtro.tamano(), 0);
            }
            procesoId = id.get();
        }

        // Acotacion territorial.
        String sedeAlt = null;
        String unidadAlt = null;
        switch (territorio.alcance()) {
            case NACIONAL -> {
                // Sin filtro territorial.
            }
            case SEDE -> {
                if (vacio(territorio.sedeAlt())) {
                    return new Pagina<>(List.of(), filtro.pagina(), filtro.tamano(), 0);
                }
                sedeAlt = territorio.sedeAlt();
            }
            case UNIDAD -> {
                if (vacio(territorio.unidadAlt())) {
                    return new Pagina<>(List.of(), filtro.pagina(), filtro.tamano(), 0);
                }
                unidadAlt = territorio.unidadAlt();
            }
        }

        String texto = (filtro.texto() == null || filtro.texto().isBlank()) ? null : filtro.texto().trim();
        Pageable pageable = PageRequest.of(filtro.pagina(), filtro.tamano());
        Page<FichaResumenRow> page = fichaRepo.buscar(filtro.estado(), filtro.calificacion(), filtro.casoAlt(),
                procesoId, texto, sedeAlt, unidadAlt, pageable);

        List<ResumenFicha> contenido = page.getContent().stream()
                .map(r -> new ResumenFicha(r.altKey(), r.folio(), r.estado(), r.calificacion(),
                        r.percepcionHallazgo(), r.unidadAlt(), r.sedeAlt(), r.casoAlt(),
                        TiempoUtc.aInstante(r.creadoEn()), TiempoUtc.aInstante(r.actualizadoEn())))
                .toList();
        return new Pagina<>(contenido, page.getNumber(), page.getSize(), page.getTotalElements());
    }

    private static boolean vacio(String valor) {
        return valor == null || valor.isBlank();
    }
}
