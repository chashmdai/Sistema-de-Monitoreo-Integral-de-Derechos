package cl.smid.catalogo.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

/**
 * Solicitud de actualización de un derecho.
 *
 * <p>El {@code codigo} es <b>opcional</b>: si se envía y difiere del actual, el dominio lo
 * rechaza con {@code CAT-006} (inmutabilidad); si coincide o se omite, no hay cambio. El
 * {@code nombre} es obligatorio (operación de reemplazo). {@code idPadreAltKey}, si se
 * envía, reubica el derecho bajo ese nuevo padre.</p>
 *
 * @param codigo        código (opcional; debe coincidir con el actual si se envía, ≤ 40)
 * @param nombre        nuevo nombre (obligatorio, ≤ 200)
 * @param descripcion   nueva descripción (opcional, reemplaza; ≤ 600)
 * @param orden         nuevo orden (opcional; si se omite, se conserva)
 * @param idPadreAltKey alt_key del nuevo padre (opcional; si se omite, se conserva el padre)
 */
@Schema(description = "Solicitud de actualizacion de derecho. El codigo es inmutable: si cambia, responde CAT-006.")
public record ActualizarDerechoRequest(
        @Schema(description = "Codigo estable. Opcional; si se envia debe coincidir con el actual.", example = "EDU.INCLUSION", nullable = true)
        @Size(max = 40, message = "El código no puede superar los 40 caracteres.")
        String codigo,

        @Schema(description = "Nombre legible del derecho.", example = "Inclusion y diversidad", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "El nombre es obligatorio.")
        @Size(max = 200, message = "El nombre no puede superar los 200 caracteres.")
        String nombre,

        @Schema(description = "Descripcion opcional.", example = "Acciones asociadas a inclusion educativa.", nullable = true)
        @Size(max = 600, message = "La descripción no puede superar los 600 caracteres.")
        String descripcion,

        @Schema(description = "Orden entre hermanos.", example = "3", nullable = true)
        @PositiveOrZero(message = "El orden debe ser cero o positivo.")
        Short orden,

        @Schema(description = "altKey del nuevo padre.", example = "11111111-1111-4111-8111-000000000003", nullable = true)
        String idPadreAltKey
) {}
