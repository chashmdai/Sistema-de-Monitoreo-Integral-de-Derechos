package cl.smid.requerimientos.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

/**
 * Petición de alta de un NNA afectado con sus derechos vulnerados (USR.01).
 *
 * @param idPersonaAlt alt_key del NNA en personas-service (obligatorio)
 * @param derechos     derechos vulnerados a imputar (puede ser vacío)
 */
@Schema(description = "Peticion para agregar un NNA afectado y sus derechos vulnerados.")
public record AgregarNnaRequest(
        @Schema(description = "altKey del NNA en personas-service.", example = "7d6c5b4a-3e2f-1a0b-9c8d-7e6f5a4b3c2d", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "El alt_key del NNA es obligatorio") String idPersonaAlt,
        @Schema(description = "Derechos vulnerados imputados al NNA.")
        @Valid List<DerechoVulneradoRequest> derechos) {
}
