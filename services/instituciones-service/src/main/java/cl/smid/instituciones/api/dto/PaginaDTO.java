package cl.smid.instituciones.api.dto;

import java.util.List;

/**
 * Sobre de paginación genérico para los listados.
 *
 * @param contenido      elementos de la página
 * @param pagina         índice de página (base 0)
 * @param tamano         tamaño de página
 * @param totalElementos total de elementos del filtro
 * @param totalPaginas   total de páginas
 * @param <T>            tipo de elemento
 */
public record PaginaDTO<T>(
        List<T> contenido,
        int pagina,
        int tamano,
        long totalElementos,
        int totalPaginas) {
}
