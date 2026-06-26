package cl.smid.personas.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

/**
 * Petición de actualización parcial de una persona ({@code PUT /personas/{altKey}}). Semántica
 * <i>partial-merge</i>: todos los campos son opcionales y un valor nulo significa "no tocar".
 *
 * <p>Convenciones especiales:</p>
 * <ul>
 *   <li>{@code tipo} no se incluye: el tipo de persona no se modifica tras el alta.</li>
 *   <li>{@code rut} nulo = mantener; cadena en blanco ({@code ""}) = limpiar el RUT; cualquier
 *       otro valor se revalida por módulo 11.</li>
 *   <li>{@code contactos} nulo = mantener los actuales; lista (incluida vacía) = reemplazar el
 *       conjunto completo.</li>
 * </ul>
 *
 * @param rut             RUT a establecer/limpiar (ver convención)
 * @param nombres         nombres de pila
 * @param apellidoPaterno apellido paterno
 * @param apellidoMaterno apellido materno
 * @param razonSocial     razón social
 * @param fechaNacimiento fecha de nacimiento
 * @param sexo            sexo registrado
 * @param nacionalidad    nacionalidad
 * @param contactos       contactos a establecer (ver convención)
 */
@Schema(description = "Peticion de actualizacion parcial de persona. Campos nulos significan no modificar.")
public record ActualizarPersonaRequest(
        @Schema(description = "RUT a establecer. Nulo mantiene el valor; cadena vacia limpia el RUT.", example = "12345678-5", nullable = true)
        @Size(max = 12, message = "El RUT no puede exceder 12 caracteres")
        String rut,

        @Schema(example = "Camila", nullable = true)
        @Size(max = 160, message = "Los nombres no pueden exceder 160 caracteres")
        String nombres,

        @Schema(example = "Reyes", nullable = true)
        @Size(max = 120, message = "El apellido paterno no puede exceder 120 caracteres")
        String apellidoPaterno,

        @Schema(example = "Soto", nullable = true)
        @Size(max = 120, message = "El apellido materno no puede exceder 120 caracteres")
        String apellidoMaterno,

        @Schema(example = "Fundacion Ejemplo", nullable = true)
        @Size(max = 200, message = "La razón social no puede exceder 200 caracteres")
        String razonSocial,

        @Schema(example = "2015-03-10", nullable = true)
        LocalDate fechaNacimiento,

        @Schema(allowableValues = {"F", "M", "OTRO", "NO_INFORMA"}, example = "F", nullable = true)
        String sexo,

        @Schema(example = "Chilena", nullable = true)
        @Size(max = 60, message = "La nacionalidad no puede exceder 60 caracteres")
        String nacionalidad,

        @Schema(description = "Nulo mantiene contactos; lista reemplaza el conjunto completo.", nullable = true)
        @Valid
        List<ContactoDTO> contactos
) {
}
