package cl.smid.sgs.service;

import cl.smid.sgs.config.OpenAiProperties;
import cl.smid.sgs.dto.in.*;
import cl.smid.sgs.dto.out.GuardarResultadoDTO;
import cl.smid.sgs.dto.out.OficioDetalleDTO;
import cl.smid.sgs.dto.out.SgsResumenDTO;
import cl.smid.sgs.entity.*;
import cl.smid.sgs.enums.EstadoGestion;
import cl.smid.sgs.enums.PlazoRecomendacion;
import cl.smid.sgs.enums.RespuestaSiNo;
import cl.smid.sgs.enums.TipoAnalisis;
import cl.smid.sgs.exception.SgsNotFoundException;
import cl.smid.sgs.exception.SgsValidationException;
import cl.smid.sgs.mapper.SgsMapper;
import cl.smid.sgs.repository.AuditRepository;
import cl.smid.sgs.repository.CatalogoRepository;
import cl.smid.sgs.repository.OficioRepository;
import cl.smid.sgs.repository.RecomendacionRepository;
import cl.smid.sgs.repository.SeguimientoRepository;
import cl.smid.sgs.repository.spec.RecomendacionSpecifications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/** Casos de uso sobre Oficio/Recomendacion. El controller orquesta, este servicio aplica reglas (CTRL-3). */
@Service
public class OficioService {

    private final OficioRepository oficioRepo;
    private final RecomendacionRepository recomendacionRepo;
    private final SeguimientoRepository seguimientoRepo;
    private final CatalogoRepository catalogoRepo;
    private final AuditRepository auditRepo;
    private final SgsMapper mapper;
    private final CostEstimator costEstimator;
    private final OpenAiProperties openAiProps;

    public OficioService(OficioRepository oficioRepo, RecomendacionRepository recomendacionRepo,
                         SeguimientoRepository seguimientoRepo, CatalogoRepository catalogoRepo,
                         AuditRepository auditRepo, SgsMapper mapper, CostEstimator costEstimator,
                         OpenAiProperties openAiProps) {
        this.oficioRepo = oficioRepo;
        this.recomendacionRepo = recomendacionRepo;
        this.seguimientoRepo = seguimientoRepo;
        this.catalogoRepo = catalogoRepo;
        this.auditRepo = auditRepo;
        this.mapper = mapper;
        this.costEstimator = costEstimator;
        this.openAiProps = openAiProps;
    }

    @Transactional
    public GuardarResultadoDTO guardar(OficioCreateDTO dto, String rut) {
        boolean duplicado = dto.nroOficio() != null && oficioRepo.existsByNroOficio(dto.nroOficio());

        Oficio oficio = new Oficio();
        oficio.setNroOficio(dto.nroOficio());
        oficio.setRegion(dto.region());
        oficio.setInstitucion(dto.institucion());
        oficio.setResidenciaCentro(dto.residenciaCentro());
        oficio.setNivel(dto.nivel());
        oficio.setPdfHash(dto.pdfHash());

        for (RecomendacionCreateDTO rc : dto.recomendaciones()) {
            Recomendacion r = new Recomendacion();
            r.setCorrelativo(rc.correlativo());
            r.setDimension(rc.dimension());
            r.setNudoCritico(rc.nudoCritico());
            r.setTipoRecomendacion(rc.tipoRecomendacion());
            r.setVerbo(rc.verbo());
            r.setDescripcion(rc.descripcion());
            r.setPlazo(rc.plazo());
            r.setPlazoRaw(rc.plazoRaw());
            r.setGv(rc.gv());
            r.setAcoge(rc.acoge());
            r.setMateria(resolveCatalogo(rc.materiaId()));
            r.setCategoria(resolveCatalogo(rc.categoriaId()));
            r.setProfesionalResponsable(rc.profesionalResponsable());
            r.setResponsableSeguimiento(rc.responsableSeguimiento());
            // acoge=NO desvía a la rama de no-acogida (decisión #4 negocio)
            r.setEstado(rc.acoge() == RespuestaSiNo.NO ? EstadoGestion.NO_ACOGIDA : EstadoGestion.PENDIENTE_REGISTRO);
            if (rc.acciones() != null) {
                int orden = 1;
                for (AccionDTO a : rc.acciones()) {
                    Accion accion = new Accion();
                    accion.setOrden(a.orden() > 0 ? a.orden() : orden);
                    accion.setDescripcion(a.descripcion());
                    r.addAccion(accion);
                    orden++;
                }
            }
            oficio.addRecomendacion(r);
        }

        Oficio guardado = oficioRepo.save(oficio);
        escribirAuditExtraccion(guardado, dto.auditMeta(), rut);
        return new GuardarResultadoDTO(guardado.getId(), guardado.getRecomendaciones().size(), duplicado);
    }

