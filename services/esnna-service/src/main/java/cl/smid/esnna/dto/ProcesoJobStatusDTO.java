package cl.smid.esnna.dto;

import cl.smid.esnna.domain.EstadoJob;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

/**
 * Estado de un job de /procesar. Mismo cuerpo para el 202 del submit y para
 * cada GET de polling: si el job nace COMPLETADO (lote ya cacheado en draft),
 * el resultado viaja de inmediato y el frontend no necesita un segundo viaje.
 *
 * Contrato de errores: un job FALLIDO se reporta con HTTP 200 en el poll (el
 * poll en sí funcionó) y el error viaja en el campo error con la misma
 * semántica de códigos del GlobalExceptionHandler.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ProcesoJobStatusDTO(
        String jobId,
        EstadoJob estado,
        ProgresoDTO progreso,
        EsnnaProcesoResultado resultado,
        JobErrorDTO error,
        LocalDateTime creadoEn,
        LocalDateTime actualizadoEn
) {

    /** Avance de la fase MAP: documentos con extracción IA resuelta (ok u omitido). */
    public record ProgresoDTO(int procesados, int total) {
    }

    /** Espejo de ApiError para fallas dentro del job (PRIV-2: mensaje genérico, detalle en logs). */
    public record JobErrorDTO(String codigo, String mensaje) {
    }
}