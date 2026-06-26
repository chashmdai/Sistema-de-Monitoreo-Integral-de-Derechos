package cl.smid.instituciones.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Cuerpo para editar un punto focal. Mismos campos y reglas que la creación.
 */
public record EditarPuntoFocalRequest(
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
