package cl.smid.requerimientos.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * NNA afectado expuesto en la API. Expone el alt_key del NNA y su snapshot; nunca la PK interna.
 *
 * @param idPersona alt_key del NNA en personas-service
 * @param persona   snapshot mínimo (nulable)
 * @param derechos  derechos vulnerados imputados
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "NNA afectado asociado al requerimiento.")
public record NnaAfectadoDTO(
        @Schema(description = "altKey del NNA en personas-service.", example = "7d6c5b4a-3e2f-1a0b-9c8d-7e6f5a4b3c2d")
        String idPersona,
        @Schema(nullable = true)
        SnapshotDTO persona,
        @Schema(description = "Derechos vulnerados imputados.")
        List<DerechoVulneradoDTO> derechos) {
}
