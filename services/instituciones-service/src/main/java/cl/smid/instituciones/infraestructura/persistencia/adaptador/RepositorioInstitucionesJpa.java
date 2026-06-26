package cl.smid.instituciones.infraestructura.persistencia.adaptador;

import cl.smid.instituciones.dominio.modelo.Ambito;
import cl.smid.instituciones.dominio.modelo.FiltroInstituciones;
import cl.smid.instituciones.dominio.modelo.Institucion;
import cl.smid.instituciones.dominio.modelo.Pagina;
import cl.smid.instituciones.dominio.modelo.Paginado;
import cl.smid.instituciones.dominio.modelo.ResumenInstitucion;
import cl.smid.instituciones.dominio.excepcion.RecursoNoEncontradoException;
import cl.smid.instituciones.dominio.excepcion.ReglaNegocioException;
import cl.smid.instituciones.dominio.puerto.salida.RepositorioInstituciones;
import cl.smid.instituciones.infraestructura.persistencia.entidad.InstitucionEntity;
import cl.smid.instituciones.infraestructura.persistencia.entidad.TipoInstitucionEntity;
import cl.smid.instituciones.infraestructura.persistencia.jpa.InstitucionJpa;
import cl.smid.instituciones.infraestructura.persistencia.jpa.TipoInstitucionJpa;
import cl.smid.instituciones.infraestructura.persistencia.mapeador.MapeadorInstitucion;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Adaptador de persistencia (JPA) del puerto {@link RepositorioInstituciones}.
 *
 * <p>Resuelve la traducción {@code tipoAlt <-> tipo_id} en el guardado y la carga; en el
 * listado construye los filtros con {@link Specification}, usando subconsultas sobre
 * {@code tipo_institucion} para los criterios por tipo o por ámbito (la relación se
 * guarda por PK escalar), y carga por lotes los tipos de la página para evitar N+1.</p>
 */
@Repository
public class RepositorioInstitucionesJpa implements RepositorioInstituciones {

    private final InstitucionJpa jpa;
    private final TipoInstitucionJpa tipoJpa;
    private final MapeadorInstitucion mapeador;

    public RepositorioInstitucionesJpa(InstitucionJpa jpa, TipoInstitucionJpa tipoJpa,
                                       MapeadorInstitucion mapeador) {
        this.jpa = jpa;
        this.tipoJpa = tipoJpa;
        this.mapeador = mapeador;
    }

    @Override
    public Institucion guardar(Institucion institucion) {
        Long tipoId = tipoJpa.findByAltKey(institucion.tipoAlt())
                .map(TipoInstitucionEntity::getId)
                .orElseThrow(() -> new ReglaNegocioException(
                        "El tipo de institución '" + institucion.tipoAlt() + "' no existe."));
        InstitucionEntity entidad = jpa.findByAltKey(institucion.altKey())
                .orElseGet(InstitucionEntity::new);
        mapeador.copiarADestino(institucion, tipoId, entidad);
        InstitucionEntity guardada = jpa.save(entidad);
        return mapeador.aDominio(guardada, institucion.tipoAlt());
    }

    @Override
    public Optional<Institucion> buscarPorAlt(String altKey) {
        return jpa.findByAltKey(altKey)
                .map(entidad -> mapeador.aDominio(entidad, altKeyDelTipo(entidad.getTipoId())));
    }

    @Override
    public Pagina<ResumenInstitucion> listar(FiltroInstituciones filtro, Paginado paginado) {
        Pageable pageable = PageRequest.of(paginado.pagina(), paginado.tamano(), Sort.by("nombre").ascending());
        Page<InstitucionEntity> pagina = jpa.findAll(especificacion(filtro), pageable);

        Set<Long> tipoIds = pagina.getContent().stream()
                .map(InstitucionEntity::getTipoId)
                .collect(Collectors.toSet());
        Map<Long, TipoInstitucionEntity> tiposPorId = tipoIds.isEmpty()
                ? Map.of()
                : tipoJpa.findByIdIn(tipoIds).stream()
                    .collect(Collectors.toMap(TipoInstitucionEntity::getId, Function.identity()));

        List<ResumenInstitucion> contenido = pagina.getContent().stream().map(entidad -> {
            TipoInstitucionEntity tipo = tiposPorId.get(entidad.getTipoId());
            if (tipo == null) {
                throw new RecursoNoEncontradoException(
                        "El tipo asociado a la institución '" + entidad.getAltKey() + "' no existe.");
            }
            Institucion dominio = mapeador.aDominio(entidad, tipo.getAltKey());
            return new ResumenInstitucion(dominio, tipo.getNombre(), Ambito.valueOf(tipo.getAmbito()));
        }).toList();

        return Pagina.de(contenido, paginado, pagina.getTotalElements());
    }

    @Override
    public boolean existePorCodigo(String codigo) {
        return jpa.existsByCodigo(codigo);
    }

    @Override
    public boolean existePorCodigoExcluyendo(String codigo, String altKeyExcluido) {
        return jpa.existsByCodigoAndAltKeyNot(codigo, altKeyExcluido);
    }

    private String altKeyDelTipo(Long tipoId) {
        return tipoJpa.findById(tipoId)
                .map(TipoInstitucionEntity::getAltKey)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "El tipo de institución asociado no existe."));
    }

    private Specification<InstitucionEntity> especificacion(FiltroInstituciones filtro) {
        return (root, query, cb) -> {
            List<Predicate> predicados = new ArrayList<>();
            if (filtro != null) {
                if (filtro.tipoAlt() != null && !filtro.tipoAlt().isBlank()) {
                    Subquery<Long> sub = query.subquery(Long.class);
                    Root<TipoInstitucionEntity> t = sub.from(TipoInstitucionEntity.class);
                    sub.select(t.get("id")).where(cb.equal(t.get("altKey"), filtro.tipoAlt()));
                    predicados.add(root.get("tipoId").in(sub));
                }
                if (filtro.ambito() != null) {
                    Subquery<Long> sub = query.subquery(Long.class);
                    Root<TipoInstitucionEntity> t = sub.from(TipoInstitucionEntity.class);
                    sub.select(t.get("id")).where(cb.equal(t.get("ambito"), filtro.ambito().name()));
                    predicados.add(root.get("tipoId").in(sub));
                }
                if (filtro.texto() != null && !filtro.texto().isBlank()) {
                    predicados.add(cb.like(cb.lower(root.get("nombre")),
                            "%" + filtro.texto().trim().toLowerCase() + "%"));
                }
                if (filtro.regionCodigo() != null && !filtro.regionCodigo().isBlank()) {
                    predicados.add(cb.equal(root.get("regionCodigo"), filtro.regionCodigo().trim()));
                }
                if (filtro.activa() != null) {
                    predicados.add(cb.equal(root.get("activa"), filtro.activa()));
                }
                if (filtro.rut() != null) {
                    predicados.add(cb.equal(root.get("rut"), filtro.rut().cuerpo()));
                    predicados.add(cb.equal(root.get("dv"), String.valueOf(filtro.rut().dv())));
                }
            }
            return cb.and(predicados.toArray(new Predicate[0]));
        };
    }
}
