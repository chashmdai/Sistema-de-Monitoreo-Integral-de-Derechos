package cl.smid.personas.dominio.puerto.entrada;

import cl.smid.personas.dominio.modelo.ContextoTerritorial;
import cl.smid.personas.dominio.modelo.CriterioDuplicados;
import cl.smid.personas.dominio.modelo.DatosPersona;
import cl.smid.personas.dominio.modelo.Pagina;
import cl.smid.personas.dominio.modelo.Persona;
import cl.smid.personas.dominio.modelo.ResultadoDuplicados;

/**
 * Puerto de entrada del dominio: casos de uso de gestión de personas (Núcleo 5.4/5.5). La capa
 * {@code api} (controlador) depende de esta interfaz, no de la implementación concreta.
 *
 * <p>El {@link ContextoTerritorial} se recibe explícitamente en cada operación: el dominio no
 * accede al contexto de seguridad de Spring, lo recibe ya resuelto desde el controlador.</p>
 */
public interface GestionPersonasUseCase {

    /**
     * Da de alta una persona con datos parciales. Estampa sede/unidad/creador desde el contexto,
     * valida y normaliza el RUT si está presente, verifica unicidad global de RUT, calcula el
     * hash de deduplicación y publica {@code persona.creada}.
     *
     * @throws cl.smid.personas.dominio.excepcion.RutInvalidoException   si el RUT no supera módulo 11
     * @throws cl.smid.personas.dominio.excepcion.RutDuplicadoException  si el RUT ya existe vigente
     */
    Persona crear(DatosPersona datos, ContextoTerritorial ctx);

    /**
     * Actualiza parcialmente una persona existente y visible para el solicitante (partial-merge:
     * sólo los campos no nulos cambian). Revalida RUT/unicidad, recalcula derivados y publica
     * {@code persona.actualizada}.
     *
     * @throws cl.smid.personas.dominio.excepcion.PersonaNoEncontradaException si no existe o está fuera de alcance
     */
    Persona actualizar(String altKey, DatosPersona datos, ContextoTerritorial ctx);

    /**
     * Obtiene una persona por su identificador público, aplicando el filtro territorial. Si está
     * fuera de alcance se comporta como inexistente.
     *
     * @throws cl.smid.personas.dominio.excepcion.PersonaNoEncontradaException si no existe o está fuera de alcance
     */
    Persona obtener(String altKey, ContextoTerritorial ctx);

    /**
     * Busca una persona por RUT dentro del alcance del solicitante. Devuelve una página de a lo
     * sumo un elemento; vacía si no hay coincidencia visible (no revela existencia fuera de alcance).
     */
    Pagina<Persona> buscarPorRut(String rut, ContextoTerritorial ctx);

    /**
     * Busca personas por término de nombre (parcial) dentro del alcance del solicitante, paginado.
     */
    Pagina<Persona> buscarPorNombre(String termino, ContextoTerritorial ctx, int pagina, int tamano);

    /**
     * Ejecuta la prevalidación de duplicados (USR.01). Es <b>cross-territorial</b> por diseño: la
     * deduplicación debe poder ver registros de todas las sedes; sólo devuelve datos mínimos.
     */
    ResultadoDuplicados buscarDuplicados(CriterioDuplicados criterio);
}
