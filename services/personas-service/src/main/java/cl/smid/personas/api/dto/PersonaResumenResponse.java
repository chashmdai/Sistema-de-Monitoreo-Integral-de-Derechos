package cl.smid.personas.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

/**
 * Proyección pública <b>reducida</b> de una persona, usada en los listados de búsqueda
 * ({@code GET /personas?rut=}|{@code ?q=}). Expone sólo lo necesario para identificar y
 * seleccionar un registro, evitando volcar el detalle completo en respuestas de listado (G7).
 *
 * @param altKey          identificador público opaco (UUID)
 * @param tipo            tipo de persona
 * @param nombre          nombre legible compuesto
 * @param rut             RUT canónico o nulo
 * @param fechaNacimiento fecha de nacimiento o nula
 */
@Schema(description = "Resumen publico de persona para listados.")
public record PersonaResumenResponse(
        @Schema(description = "Identificador publico opaco.", example = "a9b8c7d6-1111-2222-3333-444455556666")
        String altKey,
        @Schema(allowableValues = {"NNA", "ADULTO", "JURIDICA", "TESTIGO"}, example = "ADULTO")
        String tipo,
        @Schema(example = "Juan Perez")
        String nombre,
        @Schema(example = "12345678-5", nullable = true)
        String rut,
        @Schema(example = "1990-05-20", nullable = true)
        LocalDate fechaNacimiento
) {
}
