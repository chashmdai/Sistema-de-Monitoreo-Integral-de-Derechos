package cl.smid.catalogo.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Solicitud de creación de una causa vinculada a un derecho.
 *
 * @param codigo código de la causa (obligatorio, único dentro del derecho, ≤ 40)
 * @param nombre nombre legible (obligatorio, ≤ 200)
 */
@Schema(description = "Solicitud de creacion de causa para un derecho.")
public record CrearCausaRequest(
        @Schema(description = "Codigo unico dentro del derecho.", example = "BARRERA_ACCESIBILIDAD", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "El código es obligatorio.")
        @Size(max = 40, message = "El código no puede superar los 40 caracteres.")
        String codigo,

        @Schema(example = "Barreras de accesibilidad", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "El nombre es obligatorio.")
        @Size(max = 200, message = "El nombre no puede superar los 200 caracteres.")
        String nombre
) {}
