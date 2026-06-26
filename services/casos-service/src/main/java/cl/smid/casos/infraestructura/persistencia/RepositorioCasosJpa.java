package cl.smid.casos.infraestructura.persistencia;

import cl.smid.casos.dominio.excepcion.CasoYaMaterializadoException;
import cl.smid.casos.dominio.modelo.Caso;
import cl.smid.casos.dominio.modelo.FiltroCasos;
import cl.smid.casos.dominio.modelo.Pagina;
import cl.smid.casos.dominio.modelo.Transicion;
import cl.smid.casos.dominio.puerto.salida.RepositorioCasos;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Adaptador de persistencia: implementa el puerto {@link RepositorioCasos} sobre Spring Data JPA.
 *
 * <p>Traduce la violación de la clave única {@code uk_caso_requerimiento_origen} en
 * {@link CasoYaMaterializadoException}, que es el mecanismo que sostiene la idempotencia del
 * consumidor de eventos. Inserta SOLO los asientos de transición nuevos del agregado.</p>
 */
@Repository
public class RepositorioCasosJpa implements RepositorioCasos {

    /** Fragmento del nombre de la restricción única de origen, para identificar el conflicto. */
    private static final String RESTRICCION_ORIGEN = "requerimiento_origen";

    private final CasoJpaRepository casoJpa;
    private final TransicionJpaRepository transicionJpa;
    private final MapeadorCasoPersistencia mapeador;

    public RepositorioCasosJpa(CasoJpaRepository casoJpa, TransicionJpaRepository transicionJpa,
                               MapeadorCasoPersistencia mapeador) {
        this.casoJpa = casoJpa;
        this.transicionJpa = transicionJpa;
        this.mapeador = mapeador;
    }

    @Override
    public Caso guardar(Caso caso) {
        // Alta o actualización: localizamos la entidad por alt_key y volcamos el estado de dominio.
        CasoEntity entity = casoJpa.findByAltKey(caso.altKey()).orElseGet(CasoEntity::new);
        mapeador.volcar(caso, entity);

        CasoEntity guardado;
        try {
            guardado = guardarYFlushear(entity);
        } catch (DataIntegrityViolationException ex) {
            if (esConflictoDeOrigen(ex)) {
                // Otro consumidor ya materializó este requerimiento: lo señalamos al dominio.
                throw new CasoYaMaterializadoException(caso.idRequerimientoOrigenAlt());
            }
            throw ex;
        }

        // Persistimos únicamente los asientos creados en esta sesión.
        for (Transicion t : caso.nuevasTransiciones()) {
            transicionJpa.save(Objects.requireNonNull(mapeador.aEntidad(t, guardado.getId()),
                    "El mapeador no debe devolver null para una Transicion"));
        }

        // Reconstituimos el dominio con el historial vigente (persistido + nuevo).
        List<TransicionEntity> asientos = transicionJpa.findByIdCasoOrderByOcurridoEnAsc(guardado.getId());
        return mapeador.aDominio(guardado, asientos);
    }

    @Override
    public Optional<Caso> buscarPorAltKey(String altKey) {
        return casoJpa.findByAltKey(altKey).map(this::cargarConHistorial);
    }

    @Override
    public Optional<Caso> buscarPorRequerimientoOrigen(String requerimientoOrigenAlt) {
        return casoJpa.findByIdRequerimientoOrigenAlt(requerimientoOrigenAlt).map(this::cargarConHistorial);
    }

    @Override
    public boolean existePorRequerimientoOrigen(String requerimientoOrigenAlt) {
        return casoJpa.existsByIdRequerimientoOrigenAlt(requerimientoOrigenAlt);
    }

    @Override
    public Pagina<Caso> listar(FiltroCasos filtro, int pagina, int tamano) {
        PageRequest page = PageRequest.of(pagina, tamano, Sort.by(Sort.Direction.DESC, "abiertoEn"));
        Page<CasoEntity> resultado = casoJpa.buscarTerritorial(
                filtro.estado(), filtro.idUnidadAltFiltro(), filtro.alcance().name(),
                filtro.ctxSedeAlt(), filtro.ctxUnidadAlt(), page);
        // Para el listado (resumen) no se carga el historial: evita N+1 y el resumen no lo expone.
        List<Caso> contenido = resultado.getContent().stream()
                .map(e -> mapeador.aDominio(e, List.of()))
                .toList();
        return new Pagina<>(contenido, resultado.getNumber(), resultado.getSize(), resultado.getTotalElements());
    }

    // -------------------------------- Auxiliares --------------------------------

    private Caso cargarConHistorial(CasoEntity e) {
        List<TransicionEntity> asientos = transicionJpa.findByIdCasoOrderByOcurridoEnAsc(e.getId());
        return mapeador.aDominio(e, asientos);
    }

    @SuppressWarnings("null")
    private CasoEntity guardarYFlushear(CasoEntity entity) {
        return casoJpa.saveAndFlush(entity); // flush para aflorar el conflicto de unicidad aquí
    }

    /** Determina si la violación de integridad corresponde a la unicidad de requerimiento de origen. */
    private boolean esConflictoDeOrigen(DataIntegrityViolationException ex) {
        Throwable causa = ex.getMostSpecificCause();
        String mensaje = causa == null ? ex.getMessage() : causa.getMessage();
        return mensaje != null && mensaje.toLowerCase().contains(RESTRICCION_ORIGEN);
    }
}
