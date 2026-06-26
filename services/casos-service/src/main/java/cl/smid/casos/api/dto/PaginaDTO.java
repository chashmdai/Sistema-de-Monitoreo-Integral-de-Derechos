package cl.smid.casos.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * Envoltorio de paginación de la API: {@code { contenido, pagina, tamano, total }} (Núcleo 2.10).
 */
@Schema(description = "Página de resultados de la API.")
public record PaginaDTO<T>(
        @Schema(description = "Contenido de la página.")
        List<T> contenido,
        @Schema(description = "Número de página, base cero.", example = "0")
        int pagina,
        @Schema(description = "Tamaño de página solicitado.", example = "20")
        int tamano,
        @Schema(description = "Total de elementos encontrados.", example = "42")
        long total) {
}
