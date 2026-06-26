package cl.smid.personas.api.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.Map;

/**
 * Sobre de error unificado del ecosistema SMID (Núcleo 2.5), idéntico en forma al de auth y
 * catálogo para que los clientes lo procesen de manera homogénea.
 *
 * <p>Notas de contrato:</p>
 * <ul>
 *   <li>El campo de ruta se llama {@code ruta} (no {@code path}); es una discrepancia documentada
 *       respecto de algunos ejemplos, resuelta a favor del nombre en español del Núcleo.</li>
 *   <li>{@code detalles} (campo→mensaje) sólo se incluye en errores de validación ({@code PER-001});
 *       se omite del JSON cuando es nulo gracias a {@link JsonInclude}.</li>
 *   <li>{@code timestamp} es un instante UTC en formato ISO-8601.</li>
 * </ul>
 *
 * @param status    código HTTP numérico (p. ej. 404)
 * @param error     frase HTTP estándar (p. ej. {@code "Not Found"})
 * @param codigo    código estable de negocio (p. ej. {@code "PER-404"})
 * @param mensaje   descripción legible del error
 * @param detalles  detalles de validación campo→mensaje (sólo en {@code PER-001}); nulo en otros
 * @param ruta      ruta de la petición que originó el error
 * @param timestamp instante UTC de generación del error
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Sobre de error unificado del ecosistema SMID.")
public record ErrorResponse(
        @Schema(description = "Codigo HTTP numerico.", example = "422")
        int status,
        @Schema(description = "Frase HTTP estandar.", example = "Unprocessable Entity")
        String error,
        @Schema(description = "Codigo estable de negocio.", allowableValues = {"AUTZ-003", "AUTZ-004", "PER-001", "PER-002", "PER-003", "PER-404", "PER-500"}, example = "PER-002")
        String codigo,
        @Schema(description = "Mensaje legible.", example = "El RUT '12345678-9' no supera la validacion de digito verificador.")
        String mensaje,
        @Schema(description = "Detalles campo-mensaje solo en PER-001.", nullable = true)
        Map<String, String> detalles,
        @Schema(description = "Ruta interna que produjo el error.", example = "/personas")
        String ruta,
        @Schema(description = "Instante UTC del error.", example = "2026-01-15T12:00:00Z")
        Instant timestamp
) {
}
