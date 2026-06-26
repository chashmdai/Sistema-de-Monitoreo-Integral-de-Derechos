package cl.smid.catalogo.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

/**
 * Proyección de detalle de un derecho: añade {@code descripcion} y {@code vigente} a los
 * campos básicos, e incluye sus hijos directos vigentes como {@link DerechoArbolDTO}.
 *
 * @param altKey      identificador público opaco
 * @param codigo      código estable del derecho
 * @param nombre      nombre legible
 * @param nivel       profundidad en el árbol
 * @param descripcion descripción (puede ser nula)
 * @param vigente     si el derecho está vigente (un derecho dado de baja sigue siendo consultable)
 * @param hijos       hijos directos vigentes, en orden estable
 */
@Schema(description = "Detalle publico de un derecho.")
public record DerechoDetalleDTO(
        @Schema(description = "Identificador publico opaco.", example = "11111111-1111-4111-8111-000000000003")
        String altKey,
        @Schema(example = "EDU")
        String codigo,
        @Schema(example = "Derecho a la educacion")
        String nombre,
        @Schema(example = "0")
        short nivel,
        @Schema(example = "Acceso, permanencia y calidad en la educacion.", nullable = true)
        String descripcion,
        @Schema(example = "true")
        boolean vigente,
        @Schema(description = "Hijos directos vigentes.")
        List<DerechoArbolDTO> hijos
) {}
