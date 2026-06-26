package cl.smid.antecedentes.infraestructura.persistencia;

import cl.smid.antecedentes.dominio.modelo.AsientoHistorial;
import cl.smid.antecedentes.dominio.modelo.Criterio;
import cl.smid.antecedentes.dominio.modelo.DocumentoAsociado;
import cl.smid.antecedentes.dominio.modelo.FichaAntecedente;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Conversion entre {@link FichaAntecedente} (dominio) y {@link FichaAntecedenteEntity}. Resuelve
 * la traduccion alt_key&lt;-&gt;id de las referencias (proceso y categorias), cifra/descifra el
 * relato y proyecta las colecciones hijas. El dominio nunca ve ids internos ni texto cifrado.
 */
@Component
public class MapeadorFicha {

    private final CifradorCampos cifrador;
    private final ProcesoDdnRepository procesoRepo;
    private final CategoriaDdnRepository categoriaRepo;

    public MapeadorFicha(CifradorCampos cifrador, ProcesoDdnRepository procesoRepo,
                         CategoriaDdnRepository categoriaRepo) {
        this.cifrador = cifrador;
        this.procesoRepo = procesoRepo;
        this.categoriaRepo = categoriaRepo;
    }

    // ---------------------------------------------------------------------------------------
    // Entidad -> dominio
    // ---------------------------------------------------------------------------------------

    public FichaAntecedente aDominio(FichaAntecedenteEntity e) {
        String procesoAlt = procesoRepo.findById(e.getProcesoId())
                .map(ProcesoDdnEntity::getAltKey)
                .orElseThrow(() -> new IllegalStateException("Proceso interno inexistente: " + e.getProcesoId()));
        String categoriaPrincipalAlt = categoriaRepo.findById(e.getCategoriaPrincipalId())
                .map(CategoriaDdnEntity::getAltKey)
                .orElseThrow(() -> new IllegalStateException(
                        "Categoria interna inexistente: " + e.getCategoriaPrincipalId()));

        List<String> secundariasAlt = resolverAltKeysCategoria(e.getCategoriasSecundariasIds());

        List<Integer> derechos = e.getDerechosCdn().stream()
                .map(Short::intValue)
                .toList();

        Set<Criterio> criterios = new LinkedHashSet<>(e.getCriterios());

        List<DocumentoAsociado> documentos = e.getDocumentos().stream()
                .map(d -> new DocumentoAsociado(d.getAltKey(), d.getNombre(), d.getReferenciaExterna()))
                .toList();

        List<AsientoHistorial> historial = e.getHistorial().stream()
                .sorted(Comparator.comparing(EmbebidosFicha.HistorialEmbebido::getOcurridoEn))
                .map(h -> new AsientoHistorial(h.getTipoEvento(), h.getActorAlt(),
                        TiempoUtc.aInstante(h.getOcurridoEn()), h.getObservacion()))
                .toList();

        String relato = cifrador.descifrar(e.getRelatoCifrado());

        return new FichaAntecedente(
                e.getAltKey(), e.getFolio(), e.getEstado(), e.getUnidadAlt(), e.getSedeAlt(),
                e.getProfesionalAlt(), e.getJefaturaAlt(), procesoAlt, e.getCasoAlt(), categoriaPrincipalAlt,
                secundariasAlt, derechos, e.getDescripcion(), relato, e.getCalificacion(), criterios,
                e.getPercepcionHallazgo(), e.getHallazgoAlt(), documentos, historial,
                TiempoUtc.aInstante(e.getCreadoEn()), TiempoUtc.aInstante(e.getActualizadoEn()));
    }

    // ---------------------------------------------------------------------------------------
    // Dominio -> entidad
    // ---------------------------------------------------------------------------------------

