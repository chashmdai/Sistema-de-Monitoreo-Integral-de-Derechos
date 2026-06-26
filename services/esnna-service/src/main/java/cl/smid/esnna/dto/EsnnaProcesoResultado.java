package cl.smid.esnna.dto;

import cl.smid.esnna.domain.EstadoCalidad;
import cl.smid.esnna.domain.Semaforo;

import java.util.List;

/**
 * Respuesta tipada de /procesar (reemplaza el Map<String,Object> ad-hoc, CTRL-5).
 *
 *  - draftId: referencia al borrador server-side; /guardar lo usa para tomar los
 *    datos de auditoría inmutables (semáforo IA, modelos, hashes, usage) sin
 *    confiar en el cliente, y para no re-pagar el análisis (FEAT borrador).
 *  - semaforoIa + justificacionIa: computados por el backend.
 *  - estadoCalidad + documentosOmitidos: hacen visible una consolidación PARCIAL
 *    (BIZ-1/BIZ-2), en vez de enterrarla en un int.
 */
public record EsnnaProcesoResultado(
        String draftId,
        Semaforo semaforoIa,
        String justificacionIa,
        EsnnaConsolidadoDTO consolidado,
        EstadoCalidad estadoCalidad,
        int documentosTotales,
        int documentosProcesados,
        List<DocumentoOmitidoDTO> documentosOmitidos,
        UsageDTO usage
) {}
