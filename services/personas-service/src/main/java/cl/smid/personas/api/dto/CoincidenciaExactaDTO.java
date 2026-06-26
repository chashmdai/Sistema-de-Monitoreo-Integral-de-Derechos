package cl.smid.personas.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Coincidencia exacta dentro de la respuesta de prevalidación de duplicados. Sólo revela el
 * identificador público y el motivo; nunca el registro completo (G7).
 *
 * @param altKey identificador público opaco de la persona coincidente
 * @param motivo razón de la coincidencia (p. ej. {@code "RUT"})
 */
@Schema(description = "Coincidencia exacta de duplicado.")
public record CoincidenciaExactaDTO(
        @Schema(description = "Identificador publico opaco.", example = "f1c2d3e4-1111-2222-3333-444455556666")
        String altKey,
        @Schema(description = "Motivo de coincidencia.", example = "RUT")
        String motivo) {
}
