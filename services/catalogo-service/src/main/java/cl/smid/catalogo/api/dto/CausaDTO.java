package cl.smid.catalogo.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Proyección de una causa para la API.
 *
 * @param altKey  identificador público opaco
 * @param codigo  código de la causa (único dentro del derecho)
 * @param nombre  nombre legible
 * @param vigente si la causa está vigente
 */
@Schema(description = "Causa asociada a un derecho.")
public record CausaDTO(
        @Schema(description = "Identificador publico opaco.", example = "22222222-2222-4222-8222-000000000001")
        String altKey,
        @Schema(description = "Codigo unico dentro del derecho.", example = "DESERCION")
        String codigo,
        @Schema(example = "Desercion escolar")
        String nombre,
        @Schema(example = "true")
        boolean vigente
) {}
