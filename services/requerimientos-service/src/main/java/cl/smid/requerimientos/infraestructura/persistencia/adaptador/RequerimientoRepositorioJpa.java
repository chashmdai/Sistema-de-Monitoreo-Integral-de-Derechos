package cl.smid.requerimientos.infraestructura.persistencia.adaptador;

import cl.smid.requerimientos.dominio.modelo.Alcance;
import cl.smid.requerimientos.dominio.modelo.EstadoRequerimiento;
import cl.smid.requerimientos.dominio.modelo.PaginaDominio;
import cl.smid.requerimientos.dominio.modelo.Requerimiento;
import cl.smid.requerimientos.dominio.puerto.salida.RequerimientoRepositorio;
import cl.smid.requerimientos.infraestructura.persistencia.entidad.RequerimientoEntity;
import cl.smid.requerimientos.infraestructura.persistencia.repositorio.RequerimientoJpaRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Adaptador de salida que implementa {@link RequerimientoRepositorio} sobre Spring Data JPA. El
 * filtro de listado (estado, unidad y alcance territorial registro a registro, Núcleo 2.3) se
 * construye con {@link Specification} tipadas. El mapeo dominio↔entidad es explícito.
 */
@Component
public class RequerimientoRepositorioJpa implements RequerimientoRepositorio {

    /** Tamaño de página por defecto y máximo, para acotar el costo de las consultas. */
    private static final int TAMANO_DEFECTO = 20;
    private static final int TAMANO_MAXIMO = 200;

    private final RequerimientoJpaRepository jpaRepository;
    private final MapeadorRequerimiento mapeador;

    public RequerimientoRepositorioJpa(RequerimientoJpaRepository jpaRepository,
                                       MapeadorRequerimiento mapeador) {
        this.jpaRepository = jpaRepository;
        this.mapeador = mapeador;
    }

    @Override
    public Requerimiento guardar(Requerimiento requerimiento) {
        if (requerimiento.esNuevo()) {
            RequerimientoEntity nueva = Objects.requireNonNull(mapeador.aEntidadNueva(requerimiento),
                    "La entidad nueva no puede ser nula");
            RequerimientoEntity guardada = guardarEntidad(nueva);
            return mapeador.aDominio(guardada);
        }
        // Actualización: cargar la entidad gestionada y refrescar escalares + anexar hijos nuevos.
        Long idInterno = Objects.requireNonNull(requerimiento.idInterno(),
                "El id interno es obligatorio para actualizar un requerimiento");
        RequerimientoEntity gestionada = jpaRepository.findById(idInterno)
                .orElseThrow(() -> new IllegalStateException(
                        "El requerimiento a actualizar no existe: id interno " + idInterno));
        mapeador.actualizarEntidad(gestionada, requerimiento);
        RequerimientoEntity guardada = guardarEntidad(gestionada);
        return mapeador.aDominio(guardada);
    }

    @SuppressWarnings("null")
    private RequerimientoEntity guardarEntidad(RequerimientoEntity entidad) {
        return jpaRepository.save(entidad);
    }

    @Override
    public Optional<Requerimiento> buscarPorAltKey(String altKey) {
        return jpaRepository.findByAltKeyAndVigenteTrue(altKey).map(mapeador::aDominio);
    }

    @Override
    public PaginaDominio<Requerimiento> listar(EstadoRequerimiento estado,
                                               String unidadDestinoAlt,
                                               Alcance alcance,
                                               String sedeUsuarioAlt,
                                               String unidadUsuarioAlt,
                                               int pagina,
                                               int tamano) {
        int paginaSegura = Math.max(pagina, 0);
        int tamanoSeguro = tamano <= 0 ? TAMANO_DEFECTO : Math.min(tamano, TAMANO_MAXIMO);

        Specification<RequerimientoEntity> spec = construirEspecificacion(
                estado, unidadDestinoAlt, alcance, sedeUsuarioAlt, unidadUsuarioAlt);

        PageRequest pageRequest = PageRequest.of(paginaSegura, tamanoSeguro,
                Sort.by(Sort.Direction.DESC, "creadoEn"));
        Page<RequerimientoEntity> page = jpaRepository.findAll(spec, pageRequest);

        List<Requerimiento> contenido = new ArrayList<>(page.getNumberOfElements());
        for (RequerimientoEntity e : page.getContent()) {
            contenido.add(mapeador.aDominio(e));
        }
        return new PaginaDominio<>(contenido, paginaSegura, tamanoSeguro, page.getTotalElements());
    }

    /**
     * Compone la especificación de filtro: vigencia + filtros opcionales (estado/unidad) + recorte
     * territorial según el alcance del usuario.
     */
    private Specification<RequerimientoEntity> construirEspecificacion(EstadoRequerimiento estado,
                                                                       String unidadDestinoAlt,
                                                                       Alcance alcance,
                                                                       String sedeUsuarioAlt,
                                                                       String unidadUsuarioAlt) {
        return (root, query, cb) -> {
            List<Predicate> predicados = new ArrayList<>();
            predicados.add(cb.isTrue(root.get("vigente")));

            if (estado != null) {
                predicados.add(cb.equal(root.get("estado"), estado.name()));
            }
            if (unidadDestinoAlt != null && !unidadDestinoAlt.isBlank()) {
                predicados.add(cb.equal(root.get("idUnidadDestino"), unidadDestinoAlt));
            }

            // Recorte territorial registro a registro (Núcleo 2.3).
            if (alcance == Alcance.SEDE) {
                predicados.add(cb.equal(root.get("idSede"), sedeUsuarioAlt));
            } else if (alcance == Alcance.UNIDAD) {
                predicados.add(cb.equal(root.get("idUnidadDestino"), unidadUsuarioAlt));
            }
            // NACIONAL: sin recorte.

            return cb.and(predicados.toArray(new Predicate[0]));
        };
    }
}
