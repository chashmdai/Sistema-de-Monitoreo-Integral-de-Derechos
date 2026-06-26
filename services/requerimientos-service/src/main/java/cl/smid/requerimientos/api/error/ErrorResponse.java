package cl.smid.requerimientos.api.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.Map;

/**
 * Sobre de error unificado del ecosistema SMID (Núcleo 2.5). El campo de ruta se llama
 * {@code ruta} (no {@code path}); {@code detalles} solo se incluye en errores de validación y se
 * omite del JSON cuando es nulo.
 *
 * @param status    estado HTTP numérico
 * @param error     frase de razón del estado HTTP (p. ej. {@code Not Found})
 * @param codigo    código estable del catálogo (p. ej. {@code REQ-409})
 * @param mensaje   mensaje legible en español
 * @param detalles  mapa campo→mensaje (solo validación; nulable)
 * @param ruta      URI de la petición
 * @param timestamp instante UTC del error
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Sobre de error unificado del ecosistema SMID.")
public record ErrorResponse(
        @Schema(description = "Estado HTTP numerico.", example = "409")
        int status,
        @Schema(description = "Frase HTTP estandar.", example = "Conflict")
        String error,
        @Schema(description = "Codigo estable.", allowableValues = {"AUTZ-003", "AUTZ-004", "REQ-001", "REQ-404", "REQ-409", "REQ-422", "REQ-500"}, example = "REQ-409")
        String codigo,
        @Schema(description = "Mensaje legible.", example = "La operacion entra en conflicto con el estado actual del requerimiento.")
        String mensaje,
        @Schema(description = "Detalles campo-mensaje solo en validacion.", nullable = true)
        Map<String, String> detalles,
        @Schema(description = "Ruta interna que produjo el error.", example = "/requerimientos/1c2d3e4f/enviar")
        String ruta,
        @Schema(description = "Instante UTC del error.", example = "2027-04-10T09:00:00Z")
        Instant timestamp) {
}
