package cl.smid.antecedentes.infraestructura.persistencia;

import cl.smid.antecedentes.dominio.modelo.Hallazgo;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

/**
 * Conversion entre {@link Hallazgo} (dominio) y {@link HallazgoEntity}. Traduce el instrumento
 * (alt_key&lt;-&gt;id). Para listados expone una variante con un mapa de traduccion precomputado
 * (id-&gt;alt_key) que evita N+1.
 */
@Component
public class MapeadorHallazgo {

    private final InstrumentoRepository instrumentoRepo;

    public MapeadorHallazgo(InstrumentoRepository instrumentoRepo) {
        this.instrumentoRepo = instrumentoRepo;
    }

    /** Entidad -> dominio resolviendo el instrumento con una consulta puntual. */
    public Hallazgo aDominio(HallazgoEntity e) {
        String instrumentoAlt = e.getInstrumentoId() == null ? null
                : instrumentoRepo.findById(e.getInstrumentoId())
                        .map(InstrumentoEntity::getAltKey)
                        .orElse(null);
        return construir(e, instrumentoAlt);
    }

    /** Entidad -> dominio usando un mapa de traduccion id-&gt;alt_key precomputado (sin N+1). */
    public Hallazgo aDominio(HallazgoEntity e, Map<Long, String> instrumentoAltPorId) {
        String instrumentoAlt = e.getInstrumentoId() == null ? null
                : instrumentoAltPorId.get(e.getInstrumentoId());
        return construir(e, instrumentoAlt);
    }

    private Hallazgo construir(HallazgoEntity e, String instrumentoAlt) {
        List<String> unidades = new ArrayList<>(e.getUnidadesInvolucradas());
        List<String> instituciones = new ArrayList<>(e.getInstitucionesExternas());
        return new Hallazgo(e.getAltKey(), e.getFolio(), e.getTitulo(), e.getDescripcion(), e.getEstado(),
                instrumentoAlt, e.getTemporalidad(), unidades, instituciones, e.getOrigenFichaAlt(),
                TiempoUtc.aInstante(e.getCreadoEn()), TiempoUtc.aInstante(e.getActualizadoEn()));
    }

    public HallazgoEntity aEntidadNueva(Hallazgo dom) {
        HallazgoEntity e = new HallazgoEntity();
        e.setAltKey(dom.altKey());
        e.setFolio(dom.folio());
        e.setOrigenFichaAlt(dom.origenFichaAlt());
        e.setCreadoEn(TiempoUtc.aLocal(dom.creadoEn()));
        aplicarMutables(e, dom);
        return e;
    }

    public void actualizar(HallazgoEntity e, Hallazgo dom) {
        aplicarMutables(e, dom);
    }

    private void aplicarMutables(HallazgoEntity e, Hallazgo dom) {
        e.setTitulo(dom.titulo());
        e.setDescripcion(dom.descripcion());
        e.setEstado(dom.estado());
        e.setInstrumentoId(idInstrumento(dom.instrumentoAlt()));
        e.setTemporalidad(dom.temporalidad());
        e.setActualizadoEn(TiempoUtc.aLocal(dom.actualizadoEn()));

        e.getUnidadesInvolucradas().clear();
        e.getUnidadesInvolucradas().addAll(new LinkedHashSet<>(dom.unidadesInvolucradas()));
        e.getInstitucionesExternas().clear();
        e.getInstitucionesExternas().addAll(new LinkedHashSet<>(dom.institucionesExternas()));
    }

    private Long idInstrumento(String altKey) {
        if (altKey == null || altKey.isBlank()) {
            return null;
        }
        return instrumentoRepo.findByAltKey(altKey)
                .map(InstrumentoEntity::getId)
                .orElseThrow(() -> new IllegalStateException("Instrumento inexistente para alt_key: " + altKey));
    }
}
