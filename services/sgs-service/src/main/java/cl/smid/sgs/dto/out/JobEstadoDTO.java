package cl.smid.sgs.dto.out;

import cl.smid.sgs.enums.JobStatus;
import cl.smid.sgs.enums.TipoAnalisis;
import com.fasterxml.jackson.databind.JsonNode;

import java.time.LocalDateTime;

/** Respuesta del polling GET /jobs/{id}. payload = OficioExtraccionDTO o SgsEvaluacionResponseDTO ya parseado. */
public record JobEstadoDTO(
        String jobId,
        TipoAnalisis tipo,
        JobStatus status,
        JsonNode payload,
        String error,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
