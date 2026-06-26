package cl.smid.instituciones.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Cuerpo para crear un punto focal en una institución.
 *
 * @param nombre    nombre del contacto (obligatorio)
 * @param cargo     cargo opcional
 * @param email     correo opcional
 * @param telefono  teléfono opcional
 * @param principal si es el contacto principal
 */
public record CrearPuntoFocalRequest(
        @NotBlank(message = "El nombre es obligatorio.")
        @Size(max = 160, message = "El nombre no puede exceder 160 caracteres.")
        String nombre,

        @Size(max = 120, message = "El cargo no puede exceder 120 caracteres.")
        String cargo,

        @Email(message = "El correo no tiene un formato válido.")
        @Size(max = 160, message = "El correo no puede exceder 160 caracteres.")
        String email,

        @Size(max = 40, message = "El teléfono no puede exceder 40 caracteres.")
        String telefono,

        boolean principal) {
}
