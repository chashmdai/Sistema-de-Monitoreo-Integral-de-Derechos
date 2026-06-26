package cl.smid.catalogo.soporte;

import cl.smid.catalogo.dominio.modelo.Causa;
import cl.smid.catalogo.dominio.puerto.salida.CausaRepositorio;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Implementación en memoria de {@link CausaRepositorio} para pruebas unitarias del dominio.
 */
public class RepositorioCausaEnMemoria implements CausaRepositorio {

    private final Map<Long, Causa> porId = new LinkedHashMap<>();
    private final AtomicLong secuencia = new AtomicLong(0);

    @Override
    public List<Causa> causasVigentesDe(Long idDerecho) {
        return porId.values().stream()
                .filter(c -> c.estaVigente() && Objects.equals(c.getIdDerecho(), idDerecho))
                .sorted(Comparator.comparing(Causa::getCodigo))
                .toList();
    }

    @Override
    public boolean existeCodigoEnDerecho(Long idDerecho, String codigo) {
        return porId.values().stream()
                .anyMatch(c -> Objects.equals(c.getIdDerecho(), idDerecho) && c.getCodigo().equals(codigo));
    }

    @Override
    public Causa guardarNueva(Causa causa) {
        long id = secuencia.incrementAndGet();
        causa.asignarId(id);
        porId.put(id, causa);
        return causa;
    }
}
