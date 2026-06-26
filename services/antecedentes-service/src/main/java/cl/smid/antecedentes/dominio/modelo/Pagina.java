package cl.smid.antecedentes.dominio.modelo;

import java.util.List;

/**
 * Sobre de paginacion del dominio. El adaptador de persistencia lo construye a partir del
 * resultado de la consulta; el API lo proyecta a {@code {contenido, pagina, tamano,
 * totalElementos, totalPaginas}}.
 *
 * @param contenido      elementos de la pagina
 * @param pagina         indice de pagina (base 0)
 * @param tamano         tamano de pagina solicitado
 * @param totalElementos total de elementos que cumplen el filtro
 * @param <T>            tipo del contenido
 */
public record Pagina<T>(List<T> contenido, int pagina, int tamano, long totalElementos) {

    public Pagina {
        contenido = (contenido == null) ? List.of() : List.copyOf(contenido);
    }

    /** Total de paginas segun el tamano solicitado (al menos 0). */
    public int totalPaginas() {
        if (tamano <= 0) {
            return 0;
        }
        return (int) Math.ceil((double) totalElementos / (double) tamano);
    }
}
