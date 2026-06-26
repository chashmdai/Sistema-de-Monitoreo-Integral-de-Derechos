package cl.smid.catalogo.soporte;

import cl.smid.catalogo.dominio.modelo.Derecho;
import cl.smid.catalogo.dominio.puerto.salida.DerechoRepositorio;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Implementación en memoria de {@link DerechoRepositorio} para pruebas unitarias del dominio.
 *
 * <p>Permite ejercitar toda la lógica de {@code ServicioCatalogo} sin levantar Spring ni una
 * base de datos. Reproduce el comportamiento relevante: asignación de id, recorrido del árbol
 * (descendientes, hijos directos), unicidad de código y búsqueda acento- e insensible a
 * mayúsculas (normalizando los textos, como haría la colación {@code utf8mb4_0900_ai_ci}).</p>
 */
public class RepositorioDerechoEnMemoria implements DerechoRepositorio {

    private static final Comparator<Derecho> ORDEN_ESTABLE =
            Comparator.comparingInt(Derecho::getNivel)
                    .thenComparingInt(Derecho::getOrden)
                    .thenComparing(Derecho::getCodigo);

    private final Map<Long, Derecho> porId = new LinkedHashMap<>();
    private final AtomicLong secuencia = new AtomicLong(0);

    @Override
    public List<Derecho> cargarArbolVigente() {
        return vigentesOrdenados();
    }

    @Override
    public List<Derecho> cargarTodosVigentes() {
        return vigentesOrdenados();
    }

    @Override
    public List<Derecho> descendientesVigentes(Long idRaiz) {
        List<Derecho> resultado = new ArrayList<>();
        recolectarDescendientes(idRaiz, resultado, true);
        resultado.sort(ORDEN_ESTABLE);
        return resultado;
    }

    @Override
    public List<Long> idsDescendientes(Long idRaiz) {
        List<Derecho> acumulado = new ArrayList<>();
        recolectarDescendientes(idRaiz, acumulado, false);
        return acumulado.stream().map(Derecho::getId).toList();
    }

    @Override
    public List<Derecho> hijosDirectosVigentes(Long idPadre) {
        return porId.values().stream()
                .filter(d -> d.estaVigente() && java.util.Objects.equals(d.getIdPadre(), idPadre))
                .sorted(Comparator.comparingInt(Derecho::getOrden).thenComparing(Derecho::getCodigo))
                .toList();
    }

    @Override
    public Optional<Derecho> buscarPorAltKey(String altKey) {
        return porId.values().stream().filter(d -> d.getAltKey().equals(altKey)).findFirst();
    }

    @Override
    public Optional<Derecho> buscarPorCodigo(String codigo) {
        return porId.values().stream().filter(d -> d.getCodigo().equals(codigo)).findFirst();
    }

    @Override
    public boolean existeCodigo(String codigo) {
        return porId.values().stream().anyMatch(d -> d.getCodigo().equals(codigo));
    }

    @Override
    public List<Derecho> buscarPorTexto(String texto) {
        String aguja = normalizar(texto);
        return porId.values().stream()
                .filter(Derecho::estaVigente)
                .filter(d -> normalizar(d.getNombre()).contains(aguja)
                        || normalizar(d.getCodigo()).contains(aguja))
                .sorted(ORDEN_ESTABLE)
                .toList();
    }

    @Override
    public Derecho guardarNuevo(Derecho derecho) {
        long id = secuencia.incrementAndGet();
        derecho.asignarId(id);
        porId.put(id, derecho);
        return derecho;
    }

    @Override
    public void actualizar(Derecho derecho) {
        // Las instancias se comparten por referencia: el estado ya está actualizado.
        porId.put(derecho.getId(), derecho);
    }

    @Override
    public void actualizarTodos(List<Derecho> derechos) {
        derechos.forEach(this::actualizar);
    }

    // --------------------------- utilidades de prueba ---------------------------

    private List<Derecho> vigentesOrdenados() {
        return porId.values().stream().filter(Derecho::estaVigente).sorted(ORDEN_ESTABLE).toList();
    }

    private void recolectarDescendientes(Long idRaiz, List<Derecho> acumulado, boolean soloVigentes) {
        for (Derecho d : porId.values()) {
            if (java.util.Objects.equals(d.getIdPadre(), idRaiz)) {
                if (!soloVigentes || d.estaVigente()) {
                    acumulado.add(d);
                }
                recolectarDescendientes(d.getId(), acumulado, soloVigentes);
            }
        }
    }

    /** Quita acentos y pasa a minúsculas, emulando la colación acento-insensible. */
    private static String normalizar(String valor) {
        String sinAcentos = Normalizer.normalize(valor, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        return sinAcentos.toLowerCase();
    }
}
