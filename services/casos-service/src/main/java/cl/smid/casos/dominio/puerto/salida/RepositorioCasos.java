package cl.smid.casos.dominio.puerto.salida;

import cl.smid.casos.dominio.modelo.Caso;
import cl.smid.casos.dominio.modelo.FiltroCasos;
import cl.smid.casos.dominio.modelo.Pagina;

import java.util.Optional;

/**
 * Puerto de salida de persistencia del agregado Caso. El adaptador JPA traduce explícitamente entre
 * dominio y entidades, e inserta únicamente los asientos de transición nuevos.
 */
public interface RepositorioCasos {

    /**
     * Persiste el caso (alta o actualización) junto con sus transiciones nuevas. Si al insertar un
     * caso nuevo se viola la unicidad de {@code id_requerimiento_origen_alt}, el adaptador traduce el
     * conflicto a {@code CasoYaMaterializadoException} (sostén de la idempotencia).
     */
    Caso guardar(Caso caso);

    /** Busca un caso por su identificador opaco. No aplica filtro territorial (lo hace el servicio). */
    Optional<Caso> buscarPorAltKey(String altKey);

    /** Busca el (único) caso materializado para un requerimiento de origen. Sostiene el no-op idempotente. */
    Optional<Caso> buscarPorRequerimientoOrigen(String requerimientoOrigenAlt);

    /** Indica si ya existe un caso para el requerimiento de origen indicado (fast-path idempotente). */
    boolean existePorRequerimientoOrigen(String requerimientoOrigenAlt);

    /** Lista paginada de casos dentro del alcance territorial y filtros indicados. */
    Pagina<Caso> listar(FiltroCasos filtro, int pagina, int tamano);
}
