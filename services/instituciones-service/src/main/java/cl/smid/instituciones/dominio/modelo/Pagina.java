package cl.smid.instituciones.dominio.modelo;

import java.util.List;

/**
 * Página de resultados genérica del dominio (sin dependencia de Spring Data).
 *
 * @param contenido       elementos de la página actual
 * @param pagina          índice de página, base 0
 * @param tamano          tamaño de página solicitado
 * @param totalElementos  total de elementos que satisfacen el filtro
 * @param totalPaginas    total de páginas disponibles
 * @param <T>             tipo de elemento de la página
 */
public record Pagina<T>(
        List<T> contenido,
        int pagina,
        int tamano,
        long totalElementos,
        int totalPaginas) {

    public Pagina {
        contenido = contenido == null ? List.of() : List.copyOf(contenido);
    }

    /**
     * Crea una página calculando el total de páginas a partir del total de elementos
     * y el tamaño solicitado.
     *
     * @param contenido      elementos de la página
     * @param solicitud      paginación solicitada
     * @param totalElementos total de elementos del filtro
     * @param <T>            tipo de elemento
     * @return la página construida
     */
    public static <T> Pagina<T> de(List<T> contenido, Paginado solicitud, long totalElementos) {
        int totalPaginas = solicitud.tamano() == 0
                ? 0
                : (int) Math.ceil((double) totalElementos / solicitud.tamano());
        return new Pagina<>(contenido, solicitud.pagina(), solicitud.tamano(), totalElementos, totalPaginas);
    }

    /**
     * Transforma el contenido de la página preservando los metadatos de paginación.
     *
     * @param transformador función de mapeo elemento a elemento
     * @param <R>           tipo destino
     * @return una nueva página con el contenido transformado
     */
    public <R> Pagina<R> mapear(java.util.function.Function<T, R> transformador) {
        List<R> destino = contenido.stream().map(transformador).toList();
        return new Pagina<>(destino, pagina, tamano, totalElementos, totalPaginas);
    }
}