    public FichaAntecedenteEntity aEntidadNueva(FichaAntecedente dom) {
        FichaAntecedenteEntity e = new FichaAntecedenteEntity();
        e.setAltKey(dom.altKey());
        e.setFolio(dom.folio());
        e.setUnidadAlt(dom.unidadAlt());
        e.setSedeAlt(dom.sedeAlt());
        e.setProfesionalAlt(dom.profesionalAlt());
        e.setCreadoEn(TiempoUtc.aLocal(dom.creadoEn()));
        aplicarMutables(e, dom);
        return e;
    }

    public void actualizar(FichaAntecedenteEntity e, FichaAntecedente dom) {
        aplicarMutables(e, dom);
    }

    /** Aplica los campos que pueden cambiar entre alta y edicion (incluye colecciones). */
    private void aplicarMutables(FichaAntecedenteEntity e, FichaAntecedente dom) {
        e.setEstado(dom.estado());
        e.setJefaturaAlt(dom.jefaturaAlt());
        e.setProcesoId(idProceso(dom.procesoAlt()));
        e.setCasoAlt(dom.casoAlt());
        e.setCategoriaPrincipalId(idCategoria(dom.categoriaPrincipalAlt()));
        e.setDescripcion(dom.descripcion());
        e.setRelatoCifrado(cifrador.cifrar(dom.relato()));
        e.setCalificacion(dom.calificacion());
        e.setPercepcionHallazgo(dom.percepcionHallazgo());
        e.setHallazgoAlt(dom.hallazgoAlt());
        e.setActualizadoEn(TiempoUtc.aLocal(dom.actualizadoEn()));

        reemplazar(e.getCategoriasSecundariasIds(),
                dom.categoriasSecundariasAlt().stream().map(this::idCategoria).collect(Collectors.toList()));
        reemplazar(e.getDerechosCdn(),
                dom.derechosCdn().stream().map(Integer::shortValue).collect(Collectors.toList()));
        reemplazar(e.getCriterios(), dom.criterios());

        e.getDocumentos().clear();
        for (DocumentoAsociado d : dom.documentos()) {
            EmbebidosFicha.DocumentoEmbebido emb = new EmbebidosFicha.DocumentoEmbebido();
            emb.setAltKey(d.altKey());
            emb.setNombre(d.nombre());
            emb.setReferenciaExterna(d.referenciaExterna());
            e.getDocumentos().add(emb);
        }

        e.getHistorial().clear();
        for (AsientoHistorial h : dom.historial()) {
            EmbebidosFicha.HistorialEmbebido emb = new EmbebidosFicha.HistorialEmbebido();
            emb.setTipoEvento(h.tipoEvento());
            emb.setActorAlt(h.actorAlt());
            emb.setOcurridoEn(TiempoUtc.aLocal(h.ocurridoEn()));
            emb.setObservacion(h.observacion());
            e.getHistorial().add(emb);
        }
    }

    // ---------------------------------------------------------------------------------------
    // Traduccion de referencias
    // ---------------------------------------------------------------------------------------

    private Long idProceso(String altKey) {
        return procesoRepo.findByAltKey(altKey)
                .map(ProcesoDdnEntity::getId)
                .orElseThrow(() -> new IllegalStateException("Proceso inexistente para alt_key: " + altKey));
    }

    private Long idCategoria(String altKey) {
        return categoriaRepo.findByAltKey(altKey)
                .map(CategoriaDdnEntity::getId)
                .orElseThrow(() -> new IllegalStateException("Categoria inexistente para alt_key: " + altKey));
    }

    private List<String> resolverAltKeysCategoria(Set<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        Map<Long, String> porId = categoriaRepo.findByIdIn(new ArrayList<>(ids)).stream()
                .collect(Collectors.toMap(CategoriaDdnEntity::getId, CategoriaDdnEntity::getAltKey,
                        (a, b) -> a, java.util.LinkedHashMap::new));
        List<String> altKeys = new ArrayList<>();
        for (Long id : ids) {
            String alt = porId.get(id);
            if (alt != null) {
                altKeys.add(alt);
            }
        }
        return altKeys;
    }

    private static <T> void reemplazar(Set<T> destino, Collection<T> nuevos) {
        destino.clear();
        destino.addAll(nuevos);
    }
}
