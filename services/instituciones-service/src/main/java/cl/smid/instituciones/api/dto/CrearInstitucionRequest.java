package cl.smid.instituciones.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Cuerpo para crear una institución.
 *
 * @param codigo       código institucional opcional (único si viene)
 * @param nombre       nombre (obligatorio)
 * @param tipoAlt      alt_key del tipo (obligatorio; debe existir y estar vigente)
 * @param rut          RUT opcional (cualquier formato usual)
 * @param regionCodigo código de región opcional
 * @param comunaCodigo código de comuna opcional
 * @param direccion    dirección opcional
 * @param telefono     teléfono opcional
 * @param email        correo opcional
 * @param sitioWeb     sitio web opcional
 */
public record CrearInstitucionRequest(
        @Size(max = 60, message = "El código no puede exceder 60 caracteres.")
        String codigo,

        @NotBlank(message = "El nombre es obligatorio.")
        @Size(max = 200, message = "El nombre no puede exceder 200 caracteres.")
        String nombre,

        @NotBlank(message = "El tipo (tipoAlt) es obligatorio.")
        @Size(max = 36, message = "El identificador de tipo no puede exceder 36 caracteres.")
        String tipoAlt,

        @Size(max = 20, message = "El RUT no puede exceder 20 caracteres.")
        String rut,

        @Size(max = 10, message = "El código de región no puede exceder 10 caracteres.")
        String regionCodigo,

        @Size(max = 10, message = "El código de comuna no puede exceder 10 caracteres.")
        String comunaCodigo,

        @Size(max = 240, message = "La dirección no puede exceder 240 caracteres.")
        String direccion,

        @Size(max = 40, message = "El teléfono no puede exceder 40 caracteres.")
        String telefono,

        @Email(message = "El correo no tiene un formato válido.")
        @Size(max = 160, message = "El correo no puede exceder 160 caracteres.")
        String email,

        @Size(max = 200, message = "El sitio web no puede exceder 200 caracteres.")
        String sitioWeb) {
}
