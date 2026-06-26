package cl.smid.casos.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Datos de enriquecimiento on-demand expuestos en el detalle. Si {@code disponible} es {@code false},
 * el caso se entrega como esqueleto (el resto de los campos pueden ser nulos).
 */
@Schema(description = "Datos de enriquecimiento on-demand del requerimiento de origen.")
public record EnriquecimientoDTO(
        @Schema(description = "Indica si el enriquecimiento remoto estuvo disponible.", example = "false")
        boolean disponible,
        @Schema(description = "Estado del requerimiento de origen cuando el enriquecimiento está disponible.",
                example = "ASIGNADO")
        String estadoRequerimiento,
        @Schema(description = "Canal de ingreso del requerimiento de origen.", example = "WEB")
        String canal,
        @Schema(description = "Cantidad referencial de NNA afectados.", example = "2")
        Integer cantidadNnaAfectados) {
}
