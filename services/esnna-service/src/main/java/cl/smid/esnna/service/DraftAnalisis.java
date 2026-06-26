package cl.smid.esnna.service;

import cl.smid.esnna.dto.EsnnaProcesoResultado;
import cl.smid.esnna.entity.DocumentoAnalizado;

import java.util.List;

/**
 * Datos de una corrida de /procesar retenidos server-side. /guardar toma de aquí
 * los datos inmutables de auditoría (semáforo IA, usage, hashes, modelos), sin
 * confiar en el cliente. Mantenerlo permite además no re-pagar si se reprocesa
 * el mismo lote.
 */
public record DraftAnalisis(
        EsnnaProcesoResultado resultado,
        List<DocumentoAnalizado> documentos,
        String modeloExtraccion,
        String modeloConsolidacion,
        String versionProtocolo
) {}
