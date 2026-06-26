package cl.smid.casos.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

/**
 * Asiento del historial de transiciones expuesto en el detalle del caso.
 */
@Schema(description = "Asiento del historial de materialización y transiciones del caso.")
public record HistorialTransicionDTO(
        @Schema(description = "Identificador opaco del asiento de historial.",
                example = "afcf2d25-190c-4e37-aef8-9dd1543d29b0")
        String altKey,
        @Schema(description = "Estado origen. Es nulo en la materialización inicial.", example = "ABIERTO",
                allowableValues = {"ABIERTO", "EN_INVESTIGACION", "EN_SEGUIMIENTO",
                        "SUSPENDIDO", "CERRADO", "ARCHIVADO"})
        String estadoOrigen,
        @Schema(description = "Estado destino del asiento.", example = "EN_INVESTIGACION",
                allowableValues = {"ABIERTO", "EN_INVESTIGACION", "EN_SEGUIMIENTO",
                        "SUSPENDIDO", "CERRADO", "ARCHIVADO"})
        String estadoDestino,
        @Schema(description = "Acción aplicada. MATERIALIZACION registra el nacimiento por evento.",
                example = "MATERIALIZACION",
                allowableValues = {"MATERIALIZACION", "INICIAR_INVESTIGACION", "DERIVAR_A_SEGUIMIENTO",
                        "SUSPENDER", "REANUDAR", "CERRAR", "REABRIR", "ARCHIVAR"})
        String accion,
        @Schema(description = "Observación asociada al asiento.", example = "Caso materializado desde requerimiento asignado.")
        String observacion,
        @Schema(description = "Identificador opaco del actor. En materialización usa actor de sistema.",
                example = "00000000-0000-0000-0000-000000000000")
        String actor,
        @Schema(description = "Instante UTC de la transición.", example = "2027-02-01T10:00:00Z")
        Instant ocurridoEn) {
}
