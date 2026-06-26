package cl.smid.instituciones.dominio.soporte;

import cl.smid.instituciones.dominio.modelo.FiltroTipos;
import cl.smid.instituciones.dominio.modelo.Pagina;
import cl.smid.instituciones.dominio.modelo.Paginado;
import cl.smid.instituciones.dominio.modelo.TipoInstitucion;
import cl.smid.instituciones.dominio.puerto.salida.RepositorioTipos;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Implementación en memoria de {@link RepositorioTipos} para pruebas unitarias del
 * dominio (sin Spring ni base de datos).
 */
public class RepositorioTiposMemoria implements RepositorioTipos {

    private final Map<String, TipoInstitucion> porAlt = new LinkedHashMap<>();

    @Override
    public TipoInstitucion guardar(TipoInstitucion tipo) {
        porAlt.put(tipo.altKey(), tipo);
        return tipo;
    }

    @Override
    public Optional<TipoInstitucion> buscarPorAlt(String altKey) {
        return Optional.ofNullable(porAlt.get(altKey));
    }

    @Override
    public boolean existePorNombre(String nombreNormalizado) {
        return porAlt.values().stream().anyMatch(t -> t.nombre().equalsIgnoreCase(nombreNormalizado));
    }

    @Override
    public boolean existePorNombreExcluyendo(String nombreNormalizado, String altKeyExcluido) {
        return porAlt.values().stream()
                .anyMatch(t -> t.nombre().equalsIgnoreCase(nombreNormalizado)
                        && !t.altKey().equals(altKeyExcluido));
    }

    @Override
    public Pagina<TipoInstitucion> listar(FiltroTipos filtro, Paginado paginado) {
        List<TipoInstitucion> coincidencias = new ArrayList<>();
        for (TipoInstitucion t : porAlt.values()) {
            if (filtro != null) {
                if (filtro.ambito() != null && t.ambito() != filtro.ambito()) {
                    continue;
                }
                if (filtro.texto() != null && !filtro.texto().isBlank()
                        && !t.nombre().toLowerCase().contains(filtro.texto().trim().toLowerCase())) {
                    continue;
                }
                if (filtro.vigente() != null && t.vigente() != filtro.vigente()) {
                    continue;
                }
            }
            coincidencias.add(t);
        }
        return paginar(coincidencias, paginado);
    }

    private Pagina<TipoInstitucion> paginar(List<TipoInstitucion> todos, Paginado paginado) {
        int desde = Math.min(paginado.pagina() * paginado.tamano(), todos.size());
        int hasta = Math.min(desde + paginado.tamano(), todos.size());
        return Pagina.de(todos.subList(desde, hasta), paginado, todos.size());
    }
}
