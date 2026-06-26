package cl.smid.requerimientos.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Derecho vulnerado expuesto en la API. Solo referencias por alt_key (nunca llaves internas).
 *
 * @param idDerecho alt_key del derecho en el Catálogo
 * @param idCausa   alt_key de la causa (nulable)
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Derecho vulnerado asociado a un NNA.")
public record DerechoVulneradoDTO(
        @Schema(description = "altKey del derecho en Catalogo.", example = "a1b2c3d4-1111-2222-3333-444455556666")
        String idDerecho,
        @Schema(description = "altKey de la causa, opcional.", example = "e5f6a7b8-1111-2222-3333-444455556666", nullable = true)
        String idCausa) {
}
