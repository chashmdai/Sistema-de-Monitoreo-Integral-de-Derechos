package cl.smid.personas.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

/**
 * Petición de alta de una persona ({@code POST /personas}). La validación sintáctica se hace
 * aquí con Bean Validation (produce {@code PER-001} + detalles campo→mensaje); las reglas de
 * negocio (módulo 11 del RUT, unicidad, alcance) las aplica el dominio.
 *
 * <p>Flexibilidad de ingreso (Núcleo 5.5): el único campo siempre obligatorio es el {@code tipo}.
 * El RUT es opcional —un NNA puede registrarse sólo con su nombre— y se valida únicamente si
 * viene informado. La sede/unidad y la autoría no se reciben del cliente: se estampan desde el
 * token para impedir suplantación territorial.</p>
 *
 * @param tipo            tipo de persona (obligatorio): NNA | ADULTO | JURIDICA | TESTIGO
 * @param rut             RUT en cualquier formato de captura (opcional)
 * @param nombres         nombres de pila
 * @param apellidoPaterno apellido paterno
 * @param apellidoMaterno apellido materno
 * @param razonSocial     razón social (persona jurídica)
 * @param fechaNacimiento fecha de nacimiento
 * @param sexo            sexo registrado: F | M | OTRO | NO_INFORMA
 * @param nacionalidad    nacionalidad
 * @param contactos       lista de contactos (opcional)
 */
@Schema(description = "Peticion de alta de persona. El unico campo siempre obligatorio es tipo; un NNA puede registrarse sin RUT.")
public record CrearPersonaRequest(
        @Schema(allowableValues = {"NNA", "ADULTO", "JURIDICA", "TESTIGO"}, example = "NNA")
        @NotNull(message = "El tipo de persona es obligatorio")
        String tipo,

        @Schema(description = "RUT opcional. Si viene informado se valida por modulo 11; invalido responde PER-002.", example = "12345678-5", nullable = true)
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

        @Schema(nullable = true)
        @Valid
        List<ContactoDTO> contactos
) {
}
