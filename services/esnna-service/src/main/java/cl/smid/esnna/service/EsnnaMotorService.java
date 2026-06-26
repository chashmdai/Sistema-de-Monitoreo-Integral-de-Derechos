package cl.smid.esnna.service;

import cl.smid.esnna.domain.EstadoCalidad;
import cl.smid.esnna.domain.Semaforo;
import cl.smid.esnna.dto.*;
import cl.smid.esnna.entity.DocumentoAnalizado;
import cl.smid.esnna.exception.EsnnaProcessingException;
import cl.smid.esnna.exception.GptClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HexFormat;
import java.util.List;

/**
 * Orquesta el procesamiento ESNNA:
 *   extracción (texto + hash) -> respaldo MinIO best-effort -> MAP (Fase 1) ->
 *   REDUCE (Fase 2) -> semáforo determinista en backend -> usage/costo -> draft.
 *
 * Tolerante a fallos parciales: un documento ilegible o un fallo de IA en un
 * documento no aborta el lote, pero el resultado declara estado PARCIAL (BIZ-1).
 * Si la Fase 2 falla, sube como GptClientException (no se entrega resultado
 * degradado, BIZ-2).
 */
@Service
public class EsnnaMotorService {

    private static final Logger log = LoggerFactory.getLogger(EsnnaMotorService.class);

    private final PdfExtractionService pdfExtractionService;
    private final DocumentoStorageService storageService;
    private final EsnnaGptClient gptClient;
    private final SemaforoService semaforoService;
    private final CostoEstimador costoEstimador;
    private final DraftStore draftStore;

    @Value("${esnna.protocolo.version:PR-PDR-05 v1 / 26-12-2024}")
    private String versionProtocolo;

    public EsnnaMotorService(PdfExtractionService pdfExtractionService,
                             DocumentoStorageService storageService,
                             EsnnaGptClient gptClient,
                             SemaforoService semaforoService,
                             CostoEstimador costoEstimador,
                             DraftStore draftStore) {
        this.pdfExtractionService = pdfExtractionService;
        this.storageService = storageService;
        this.gptClient = gptClient;
        this.semaforoService = semaforoService;
        this.costoEstimador = costoEstimador;
        this.draftStore = draftStore;
    }

    public EsnnaProcesoResultado procesar(List<MultipartFile> files) {
        // 1) Extracción de texto + hash, respaldo best-effort
        List<TextoExtraido> textos = new ArrayList<>();
        List<DocumentoOmitidoDTO> omitidos = new ArrayList<>();
        for (MultipartFile file : files) {
            try {
                TextoExtraido te = pdfExtractionService.extraer(file);
                textos.add(te);
                respaldar(te, file);
            } catch (Exception ex) {
                String nombre = file.getOriginalFilename() != null ? file.getOriginalFilename() : "(sin nombre)";
                log.warn("Documento omitido en extracción PDF: {} - {}", nombre, ex.getMessage());
                omitidos.add(new DocumentoOmitidoDTO(nombre, "EXTRACCION_PDF", "PDF ilegible, encriptado o sin texto."));
            }
        }
        if (textos.isEmpty()) {
            throw new EsnnaProcessingException("Ninguno de los " + files.size() + " archivos pudo ser leído.");
        }

        // 2) Dedup de re-pago: si el mismo lote ya fue procesado, devolver cacheado
        String draftId = hashLote(textos);
        var cacheado = draftStore.get(draftId);
        if (cacheado.isPresent()) {
            log.info("Lote ya procesado (draftId={}); se devuelve el resultado cacheado.", draftId);
            return cacheado.get().resultado();
        }

        // 3) FASE 1 (MAP)
        List<EsnnaExtraccionParcialDTO> extracciones = new ArrayList<>();
        int extPrompt = 0, extCompletion = 0, extReasoning = 0;
        String modeloExtraccion = null;
        GptClientException ultimoFalloIa = null;
        for (TextoExtraido te : textos) {
            try {
                ResultadoIa<EsnnaExtraccionParcialDTO> r = gptClient.extraerHechosUnitarios(te.texto());
                extracciones.add(r.dato());
                extPrompt += r.tokensPrompt();
                extCompletion += r.tokensCompletion();
                extReasoning += r.tokensReasoning();
                modeloExtraccion = r.modelo();
            } catch (GptClientException e) {
                ultimoFalloIa = e;
                log.warn("Fallo de extracción IA en documento {}. Se omite. Causa: {}", te.nombreArchivo(), e.getMessage());
                omitidos.add(new DocumentoOmitidoDTO(te.nombreArchivo(), "EXTRACCION_IA", "El motor no extrajo datos del documento."));
            }
        }
        if (extracciones.isEmpty()) {
            throw new GptClientException(
                    "El motor de extracción no procesó ninguno de los documentos provistos.", ultimoFalloIa);
        }

        // 4) FASE 2 (REDUCE) — un fallo aquí sube como 502 (BIZ-2)
        ResultadoIa<EsnnaConsolidadoDTO> cons = gptClient.consolidarYEvaluarCaso(extracciones);
        EsnnaConsolidadoDTO consolidado = cons.dato();

        // 5) Semáforo determinista en backend
        Semaforo semaforoIa = semaforoService.computar(consolidado.criterios());
        String justificacionIa = semaforoService.justificar(consolidado.criterios());

        // 6) Usage + costo
        int totalPrompt = extPrompt + cons.tokensPrompt();
        int totalCompletion = extCompletion + cons.tokensCompletion();
        int totalReasoning = extReasoning + cons.tokensReasoning();
        BigDecimal costo = costoEstimador.estimarUsd(false, extPrompt, extCompletion)
                .add(costoEstimador.estimarUsd(true, cons.tokensPrompt(), cons.tokensCompletion()));
        UsageDTO usage = new UsageDTO(totalPrompt, totalCompletion, totalReasoning, costo);

        // 7) Calidad y resultado
        EstadoCalidad calidad = omitidos.isEmpty() ? EstadoCalidad.COMPLETA : EstadoCalidad.PARCIAL;
        EsnnaProcesoResultado resultado = new EsnnaProcesoResultado(
                draftId, semaforoIa, justificacionIa, consolidado, calidad,
                files.size(), extracciones.size(), omitidos, usage);

        // 8) Draft (incluye datos de auditoría para /guardar)
        List<DocumentoAnalizado> docs = textos.stream()
                .map(t -> DocumentoAnalizado.builder().nombreArchivo(t.nombreArchivo()).sha256(t.sha256()).build())
                .toList();
        draftStore.put(draftId, new DraftAnalisis(resultado, docs,
                modeloExtraccion, cons.modelo(), versionProtocolo));

        log.info("Caso procesado: {}/{} documentos, calidad={}, semaforoIA={}.",
                extracciones.size(), files.size(), calidad, semaforoIa);
        return resultado;
    }

    private void respaldar(TextoExtraido te, MultipartFile file) {
        try {
            storageService.guardar(te.sha256(), file.getBytes(), file.getContentType());
        } catch (Exception e) {
            log.warn("No se pudo respaldar {} en storage: {}", te.nombreArchivo(), e.getMessage());
        }
    }

    private String hashLote(List<TextoExtraido> textos) {
        try {
            List<String> hashes = new ArrayList<>(textos.stream().map(TextoExtraido::sha256).toList());
            hashes.sort(String::compareTo);
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(md.digest(String.join("|", hashes).getBytes()));
        } catch (Exception e) {
            throw new EsnnaProcessingException("No se pudo calcular el identificador del lote.", e);
        }
    }
}
