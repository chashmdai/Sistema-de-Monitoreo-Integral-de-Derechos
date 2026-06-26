package cl.smid.requerimientos.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

/**
 * Snapshot mínimo de una persona expuesto en la API (nombre/RUT capturados). Refleja
 * {@code SnapshotPersona} del dominio.
 *
 * @param nombreLegible nombre para mostrar (nulable)
 * @param rut           RUT (nulable)
 * @param capturadoEn   instante UTC de captura
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Snapshot minimo de persona capturado al momento de asociarla.")
public record SnapshotDTO(
        @Schema(example = "Camila Reyes", nullable = true)
        String nombreLegible,
        @Schema(example = "12345678-5", nullable = true)
        String rut,
        @Schema(example = "2027-04-10T09:00:00Z")
        Instant capturadoEn) {
}
