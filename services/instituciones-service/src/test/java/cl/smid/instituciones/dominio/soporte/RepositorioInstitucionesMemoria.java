package cl.smid.instituciones.dominio.soporte;

import cl.smid.instituciones.dominio.modelo.FiltroInstituciones;
import cl.smid.instituciones.dominio.modelo.Institucion;
import cl.smid.instituciones.dominio.modelo.Pagina;
import cl.smid.instituciones.dominio.modelo.Paginado;
import cl.smid.instituciones.dominio.modelo.ResumenInstitucion;
import cl.smid.instituciones.dominio.modelo.TipoInstitucion;
import cl.smid.instituciones.dominio.puerto.salida.RepositorioInstituciones;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Implementación en memoria de {@link RepositorioInstituciones} para pruebas. Resuelve el
 * nombre y ámbito del tipo apoyándose en un {@link RepositorioTiposMemoria}, igual que el
 * adaptador real consulta la tabla de tipos.
 */
public class RepositorioInstitucionesMemoria implements RepositorioInstituciones {

    private final Map<String, Institucion> porAlt = new LinkedHashMap<>();
    private final RepositorioTiposMemoria tipos;

    public RepositorioInstitucionesMemoria(RepositorioTiposMemoria tipos) {
        this.tipos = tipos;
    }

    @Override
    public Institucion guardar(Institucion institucion) {
        porAlt.put(institucion.altKey(), institucion);
        return institucion;
    }

    @Override
    public Optional<Institucion> buscarPorAlt(String altKey) {
        return Optional.ofNullable(porAlt.get(altKey));
    }

    @Override
    public Pagina<ResumenInstitucion> listar(FiltroInstituciones filtro, Paginado paginado) {
        List<ResumenInstitucion> coincidencias = new ArrayList<>();
        for (Institucion i : porAlt.values()) {
            TipoInstitucion tipo = tipos.buscarPorAlt(i.tipoAlt()).orElse(null);
            if (!coincide(i, tipo, filtro)) {
                continue;
            }
            if (tipo != null) {
                coincidencias.add(new ResumenInstitucion(i, tipo.nombre(), tipo.ambito()));
            }
        }
        int desde = Math.min(paginado.pagina() * paginado.tamano(), coincidencias.size());
        int hasta = Math.min(desde + paginado.tamano(), coincidencias.size());
        return Pagina.de(coincidencias.subList(desde, hasta), paginado, coincidencias.size());
    }

    private boolean coincide(Institucion i, TipoInstitucion tipo, FiltroInstituciones filtro) {
        if (filtro == null) {
            return true;
        }
        if (filtro.tipoAlt() != null && !filtro.tipoAlt().isBlank() && !filtro.tipoAlt().equals(i.tipoAlt())) {
            return false;
        }
        if (filtro.ambito() != null && (tipo == null || tipo.ambito() != filtro.ambito())) {
            return false;
        }
        if (filtro.texto() != null && !filtro.texto().isBlank()
                && !i.nombre().toLowerCase().contains(filtro.texto().trim().toLowerCase())) {
            return false;
        }
        if (filtro.regionCodigo() != null && !filtro.regionCodigo().isBlank()
                && !filtro.regionCodigo().equals(i.regionCodigo())) {
            return false;
        }
        if (filtro.activa() != null && i.activa() != filtro.activa()) {
            return false;
        }
        if (filtro.rut() != null && !filtro.rut().equals(i.rut())) {
            return false;
        }
        return true;
    }

    @Override
    public boolean existePorCodigo(String codigo) {
        return porAlt.values().stream().anyMatch(i -> codigo.equals(i.codigo()));
    }

    @Override
    public boolean existePorCodigoExcluyendo(String codigo, String altKeyExcluido) {
        return porAlt.values().stream()
                .anyMatch(i -> codigo.equals(i.codigo()) && !i.altKey().equals(altKeyExcluido));
    }
}
