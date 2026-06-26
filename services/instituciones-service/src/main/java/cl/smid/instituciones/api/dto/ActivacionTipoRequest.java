package cl.smid.instituciones.api.dto;

import jakarta.validation.constraints.NotNull;

/**
 * Cuerpo para cambiar la vigencia de un tipo de institución.
 *
 * @param vigente nuevo estado de vigencia (obligatorio)
 */
public record ActivacionTipoRequest(
        @NotNull(message = "El campo 'vigente' es obligatorio.")
        Boolean vigente) {
}
