package cl.smid.personas.dominio.modelo;

import java.util.Collections;
import java.util.List;

/**
 * Página genérica de resultados, independiente del framework de persistencia. El dominio
 * devuelve este record y la capa {@code api} lo proyecta al DTO de paginación.
 *
 * @param contenido elementos de la página (lista inmutable)
 * @param pagina    número de página solicitado (base 0)
 * @param tamano    tamaño de página solicitado
 * @param total     total de elementos que cumplen el criterio (en todas las páginas)
 * @param <T>       tipo de elemento
 */
public record Pagina<T>(List<T> contenido, int pagina, int tamano, long total) {

    public Pagina {
        contenido = (contenido == null) ? Collections.emptyList() : List.copyOf(contenido);
    }

    /** Página vacía conservando los parámetros de paginación. */
    public static <T> Pagina<T> vacia(int pagina, int tamano) {
        return new Pagina<>(Collections.emptyList(), pagina, tamano, 0L);
    }
}
