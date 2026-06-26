package cl.smid.esnna.service;

import cl.smid.esnna.domain.EstadoGestion;
import cl.smid.esnna.domain.Semaforo;
import cl.smid.esnna.dto.*;
import cl.smid.esnna.entity.EsnnaAnalisisAudit;
import cl.smid.esnna.entity.EsnnaEntity;
import cl.smid.esnna.exception.EsnnaValidationException;
import cl.smid.esnna.exception.RecursoNoEncontradoException;
import cl.smid.esnna.exception.TransicionInvalidaException;
import cl.smid.esnna.mapper.EsnnaMapper;
import cl.smid.esnna.repository.EsnnaAnalisisAuditRepository;
import cl.smid.esnna.repository.EsnnaRepository;
import cl.smid.esnna.repository.EsnnaSpecifications;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Gestión del ciclo de vida del caso: listado con filtros, detalle, guardado con
 * auditoría, edición, transición de estado (con borrado lógico) y métricas de
 * concordancia. El controller no toca repositorios ni entidades directamente.
 */
@Service
public class EsnnaCasoService {

    private static final Logger log = LoggerFactory.getLogger(EsnnaCasoService.class);

    private final EsnnaRepository repository;
    private final EsnnaAnalisisAuditRepository auditRepository;
    private final EsnnaMapper mapper;
    private final DraftStore draftStore;
    private final InformeService informeService;
    private final DocumentoStorageService storageService;
    private final ExcelExportService excelExportService;

    public EsnnaCasoService(EsnnaRepository repository,
                            EsnnaAnalisisAuditRepository auditRepository,
                            EsnnaMapper mapper,
                            DraftStore draftStore,
                            InformeService informeService,
                            DocumentoStorageService storageService,
                            ExcelExportService excelExportService) {
        this.repository = repository;
        this.auditRepository = auditRepository;
        this.mapper = mapper;
        this.draftStore = draftStore;
        this.informeService = informeService;
        this.storageService = storageService;
        this.excelExportService = excelExportService;
    }

    // ===================== Listado =====================

    @Transactional(readOnly = true)
    public Page<EsnnaResumenDTO> listar(Semaforo semaforoFinal, EstadoGestion estado, String region,
                                        String delito, String ruc, String cedulaNna, String rutImputado,
                                        LocalDateTime desde, LocalDateTime hasta, Pageable pageable) {
        Specification<EsnnaEntity> spec = Specification.allOf(
                EsnnaSpecifications.noAnulado(),
                EsnnaSpecifications.conSemaforoFinal(semaforoFinal),
                EsnnaSpecifications.conEstado(estado),
                EsnnaSpecifications.conRegion(region),
                EsnnaSpecifications.conDelito(delito),
                EsnnaSpecifications.conRuc(ruc),
                EsnnaSpecifications.conCedulaNna(cedulaNna),
                EsnnaSpecifications.conRutImputado(rutImputado),
                EsnnaSpecifications.ingresadoDesde(desde),
                EsnnaSpecifications.ingresadoHasta(hasta)
        );
        return repository.findAll(spec, pageable).map(mapper::toResumen);
    }

    // ===================== Detalle =====================

    @Transactional(readOnly = true)
    public EsnnaDetalleDTO obtenerDetalle(Long id) {
        return mapper.toDetalle(buscar(id));
    }

    // ===================== Guardar (con auditoría) =====================

    @Transactional
    public GuardarCasoResultado guardar(EsnnaCasoCreateRequest req, String usuarioRut) {
        DraftAnalisis draft = draftStore.get(req.draftId())
                .orElseThrow(() -> new EsnnaValidationException(
                        "El análisis expiró o no existe. Vuelva a procesar los documentos antes de guardar."));

        EsnnaProcesoResultado r = draft.resultado();
        Double confianzaIa = r.consolidado() != null ? r.consolidado().confianzaAnalisis() : null;

        // Base = consolidado completo del draft; el request solo aporta overrides + semaforoFinal.
        EsnnaEntity entidad = mapper.toEntity(r.consolidado(), req, r.semaforoIa(), r.justificacionIa(), confianzaIa, usuarioRut);
        EsnnaEntity guardado = repository.save(entidad);

        // Auditoría inmutable (BIZ-3)
        UsageDTO u = r.usage();
        EsnnaAnalisisAudit audit = EsnnaAnalisisAudit.builder()
                .casoId(guardado.getId())
                .usuarioRut(usuarioRut)
                .modeloExtraccion(draft.modeloExtraccion())
                .modeloConsolidacion(draft.modeloConsolidacion())
                .versionProtocolo(draft.versionProtocolo())
                .documentos(draft.documentos())
                .semaforoIa(r.semaforoIa())
                .confianzaIa(guardado.getConfianzaIa())
                .tokensPrompt(u != null ? u.tokensPrompt() : null)
                .tokensCompletion(u != null ? u.tokensCompletion() : null)
                .tokensReasoning(u != null ? u.tokensReasoning() : null)
                .costoEstimado(u != null ? u.costoEstimado() : null)
                .build();
        auditRepository.save(audit);

        // Dedup (advertencia, no bloqueo)
        List<Long> duplicados = repository
                .buscarPosiblesDuplicados(guardado.getNroOficio(), guardado.getRucAsociados(), guardado.getCedulaNna())
                .stream().map(EsnnaEntity::getId).filter(idd -> !idd.equals(guardado.getId())).toList();
        if (!duplicados.isEmpty()) {
            log.warn("Caso {} guardado con posibles duplicados: {}", guardado.getId(), duplicados);
        }

        draftStore.invalidar(req.draftId());
        return new GuardarCasoResultado(mapper.toDetalle(guardado), duplicados);
    }

