package cl.smid.casos.api.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.Map;

/**
 * Sobre de error unificado del clúster SMID (Núcleo 2.5). Campos: {@code status, error, codigo,
 * mensaje, detalles, ruta, timestamp}. Importante: se usa {@code ruta} (no "path"), y
 * {@code detalles} solo aparece en errores de validación (se omite cuando es nulo).
 *
 * @param status    código HTTP numérico.
 * @param error     frase de estado HTTP.
 * @param codigo    código estable del servicio (p. ej. {@code CAS-404}).
 * @param mensaje   descripción legible del error.
 * @param detalles  detalle por campo (solo validación); se omite si es nulo.
 * @param ruta      URI de la solicitud.
 * @param timestamp instante UTC del error (ISO-8601).
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Sobre de error unificado del ecosistema SMID.")
public record SobreError(
        @Schema(description = "Código HTTP numérico.", example = "404")
        int status,
        @Schema(description = "Frase HTTP.", example = "Not Found")
        String error,
        @Schema(description = "Código estable del error.",
                example = "CAS-404",
                allowableValues = {"AUTZ-003", "AUTZ-004", "CAS-001", "CAS-404", "CAS-409",
                        "CAS-422", "CAS-500"})
        String codigo,
        @Schema(description = "Mensaje legible para el consumidor.",
                example = "No existe un caso accesible para el usuario autenticado.")
        String mensaje,
        @Schema(description = "Detalle por campo, solo para errores de validación.",
                example = "{\"accion\":\"La acción es obligatoria.\"}")
        Map<String, String> detalles,
        @Schema(description = "Ruta solicitada.", example = "/casos/7d6b3c3c-9d22-4b10-8d3d-1f3900c13f20")
        String ruta,
        @Schema(description = "Instante UTC del error.", example = "2027-02-01T10:00:00Z")
        Instant timestamp) {
}
