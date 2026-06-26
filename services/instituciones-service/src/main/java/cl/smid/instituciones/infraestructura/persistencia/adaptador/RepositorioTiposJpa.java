package cl.smid.instituciones.infraestructura.persistencia.adaptador;

import cl.smid.instituciones.dominio.modelo.FiltroTipos;
import cl.smid.instituciones.dominio.modelo.Pagina;
import cl.smid.instituciones.dominio.modelo.Paginado;
import cl.smid.instituciones.dominio.modelo.TipoInstitucion;
import cl.smid.instituciones.dominio.puerto.salida.RepositorioTipos;
import cl.smid.instituciones.infraestructura.persistencia.entidad.TipoInstitucionEntity;
import cl.smid.instituciones.infraestructura.persistencia.jpa.TipoInstitucionJpa;
import cl.smid.instituciones.infraestructura.persistencia.mapeador.MapeadorTipo;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Adaptador de persistencia (JPA) del puerto {@link RepositorioTipos}. Implementa el
 * upsert por {@code alt_key} y los filtros dinámicos del listado mediante
 * {@link Specification}.
 */
@Repository
public class RepositorioTiposJpa implements RepositorioTipos {

    private final TipoInstitucionJpa jpa;
    private final MapeadorTipo mapeador;

    public RepositorioTiposJpa(TipoInstitucionJpa jpa, MapeadorTipo mapeador) {
        this.jpa = jpa;
        this.mapeador = mapeador;
    }

    @Override
    public TipoInstitucion guardar(TipoInstitucion tipo) {
        TipoInstitucionEntity entidad = jpa.findByAltKey(tipo.altKey())
                .orElseGet(TipoInstitucionEntity::new);
        mapeador.copiarADestino(tipo, entidad);
        return mapeador.aDominio(jpa.save(entidad));
    }

    @Override
    public Optional<TipoInstitucion> buscarPorAlt(String altKey) {
        return jpa.findByAltKey(altKey).map(mapeador::aDominio);
    }

    @Override
    public boolean existePorNombre(String nombreNormalizado) {
        return jpa.existsByNombre(nombreNormalizado);
    }

    @Override
    public boolean existePorNombreExcluyendo(String nombreNormalizado, String altKeyExcluido) {
        return jpa.existsByNombreAndAltKeyNot(nombreNormalizado, altKeyExcluido);
    }

    @Override
    public Pagina<TipoInstitucion> listar(FiltroTipos filtro, Paginado paginado) {
        Pageable pageable = PageRequest.of(paginado.pagina(), paginado.tamano(), Sort.by("nombre").ascending());
        Page<TipoInstitucionEntity> pagina = jpa.findAll(especificacion(filtro), pageable);
        List<TipoInstitucion> contenido = pagina.getContent().stream().map(mapeador::aDominio).toList();
        return Pagina.de(contenido, paginado, pagina.getTotalElements());
    }

    private Specification<TipoInstitucionEntity> especificacion(FiltroTipos filtro) {
        return (root, query, cb) -> {
            List<Predicate> predicados = new ArrayList<>();
            if (filtro != null) {
                if (filtro.ambito() != null) {
                    predicados.add(cb.equal(root.get("ambito"), filtro.ambito().name()));
                }
                if (filtro.texto() != null && !filtro.texto().isBlank()) {
                    predicados.add(cb.like(cb.lower(root.get("nombre")),
                            "%" + filtro.texto().trim().toLowerCase() + "%"));
                }
                if (filtro.vigente() != null) {
                    predicados.add(cb.equal(root.get("vigente"), filtro.vigente()));
                }
            }
            return cb.and(predicados.toArray(new Predicate[0]));
        };
    }
}