    // ===================== Editar (PATCH) =====================

    @Transactional
    public EsnnaDetalleDTO actualizar(Long id, EsnnaCasoUpdateRequest req, String usuarioRut) {
        EsnnaEntity e = buscar(id);
        if (e.isAnulado()) {
            throw new TransicionInvalidaException("No se puede editar un caso anulado.");
        }
        mapper.applyPatch(e, req, usuarioRut);
        return mapper.toDetalle(repository.save(e));
    }

    // ===================== Transición de estado =====================

    @Transactional
    public EsnnaDetalleDTO cambiarEstado(Long id, CambioEstadoRequest req, String usuarioRut) {
        EsnnaEntity e = buscar(id);
        EstadoGestion actual = e.getEstadoGestion();
        EstadoGestion destino = req.estadoDestino();

        if (!actual.puedeTransicionarA(destino)) {
            throw new TransicionInvalidaException(
                    "Transición no permitida: " + actual + " -> " + destino + ".");
        }
        if (destino == EstadoGestion.ANULADO) {
            anular(e, req.motivo(), usuarioRut);
        } else {
            e.setEstadoGestion(destino);
        }
        return mapper.toDetalle(repository.save(e));
    }

    @Transactional
    public EsnnaDetalleDTO anularCaso(Long id, String motivo, String usuarioRut) {
        EsnnaEntity e = buscar(id);
        if (!e.getEstadoGestion().puedeTransicionarA(EstadoGestion.ANULADO)) {
            throw new TransicionInvalidaException("El caso no admite anulación en su estado actual.");
        }
        anular(e, motivo, usuarioRut);
        return mapper.toDetalle(repository.save(e));
    }

    private void anular(EsnnaEntity e, String motivo, String usuarioRut) {
        if (motivo == null || motivo.isBlank()) {
            throw new EsnnaValidationException("La anulación requiere un motivo.");
        }
        e.setEstadoGestion(EstadoGestion.ANULADO);
        e.setAnulado(true);
        e.setMotivoAnulacion(motivo);
        e.setAnuladoPor(usuarioRut);
        e.setFechaAnulacion(LocalDateTime.now());
    }

    // ===================== Informe / documentos =====================

    @Transactional(readOnly = true)
    public byte[] generarInforme(Long id) {
        return informeService.generar(buscar(id));
    }

    @Transactional(readOnly = true)
    public java.io.ByteArrayInputStream exportarExcel(Semaforo semaforoFinal) {
        List<EsnnaEntity> casos = repository.findParaExport(semaforoFinal);
        return excelExportService.exportarMatrizEsnna(casos);
    }

    @Transactional(readOnly = true)
    public List<DocumentoDescargaDTO> documentos(Long id, int minutosValidez) {
        buscar(id); // valida existencia
        return auditRepository.findByCasoIdOrderByTimestampDesc(id).stream()
                .flatMap(a -> a.getDocumentos().stream())
                .distinct()
                .map(d -> new DocumentoDescargaDTO(d.getNombreArchivo(), d.getSha256(),
                        storageService.urlDescarga(d.getSha256(), minutosValidez)))
                .toList();
    }

    // ===================== Métricas de concordancia (decisión #12) =====================

    @Transactional(readOnly = true)
    public ConcordanciaMetricaDTO metricasConcordancia() {
        long total = repository.countConSemaforoFinal();
        long overrides = repository.countOverrides();
        double tasa = total > 0 ? (double) overrides / total : 0.0;

        Map<String, Long> matriz = new HashMap<>();
        long sub = 0, sobre = 0;
        for (Object[] fila : repository.matrizConfusionRaw()) {
            Semaforo ia = (Semaforo) fila[0];
            Semaforo fin = (Semaforo) fila[1];
            long n = ((Number) fila[2]).longValue();
            matriz.put("IA_" + ia + "->FINAL_" + fin, n);
            int dif = severidad(ia) - severidad(fin);
            if (dif < 0) sub += n;        // IA menos grave que el humano
            else if (dif > 0) sobre += n; // IA más grave que el humano
        }
        return new ConcordanciaMetricaDTO(total, overrides, tasa, matriz, sub, sobre);
    }

    private int severidad(Semaforo s) {
        return switch (s) {
            case ROJO -> 3;
            case AMARILLO -> 2;
            case VERDE -> 1;
        };
    }

    // ===================== Helpers =====================

    private EsnnaEntity buscar(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Caso no encontrado: " + id));
    }
}