package cl.smid.catalogo.dominio;

import cl.smid.catalogo.dominio.modelo.Derecho;
import cl.smid.catalogo.dominio.modelo.NodoArbol;
import cl.smid.catalogo.dominio.servicio.EnsambladorArbol;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Verifica el ensamblado del árbol anidado a partir de la lista plana ordenada.
 */
class EnsambladorArbolTest {

    private final EnsambladorArbol ensamblador = new EnsambladorArbol();
    private static final LocalDate HOY = LocalDate.of(2026, 6, 13);

    private static Derecho fila(long id, Long idPadre, String codigo, short nivel, short orden) {
        return new Derecho(id, idPadre, "ak-" + id, codigo, "Nombre " + codigo, null,
                nivel, orden, true, HOY, null);
    }

    @Test
    @DisplayName("Anida hijos bajo sus padres y conserva el orden estable de entrada")
    void anidaYConservaOrden() {
        // Entrada ya ordenada por (nivel, orden, codigo), como la entrega la CTE.
        List<Derecho> planos = List.of(
                fila(1, null, "A", (short) 0, (short) 1),
                fila(2, null, "B", (short) 0, (short) 2),
                fila(3, 1L, "A.1", (short) 1, (short) 1),
                fila(4, 1L, "A.2", (short) 1, (short) 2),
                fila(5, 3L, "A.1.1", (short) 2, (short) 1));

        List<NodoArbol> raices = ensamblador.ensamblar(planos);

        assertThat(raices).hasSize(2);
        NodoArbol a = raices.get(0);
        NodoArbol b = raices.get(1);
        assertThat(a.getDerecho().getCodigo()).isEqualTo("A");
        assertThat(b.getDerecho().getCodigo()).isEqualTo("B");

        // A tiene dos hijos en orden A.1, A.2; A.1 tiene un hijo A.1.1.
        assertThat(a.getHijos()).extracting(n -> n.getDerecho().getCodigo())
                .containsExactly("A.1", "A.2");
        assertThat(a.getHijos().get(0).getHijos()).extracting(n -> n.getDerecho().getCodigo())
                .containsExactly("A.1.1");
        assertThat(b.getHijos()).isEmpty();

        // Conteo total de descendientes de A: A.1, A.2, A.1.1 = 3.
        assertThat(a.contarDescendientes()).isEqualTo(3);
    }

    @Test
    @DisplayName("Un hijo cuyo padre no está en el conjunto se preserva como raíz (defensivo)")
    void hijoHuerfanoSePreservaComoRaiz() {
        List<Derecho> planos = List.of(
                fila(1, null, "A", (short) 0, (short) 1),
                fila(9, 99L, "X", (short) 1, (short) 1)); // padre 99 inexistente

        List<NodoArbol> raices = ensamblador.ensamblar(planos);

        assertThat(raices).extracting(n -> n.getDerecho().getCodigo())
                .containsExactlyInAnyOrder("A", "X");
    }

    @Test
    @DisplayName("Lista vacía produce bosque vacío")
    void listaVacia() {
        assertThat(ensamblador.ensamblar(List.of())).isEmpty();
    }
}
