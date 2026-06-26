package cl.smid.requerimientos.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

/**
 * Envoltorio de paginación de la API.
 *
 * @param <T>       tipo de elemento
 * @param contenido elementos de la página
 * @param pagina    índice de página (base 0)
 * @param tamano    tamaño de página
 * @param total     total de elementos en todas las páginas
 */
@Schema(description = "Respuesta paginada.")
public record PaginaDTO<T>(
        @Schema(description = "Elementos de la pagina actual.")
        List<T> contenido,
        @Schema(example = "0")
        int pagina,
        @Schema(example = "20")
        int tamano,
        @Schema(example = "1")
        long total) {
}
