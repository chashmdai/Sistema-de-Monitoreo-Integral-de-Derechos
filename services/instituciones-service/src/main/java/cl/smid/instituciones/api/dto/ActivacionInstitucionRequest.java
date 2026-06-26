package cl.smid.instituciones.api.dto;

import jakarta.validation.constraints.NotNull;

/**
 * Cuerpo para activar o desactivar una institución.
 *
 * @param activa nuevo estado (obligatorio)
 */
public record ActivacionInstitucionRequest(
        @NotNull(message = "El campo 'activa' es obligatorio.")
        Boolean activa) {
}
