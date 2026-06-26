package cl.smid.personas.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

/**
 * Petición de la prevalidación de duplicados ({@code POST /personas/buscar-duplicados}, USR.01).
 * Es una operación de sólo lectura y <b>cross-territorial</b> por diseño (la deduplicación debe
 * poder ver registros de todas las sedes), por lo que no acarrea contexto territorial.
 *
 * <p>Todos los datos identificatorios son opcionales: el buscador decide qué estrategias puede
 * ejecutar según lo informado (sin RUT no hay match exacto; sin nombre ni fecha no hay difusos).</p>
 *
 * @param tipo            tipo de persona buscado (obligatorio; acota expectativas)
 * @param rut             RUT en formato de captura (opcional)
 * @param nombres         nombres de pila (opcional)
 * @param apellidoPaterno apellido paterno (opcional)
 * @param apellidoMaterno apellido materno (opcional)
 * @param fechaNacimiento fecha de nacimiento (opcional)
 */
@Schema(description = "Criterios para prevalidacion de duplicados.")
public record BuscarDuplicadosRequest(
        @Schema(description = "Tipo de persona buscado.", allowableValues = {"NNA", "ADULTO", "JURIDICA", "TESTIGO"}, example = "ADULTO")
        @NotNull(message = "El tipo de persona es obligatorio")
        String tipo,

        @Schema(description = "RUT opcional. Si es invalido, responde PER-002.", example = "12345678-5", nullable = true)
        @Size(max = 12, message = "El RUT no puede exceder 12 caracteres")
        String rut,

        @Schema(example = "Juan", nullable = true)
        @Size(max = 160, message = "Los nombres no pueden exceder 160 caracteres")
        String nombres,

        @Schema(example = "Perez", nullable = true)
        @Size(max = 120, message = "El apellido paterno no puede exceder 120 caracteres")
        String apellidoPaterno,

        @Schema(example = "Soto", nullable = true)
        @Size(max = 120, message = "El apellido materno no puede exceder 120 caracteres")
        String apellidoMaterno,

        @Schema(example = "1990-05-20", nullable = true)
        LocalDate fechaNacimiento
) {
}
