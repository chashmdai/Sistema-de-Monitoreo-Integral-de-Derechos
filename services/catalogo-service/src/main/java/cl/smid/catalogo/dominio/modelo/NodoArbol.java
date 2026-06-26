package cl.smid.catalogo.dominio.modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Nodo del <b>árbol taxonómico ya ensamblado en memoria</b>.
 *
 * <p>Mientras {@link Derecho} es una fila (lista de adyacencia con {@code idPadre}),
 * {@code NodoArbol} es la representación jerárquica anidada que produce el
 * {@code EnsambladorArbol} a partir de la lista plana recuperada por la CTE recursiva.
 * Cada nodo envuelve su {@link Derecho} y la lista (ordenada y estable) de sus
 * {@link #hijos}. El adaptador de API lo proyecta a {@code DerechoArbolDTO} sin exponer
 * jamás las llaves internas.</p>
 */
public class NodoArbol {

    private final Derecho derecho;
    private final List<NodoArbol> hijos = new ArrayList<>();

    public NodoArbol(Derecho derecho) {
        this.derecho = derecho;
    }

    /** Agrega un hijo (lo usa el ensamblador durante la construcción recursiva). */
    public void agregarHijo(NodoArbol hijo) {
        hijos.add(hijo);
    }

    public Derecho getDerecho() { return derecho; }

    /** Hijos directos, en orden estable (orden, luego código). Vista de solo lectura. */
    public List<NodoArbol> getHijos() {
        return Collections.unmodifiableList(hijos);
    }

    /** Conveniencia: cantidad total de descendientes (incluyendo nietos, etc.). */
    public int contarDescendientes() {
        int total = hijos.size();
        for (NodoArbol h : hijos) {
            total += h.contarDescendientes();
        }
        return total;
    }
}
