package cl.smid.requerimientos.dominio.modelo;

import java.util.List;
import java.util.Objects;

/**
 * Página de resultados de dominio (envoltorio de paginación neutral, sin dependencia de Spring).
 * Mantiene el dominio independiente de {@code org.springframework.data.domain.Page}.
 *
 * @param <T>        tipo de elemento de la página
 * @param contenido  elementos de la página actual
 * @param pagina     índice de página (base 0)
 * @param tamano     tamaño de página solicitado
 * @param total      total de elementos que satisfacen el criterio (en todas las páginas)
 */
public record PaginaDominio<T>(List<T> contenido, int pagina, int tamano, long total) {

    public PaginaDominio {
        Objects.requireNonNull(contenido, "El contenido de la página no puede ser nulo");
        contenido = List.copyOf(contenido);
    }
}
