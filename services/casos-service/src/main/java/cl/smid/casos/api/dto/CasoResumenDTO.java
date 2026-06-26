package cl.smid.casos.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

/**
 * Proyección de resumen de un caso para listados. Expone SOLO el identificador opaco; la PK interna no
 * cruza la frontera (cierre IDOR). No incluye historial.
 */
@Schema(description = "Resumen de un caso para listados paginados.")
public record CasoResumenDTO(
        @Schema(description = "Identificador opaco del caso.", example = "7d6b3c3c-9d22-4b10-8d3d-1f3900c13f20")
        String altKey,
        @Schema(description = "Número institucional del expediente.", example = "EXP-RM-1/2027")
        String numeroExpediente,
        @Schema(description = "Estado actual del expediente.", example = "ABIERTO",
                allowableValues = {"ABIERTO", "EN_INVESTIGACION", "EN_SEGUIMIENTO",
                        "SUSPENDIDO", "CERRADO", "ARCHIVADO"})
        String estado,
        @Schema(description = "Complejidad heredada del requerimiento.", example = "MEDIANA")
        String complejidad,
        @Schema(description = "Identificador opaco de la sede.", example = "11111111-1111-1111-1111-111111111111")
        String idSede,
        @Schema(description = "Identificador opaco de la unidad.", example = "4f86e9a4-2924-41d7-bf27-6ef13b6f6b9a")
        String idUnidad,
        @Schema(description = "Identificador opaco del profesional responsable.",
                example = "c27f4500-f412-4fd1-86a8-6caa5933583b")
        String idProfesionalResponsable,
        @Schema(description = "Indica si el expediente requiere ficha reservada.", example = "false")
        boolean requiereFichaReservada,
        @Schema(description = "Indica si pertenece a serie beta.", example = "false")
        boolean esBeta,
        @Schema(description = "Fecha de apertura en UTC.", example = "2027-02-01T10:00:00Z")
        Instant abiertoEn,
        @Schema(description = "Fecha de cierre en UTC, nula si sigue activo.", example = "2027-03-10T15:30:00Z")
        Instant cerradoEn,
        @Schema(description = "Fecha de última actualización en UTC.", example = "2027-02-05T12:15:00Z")
        Instant actualizadoEn) {
}
