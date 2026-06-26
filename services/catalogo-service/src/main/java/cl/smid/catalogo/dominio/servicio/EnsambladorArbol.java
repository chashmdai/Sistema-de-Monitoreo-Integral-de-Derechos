package cl.smid.catalogo.dominio.servicio;

import cl.smid.catalogo.dominio.modelo.Derecho;
import cl.smid.catalogo.dominio.modelo.NodoArbol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Ensambla el <b>árbol taxonómico anidado</b> a partir de la lista plana de derechos que
 * entrega la consulta recursiva ({@code WITH RECURSIVE}) del repositorio.
 *
 * <h2>Diseño del algoritmo</h2>
 * <p>La base de datos resuelve eficientemente el <i>recorrido</i> (qué filas pertenecen al
 * árbol y en qué orden estable: {@code nivel, orden, codigo}); el ensamblado de la
 * estructura anidada se hace aquí en memoria, en <b>O(n)</b> y sin tocar la base:</p>
 * <ol>
 *   <li>Primer recorrido: se crea un {@link NodoArbol} por cada derecho y se indexa por su
 *       id interno ({@code id → nodo}).</li>
 *   <li>Segundo recorrido (en el mismo orden de entrada, que ya viene ordenado): cada nodo
 *       sin padre es una raíz; cada nodo con padre se cuelga del nodo padre. Como se itera
 *       en orden estable, los hermanos quedan en el orden correcto sin volver a ordenar.</li>
 * </ol>
 *
 * <p>El doble recorrido hace al ensamblador <b>independiente del orden de llegada</b>: aunque
 * un hijo apareciera antes que su padre, el enlace se resuelve igual. Si un nodo referencia
 * un padre que no está en el conjunto (situación que la CTE sobre el conjunto vigente no
 * produce, pero que aquí se contempla de forma defensiva), ese nodo se trata como raíz para
 * no perderlo del resultado.</p>
 *
 * <p>Es un componente del núcleo: POJO sin estado y sin dependencias de framework.</p>
 */
public class EnsambladorArbol {

    /**
     * Construye el bosque (lista de raíces) a partir de los derechos ya ordenados.
     *
     * @param derechosOrdenados derechos en orden estable ({@code nivel, orden, codigo})
     * @return raíces del árbol, cada una con sus hijos anidados en orden estable
     */
    public List<NodoArbol> ensamblar(List<Derecho> derechosOrdenados) {
        // Índice id interno → nodo, para enlazar en O(1) por nodo.
        Map<Long, NodoArbol> indicePorId = new HashMap<>(derechosOrdenados.size() * 2);
        for (Derecho d : derechosOrdenados) {
            indicePorId.put(d.getId(), new NodoArbol(d));
        }

        List<NodoArbol> raices = new ArrayList<>();
        for (Derecho d : derechosOrdenados) {
            NodoArbol nodo = indicePorId.get(d.getId());
            Long idPadre = d.getIdPadre();
            if (idPadre == null) {
                raices.add(nodo);
                continue;
            }
            NodoArbol padre = indicePorId.get(idPadre);
            if (padre != null) {
                padre.agregarHijo(nodo);
            } else {
                // Padre fuera del conjunto: se preserva como raíz para no perder el nodo.
                raices.add(nodo);
            }
        }
        return raices;
    }
}
