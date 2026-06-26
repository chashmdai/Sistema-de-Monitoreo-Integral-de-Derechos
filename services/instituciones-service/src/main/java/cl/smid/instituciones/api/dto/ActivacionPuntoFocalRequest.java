package cl.smid.instituciones.api.dto;

import jakarta.validation.constraints.NotNull;

/**
 * Cuerpo para activar o desactivar un punto focal.
 *
 * @param activo nuevo estado (obligatorio)
 */
public record ActivacionPuntoFocalRequest(
        @NotNull(message = "El campo 'activo' es obligatorio.")
        Boolean activo) {
}
