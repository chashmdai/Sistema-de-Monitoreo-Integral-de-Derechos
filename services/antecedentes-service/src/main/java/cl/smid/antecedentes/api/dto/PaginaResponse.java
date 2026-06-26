package cl.smid.antecedentes.api.dto;

import cl.smid.antecedentes.dominio.modelo.Pagina;

import java.util.List;
import java.util.function.Function;

/**
 * Sobre de paginacion del API: {@code {contenido, pagina, tamano, totalElementos, totalPaginas}}.
 */
public record PaginaResponse<T>(
        List<T> contenido,
        int pagina,
        int tamano,
        long totalElementos,
        int totalPaginas) {

    /** Proyecta una {@link Pagina} de dominio a su DTO aplicando un mapeo por elemento. */
    public static <D, R> PaginaResponse<R> de(Pagina<D> pagina, Function<D, R> mapeo) {
        List<R> contenido = pagina.contenido().stream().map(mapeo).toList();
        return new PaginaResponse<>(contenido, pagina.pagina(), pagina.tamano(),
                pagina.totalElementos(), pagina.totalPaginas());
    }
}
