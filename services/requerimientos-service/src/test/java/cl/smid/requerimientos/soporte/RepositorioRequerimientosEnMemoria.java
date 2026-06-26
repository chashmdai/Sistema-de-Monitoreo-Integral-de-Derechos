package cl.smid.requerimientos.soporte;

import cl.smid.requerimientos.dominio.modelo.Alcance;
import cl.smid.requerimientos.dominio.modelo.EstadoRequerimiento;
import cl.smid.requerimientos.dominio.modelo.PaginaDominio;
import cl.smid.requerimientos.dominio.modelo.Requerimiento;
import cl.smid.requerimientos.dominio.puerto.salida.RequerimientoRepositorio;
import cl.smid.requerimientos.dominio.servicio.EvaluadorAlcance;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Repositorio de requerimientos en memoria, indexado por alt_key. Replica el contrato del puerto:
 * el listado aplica los filtros opcionales (estado/unidad) y el recorte territorial registro a
 * registro mediante {@link EvaluadorAlcance}, y ordena por fecha de creación descendente.
 */
public class RepositorioRequerimientosEnMemoria implements RequerimientoRepositorio {

    private final Map<String, Requerimiento> almacen = new ConcurrentHashMap<>();

    @Override
    public Requerimiento guardar(Requerimiento requerimiento) {
        almacen.put(requerimiento.altKey(), requerimiento);
        return requerimiento;
    }

    @Override
    public Optional<Requerimiento> buscarPorAltKey(String altKey) {
        return Optional.ofNullable(almacen.get(altKey));
    }

    @Override
    public PaginaDominio<Requerimiento> listar(EstadoRequerimiento estado, String unidadDestinoAlt,
                                               Alcance alcance, String sedeUsuarioAlt,
                                               String unidadUsuarioAlt, int pagina, int tamano) {
        List<Requerimiento> filtrados = almacen.values().stream()
                .filter(Requerimiento::vigente)
                .filter(r -> estado == null || r.estado() == estado)
                .filter(r -> unidadDestinoAlt == null || unidadDestinoAlt.equals(r.idUnidadDestinoAlt()))
                .filter(r -> EvaluadorAlcance.puedeVer(alcance, sedeUsuarioAlt, unidadUsuarioAlt,
                        r.idSedeAlt(), r.idUnidadDestinoAlt()))
                .sorted(Comparator.comparing(Requerimiento::creadoEn).reversed())
                .collect(Collectors.toList());

        long total = filtrados.size();
        int desde = Math.min(pagina * tamano, filtrados.size());
        int hasta = Math.min(desde + tamano, filtrados.size());
        List<Requerimiento> pagit = filtrados.subList(desde, hasta);
        return new PaginaDominio<>(pagit, pagina, tamano, total);
    }
}
