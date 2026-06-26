package cl.smid.personas.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

/**
 * Sobre de paginación genérico para respuestas de listado. Espeja el record de dominio
 * {@code Pagina<T>} en la frontera HTTP con una forma estable y predecible para los clientes.
 *
 * @param contenido elementos de la página actual
 * @param pagina    número de página (base 0)
 * @param tamano    tamaño de página solicitado
 * @param total     total de elementos que cumplen el criterio (todas las páginas)
 * @param <T>       tipo de elemento de la página
 */
@Schema(description = "Respuesta paginada.")
public record PaginaResponse<T>(
        @Schema(description = "Elementos de la pagina actual.")
        List<T> contenido,
        @Schema(example = "0")
        int pagina,
        @Schema(example = "20")
        int tamano,
        @Schema(example = "1")
        long total) {
}
