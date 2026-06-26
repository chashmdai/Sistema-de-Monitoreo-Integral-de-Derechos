package cl.smid.sgs.service;

import cl.smid.sgs.config.OpenAiProperties;
import cl.smid.sgs.dto.in.AplicarEvaluacionRequest;
import cl.smid.sgs.dto.in.AuditMetaDTO;
import cl.smid.sgs.dto.in.SeguimientoAplicarDTO;
import cl.smid.sgs.entity.*;
import cl.smid.sgs.enums.EstadoGestion;
import cl.smid.sgs.enums.TipoAnalisis;
import cl.smid.sgs.exception.SgsNotFoundException;
import cl.smid.sgs.exception.SgsValidationException;
import cl.smid.sgs.repository.AuditRepository;
import cl.smid.sgs.repository.CatalogoRepository;
import cl.smid.sgs.repository.RecomendacionRepository;
import cl.smid.sgs.repository.SeguimientoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/** Aplica las evaluaciones confirmadas creando hitos de Seguimiento. Aquí se materializa el desdoble IA/humano (MOD-2). */
@Service
public class SeguimientoService {

    private final RecomendacionRepository recomendacionRepo;
    private final SeguimientoRepository seguimientoRepo;
    private final CatalogoRepository catalogoRepo;
    private final AuditRepository auditRepo;
    private final CostEstimator costEstimator;
    private final OpenAiProperties openAiProps;

    public SeguimientoService(RecomendacionRepository recomendacionRepo, SeguimientoRepository seguimientoRepo,
                              CatalogoRepository catalogoRepo, AuditRepository auditRepo,
                              CostEstimator costEstimator, OpenAiProperties openAiProps) {
        this.recomendacionRepo = recomendacionRepo;
        this.seguimientoRepo = seguimientoRepo;
        this.catalogoRepo = catalogoRepo;
        this.auditRepo = auditRepo;
        this.costEstimator = costEstimator;
        this.openAiProps = openAiProps;
    }

    @Transactional
    public int aplicar(AplicarEvaluacionRequest req, String rut) {
        List<Seguimiento> creados = new ArrayList<>();
        for (SeguimientoAplicarDTO item : req.items()) {
            Recomendacion r = recomendacionRepo.findById(item.recomendacionId())
                    .orElseThrow(() -> new SgsNotFoundException("Recomendación inexistente: " + item.recomendacionId()));

            Seguimiento s = new Seguimiento();
            s.setFase(item.fase());
            s.setFechaSeguimiento(item.fechaSeguimiento());
            s.setTipoSeguimiento(resolveCatalogo(item.tipoSeguimientoId()));
            s.setTipoRespuesta(resolveCatalogo(item.tipoRespuestaId()));
            s.setFechaRespuesta(item.fechaRespuesta());
            s.setOtroSeguimientoInstitucional(item.otroSeguimientoInstitucional());
            // propuesta IA (inmutable)
            s.setEvaluacionIA(item.evaluacionIA());
            s.setValoracionRubrica(item.valoracionRubrica());
            s.setConfianza(item.confianza());
            s.setRazonamiento(item.razonamiento());
            s.setRequiereRevisionHumana(item.confianza() != null && item.confianza() < 0.60);
            // override humano
            s.setEvaluacionFinal(item.evaluacionFinal());
            s.setEvaluacionFinalAutor(rut);
            s.setEvaluacionFinalFecha(LocalDateTime.now());
            s.setResponsableSeguimiento(item.responsableSeguimiento());

            r.addSeguimiento(s);
            // avanzar la gestión al primer seguimiento si estaba pendiente de registro
            if (r.getEstado() == EstadoGestion.PENDIENTE_REGISTRO) {
                r.setEstado(EstadoGestion.EN_SEGUIMIENTO);
            }
            creados.add(s);
        }
        seguimientoRepo.saveAll(creados);
        escribirAuditEvaluacion(req, rut);
        return creados.size();
    }

    private void escribirAuditEvaluacion(AplicarEvaluacionRequest req, String rut) {
        AuditMetaDTO meta = req.auditMeta();
        List<DocumentoHash> docs = new ArrayList<>();
        if (meta != null && meta.documentos() != null) {
            meta.documentos().forEach(d -> docs.add(new DocumentoHash(d.nombreArchivo(), d.sha256())));
        }
        Long oficioId = req.items().isEmpty() ? null
                : recomendacionRepo.findById(req.items().get(0).recomendacionId())
                    .map(r -> r.getOficio().getId()).orElse(null);
        auditRepo.save(SgsAnalisisAudit.builder()
                .oficioId(oficioId)
                .usuarioRut(rut)
                .timestamp(LocalDateTime.now())
                .tipoAnalisis(TipoAnalisis.EVALUACION)
                .modelo(meta != null ? meta.modelo() : openAiProps.getModelEvaluacion())
                .modeloSnapshot(meta != null ? meta.modeloSnapshot() : openAiProps.getModelEvaluacion())
                .versionRubrica(openAiProps.getRubricaVersion())
                .documentos(docs)
                .tokensPrompt(meta != null ? meta.tokensPrompt() : null)
                .tokensCompletion(meta != null ? meta.tokensCompletion() : null)
                .tokensReasoning(meta != null ? meta.tokensReasoning() : null)
                .costoEstimado(meta != null ? costEstimator.estimar(meta.tokensPrompt(), meta.tokensCompletion()) : null)
                .build());
    }

    private Catalogo resolveCatalogo(Long id) {
        if (id == null) return null;
        return catalogoRepo.findById(id)
                .orElseThrow(() -> new SgsValidationException("Catálogo inexistente: " + id));
    }
}
