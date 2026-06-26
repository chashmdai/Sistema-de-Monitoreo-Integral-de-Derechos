package cl.smid.catalogo.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

/**
 * Solicitud de creación de un derecho. La validación de formato (obligatoriedad, longitudes)
 * vive aquí, en la frontera; las reglas de negocio (unicidad de código, validez del árbol)
 * las verifica el dominio. Los límites de longitud coinciden con las columnas de la base.
 *
 * @param idPadreAltKey alt_key del padre; nulo o vacío ⇒ se crea una categoría raíz
 * @param codigo        código estable e inmutable (obligatorio, ≤ 40)
 * @param nombre        nombre legible (obligatorio, ≤ 200)
 * @param descripcion   descripción opcional (≤ 600)
 * @param orden         orden de despliegue entre hermanos (opcional, ≥ 0)
 */
@Schema(description = "Solicitud de creacion de derecho. idPadreAltKey nulo crea una raiz.")
public record CrearDerechoRequest(
        @Schema(description = "altKey del padre; nulo crea raiz.", example = "11111111-1111-4111-8111-000000000003", nullable = true)
        String idPadreAltKey,

        @Schema(description = "Codigo estable e inmutable.", example = "EDU.INCLUSION", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "El código es obligatorio.")
        @Size(max = 40, message = "El código no puede superar los 40 caracteres.")
        String codigo,

        @Schema(example = "Inclusion educativa", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "El nombre es obligatorio.")
        @Size(max = 200, message = "El nombre no puede superar los 200 caracteres.")
        String nombre,

        @Schema(example = "Medidas de acceso, inclusion y permanencia.", nullable = true)
        @Size(max = 600, message = "La descripción no puede superar los 600 caracteres.")
        String descripcion,

        @Schema(description = "Orden entre hermanos.", example = "3", nullable = true)
        @PositiveOrZero(message = "El orden debe ser cero o positivo.")
        Short orden
) {}
