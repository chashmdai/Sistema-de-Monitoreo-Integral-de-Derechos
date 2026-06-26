package cl.smid.catalogo.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

/**
 * Proyección de un nodo del árbol de derechos para la API. Es deliberadamente reducido: el
 * identificador público {@code altKey}, los campos descriptivos y los {@link #hijos} anidados.
 * <b>Jamás</b> incluye la PK interna ni la FK del padre (cierre de IDOR, Núcleo 2.2).
 *
 * @param altKey identificador público opaco
 * @param codigo código estable del derecho
 * @param nombre nombre legible
 * @param nivel  profundidad en el árbol (0 = raíz)
 * @param hijos  hijos directos, en orden estable (lista vacía en las hojas)
 */
@Schema(description = "Nodo del arbol anidado de derechos.")
public record DerechoArbolDTO(
        @Schema(description = "Identificador publico opaco.", example = "11111111-1111-4111-8111-000000000003")
        String altKey,
        @Schema(example = "EDU")
        String codigo,
        @Schema(example = "Derecho a la educacion")
        String nombre,
        @Schema(description = "Profundidad en el arbol; 0 es raiz.", example = "0")
        short nivel,
        @Schema(description = "Hijos directos. Ejemplo: EDU.ACCESO bajo EDU.")
        List<DerechoArbolDTO> hijos
) {}
