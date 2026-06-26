package cl.smid.casos.dominio.modelo;

import java.util.List;
import java.util.Objects;

/**
 * Envoltorio de paginación del dominio (independiente de Spring Data). Se traduce en la capa API al
 * envoltorio JSON {@code { contenido, pagina, tamano, total }} (Núcleo 2.10).
 *
 * @param contenido elementos de la página actual.
 * @param pagina    índice de página (base 0).
 * @param tamano    tamaño de página solicitado.
 * @param total     total de elementos que satisfacen el filtro.
 * @param <T>       tipo de elemento.
 */
public record Pagina<T>(List<T> contenido, int pagina, int tamano, long total) {

    public Pagina {
        Objects.requireNonNull(contenido, "contenido");
        contenido = List.copyOf(contenido);
    }
}
