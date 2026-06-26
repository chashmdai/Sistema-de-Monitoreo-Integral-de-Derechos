package cl.smid.personas.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

/**
 * Coincidencia probable (difusa) dentro de la respuesta de prevalidación de duplicados. Expone
 * el dato mínimo para que el operador humano decida: identificador, nombre, fecha y score.
 *
 * @param altKey          identificador público opaco de la persona candidata
 * @param nombre          nombre legible compacto
 * @param fechaNacimiento fecha de nacimiento (puede ser nula)
 * @param score           grado de similitud en el rango {@code [0,1]}
 */
@Schema(description = "Coincidencia probable por similitud.")
public record CoincidenciaProbableDTO(
        @Schema(description = "Identificador publico opaco.", example = "a9b8c7d6-1111-2222-3333-444455556666")
        String altKey,
        @Schema(example = "Juan Perez")
        String nombre,
        @Schema(example = "1990-05-20", nullable = true)
        LocalDate fechaNacimiento,
        @Schema(description = "Score de similitud entre 0 y 1.", example = "0.97")
        double score) {
}
