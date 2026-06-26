package cl.smid.instituciones.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Cuerpo para editar un tipo de institución.
 *
 * @param nombre      nombre del tipo (obligatorio)
 * @param ambito      ámbito sectorial (obligatorio)
 * @param descripcion descripción opcional
 */
public record EditarTipoRequest(
        @NotBlank(message = "El nombre es obligatorio.")
        @Size(max = 160, message = "El nombre no puede exceder 160 caracteres.")
        String nombre,

        @NotBlank(message = "El ámbito es obligatorio.")
        @Size(max = 20, message = "El ámbito no puede exceder 20 caracteres.")
        String ambito,

        @Size(max = 500, message = "La descripción no puede exceder 500 caracteres.")
        String descripcion) {
}