    private void escribirAuditExtraccion(Oficio oficio, AuditMetaDTO meta, String rut) {
        List<DocumentoHash> docs = new ArrayList<>();
        if (meta != null && meta.documentos() != null) {
            meta.documentos().forEach(d -> docs.add(new DocumentoHash(d.nombreArchivo(), d.sha256())));
        }
        if (docs.isEmpty() && oficio.getPdfHash() != null) {
            docs.add(new DocumentoHash(oficio.getNroOficio(), oficio.getPdfHash()));
        }
        auditRepo.save(SgsAnalisisAudit.builder()
                .oficioId(oficio.getId())
                .usuarioRut(rut)
                .timestamp(LocalDateTime.now())
                .tipoAnalisis(TipoAnalisis.EXTRACCION)
                .modelo(meta != null ? meta.modelo() : openAiProps.getModelExtraccion())
                .modeloSnapshot(meta != null ? meta.modeloSnapshot() : openAiProps.getModelExtraccion())
                .versionRubrica(openAiProps.getRubricaVersion())
                .documentos(docs)
                .tokensPrompt(meta != null ? meta.tokensPrompt() : null)
                .tokensCompletion(meta != null ? meta.tokensCompletion() : null)
                .costoEstimado(meta != null ? costEstimator.estimar(meta.tokensPrompt(), meta.tokensCompletion()) : null)
                .build());
    }

    @Transactional(readOnly = true)
    public Page<SgsResumenDTO> tablero(EstadoGestion estado, PlazoRecomendacion plazo, Boolean gv,
                                       String region, String institucion, String dimension,
                                       String nroOficio, String responsable, Pageable pageable) {
        Specification<Recomendacion> spec = Specification.allOf(
                RecomendacionSpecifications.noAnuladas(),
                RecomendacionSpecifications.estado(estado),
                RecomendacionSpecifications.plazo(plazo),
                RecomendacionSpecifications.gv(gv),
                RecomendacionSpecifications.region(region),
                RecomendacionSpecifications.institucion(institucion),
                RecomendacionSpecifications.dimension(dimension),
                RecomendacionSpecifications.nroOficio(nroOficio),
                RecomendacionSpecifications.responsable(responsable));

        Page<Recomendacion> page = recomendacionRepo.findAll(spec, pageable);
        List<Long> ids = page.getContent().stream().map(Recomendacion::getId).toList();
        Map<Long, Seguimiento> ultimos = ids.isEmpty() ? Map.of()
                : seguimientoRepo.findUltimosPorRecomendacion(ids).stream()
                    .collect(Collectors.toMap(s -> s.getRecomendacion().getId(), Function.identity(), (a, b) -> a));

        List<SgsResumenDTO> content = page.getContent().stream()
                .map(r -> mapper.toResumen(r, ultimos.get(r.getId())))
                .toList();
        return new PageImpl<>(content, pageable, page.getTotalElements());
    }

    @Transactional(readOnly = true)
    public OficioDetalleDTO detalle(Long id) {
        Oficio o = oficioRepo.findById(id)
                .orElseThrow(() -> new SgsNotFoundException("Oficio inexistente: " + id));
        return mapper.toOficioDetalle(o);
    }

    @Transactional
    public void patch(Long recomendacionId, RecomendacionPatchDTO dto) {
        Recomendacion r = cargarRecomendacion(recomendacionId);
        if (dto.acoge() != null) r.setAcoge(dto.acoge());
        if (dto.materiaId() != null) r.setMateria(resolveCatalogo(dto.materiaId()));
        if (dto.categoriaId() != null) r.setCategoria(resolveCatalogo(dto.categoriaId()));
        if (dto.profesionalResponsable() != null) r.setProfesionalResponsable(dto.profesionalResponsable());
        if (dto.responsableSeguimiento() != null) r.setResponsableSeguimiento(dto.responsableSeguimiento());
        recomendacionRepo.save(r);
    }

    @Transactional
    public void cambiarEstado(Long recomendacionId, CambioEstadoDTO dto, String rut) {
        Recomendacion r = cargarRecomendacion(recomendacionId);
        if (dto.nuevoEstado() == EstadoGestion.ANULADA) {
            throw new SgsValidationException("Use el endpoint de anulación (DELETE) para anular.");
        }
        if (!r.getEstado().puedeTransicionarA(dto.nuevoEstado())) {
            throw new SgsValidationException(
                    "Transición inválida: " + r.getEstado() + " -> " + dto.nuevoEstado());
        }
        r.setEstado(dto.nuevoEstado());
        recomendacionRepo.save(r);
    }

    @Transactional
    public void anular(Long recomendacionId, AnulacionDTO dto, String rut) {
        Recomendacion r = cargarRecomendacion(recomendacionId);
        r.setAnulado(true);
        r.setMotivoAnulacion(dto.motivo());
        r.setAnuladoPor(rut);
        r.setEstado(EstadoGestion.ANULADA);
        recomendacionRepo.save(r);
    }

    private Recomendacion cargarRecomendacion(Long id) {
        return recomendacionRepo.findById(id)
                .orElseThrow(() -> new SgsNotFoundException("Recomendación inexistente: " + id));
    }

    private Catalogo resolveCatalogo(Long id) {
        if (id == null) return null;
        return catalogoRepo.findById(id)
                .orElseThrow(() -> new SgsValidationException("Catálogo inexistente: " + id));
    }
}
