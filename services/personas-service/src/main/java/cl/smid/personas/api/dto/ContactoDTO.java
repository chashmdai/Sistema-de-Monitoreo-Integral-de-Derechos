package cl.smid.personas.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Representación de un dato de contacto en la frontera HTTP. Sirve tanto de entrada (dentro de
 * las peticiones de alta/edición) como de salida (dentro de {@link PersonaResponse}).
 *
 * <p>El {@code tipo} se recibe como cadena ({@code TELEFONO}, {@code EMAIL}, {@code DIRECCION})
 * y se convierte al enum de dominio en el mapeo; así un valor desconocido produce un error de
 * validación controlado y no una excepción de deserialización.</p>
 *
 * @param tipo  categoría del contacto (obligatoria)
 * @param valor valor del contacto (obligatorio, máximo 240 caracteres por el esquema)
 */
@Schema(description = "Dato de contacto publico de una persona.")
public record ContactoDTO(
        @Schema(allowableValues = {"TELEFONO", "EMAIL", "DIRECCION"}, example = "TELEFONO")
        @NotNull(message = "El tipo de contacto es obligatorio")
        String tipo,

        @Schema(example = "+56 9 1234 5678")
        @NotBlank(message = "El valor del contacto es obligatorio")
        @Size(max = 240, message = "El valor del contacto no puede exceder 240 caracteres")
        String valor
) {
}
