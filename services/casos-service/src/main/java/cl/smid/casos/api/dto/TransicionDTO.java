package cl.smid.casos.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Cuerpo de la solicitud de transición de estado. La {@code accion} se valida como no vacía y se
 * interpreta en el controlador contra el enum {@code AccionCaso} (valor desconocido ⇒ CAS-001).
 *
 * @param accion      nombre de la acción (p. ej. {@code INICIAR_INVESTIGACION}, {@code CERRAR}).
 * @param observacion nota opcional asociada a la transición (máximo 2000 caracteres).
 */
@Schema(description = """
        Solicitud de transición de estado. CERRAR, REABRIR y ARCHIVAR requieren rol de Coordinación.
        Las demás acciones son operativas y requieren alcance territorial sobre el caso.
        """)
public record TransicionDTO(
        @Schema(description = "Acción a aplicar sobre el caso.", example = "INICIAR_INVESTIGACION",
                requiredMode = Schema.RequiredMode.REQUIRED,
                allowableValues = {"INICIAR_INVESTIGACION", "DERIVAR_A_SEGUIMIENTO", "SUSPENDER",
                        "REANUDAR", "CERRAR", "REABRIR", "ARCHIVAR"})
        @NotBlank(message = "La acción es obligatoria.")
        String accion,
        @Schema(description = "Observación opcional de la transición.", example = "Inicio de investigación institucional.",
                maxLength = 2000)
        @Size(max = 2000, message = "La observación no puede superar 2000 caracteres.")
        String observacion) {
}
