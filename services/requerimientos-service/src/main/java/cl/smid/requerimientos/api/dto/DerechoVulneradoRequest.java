package cl.smid.requerimientos.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/**
 * Derecho vulnerado solicitado para un NNA. Solo referencias por alt_key hacia el Catálogo.
 *
 * @param idDerechoAlt alt_key del derecho (obligatorio)
 * @param idCausaAlt   alt_key de la causa (opcional)
 */
@Schema(description = "Derecho vulnerado solicitado para un NNA.")
public record DerechoVulneradoRequest(
        @Schema(description = "altKey del derecho en Catalogo.", example = "a1b2c3d4-1111-2222-3333-444455556666", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "El alt_key del derecho es obligatorio") String idDerechoAlt,
        @Schema(description = "altKey de la causa.", example = "e5f6a7b8-1111-2222-3333-444455556666", nullable = true)
        String idCausaAlt) {
}
