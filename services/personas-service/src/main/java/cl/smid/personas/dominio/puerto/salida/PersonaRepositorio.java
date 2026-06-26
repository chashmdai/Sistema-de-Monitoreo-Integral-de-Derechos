package cl.smid.personas.dominio.puerto.salida;

import cl.smid.personas.dominio.modelo.ContextoTerritorial;
import cl.smid.personas.dominio.modelo.Pagina;
import cl.smid.personas.dominio.modelo.Persona;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida hacia el almacenamiento de personas. El dominio expresa <i>qué</i> necesita
 * sin conocer JPA; la infraestructura provee el adaptador.
 *
 * <p>Importante sobre el alcance territorial: las búsquedas por listado aplican el filtro de
 * §2.3 en la consulta (no traen lo que no corresponde), mientras que la obtención por
 * {@code altKey} es <b>incondicional</b> —trae el registro exista donde exista— y deja que el
 * servicio de dominio decida la visibilidad (para poder responder 404 uniforme). Así la regla
 * de alcance vive en el dominio ({@code EvaluadorAlcance}), no dispersa en SQL.</p>
 */
public interface PersonaRepositorio {

    /** Persiste (alta o actualización) una persona y devuelve el estado guardado (con id/alt_key). */
    Persona guardar(Persona persona);

    /**
     * Obtiene una persona por su identificador público, <b>sin</b> filtro territorial. El
     * servicio aplicará el filtro de alcance para decidir si la revela o responde 404.
     */
    Optional<Persona> buscarPorAltKey(String altKey);

    /**
     * Busca una persona vigente por RUT canónico en <b>toda</b> la base (sin filtro
     * territorial). Sustenta la unicidad global de RUT y la coincidencia exacta de la
     * prevalidación de duplicados (que es cross-territorial por diseño).
     */
    Optional<Persona> buscarVigentePorRutGlobal(String rutCanonico);

    /**
     * Busca una persona vigente por RUT canónico <b>dentro del alcance</b> del solicitante.
     * Soporta el {@code GET /personas?rut=} territorial, que no debe revelar registros de otras
     * jurisdicciones.
     */
    Optional<Persona> buscarVigentePorRutEnAlcance(String rutCanonico, ContextoTerritorial ctx);

    /**
     * Lista paginada de personas vigentes cuyo nombre coincide (parcial, insensible a
     * tildes/mayúsculas) con el término, <b>filtrada por alcance territorial</b>.
     */
    Pagina<Persona> buscarPorNombreEnAlcance(String termino, ContextoTerritorial ctx, int pagina, int tamano);

    /**
     * Recupera los candidatos para la deduplicación difusa ("blocking"): personas vigentes que
     * comparten fecha de nacimiento <b>o</b> prefijo de apellido paterno con la consulta. Es
     * cross-territorial por diseño (la deduplicación debe ver todas las sedes) y está acotado en
     * cantidad por el adaptador. Si no hay ni prefijo ni fecha, el adaptador devuelve lista vacía.
     *
     * @param prefijoApellido prefijo normalizado del apellido paterno (o nulo/vacío)
     * @param fechaNacimiento fecha de nacimiento exacta a cotejar (o nula)
     * @return candidatos acotados para el cálculo de similitud
     */
    List<Persona> candidatosParaDedup(String prefijoApellido, LocalDate fechaNacimiento);
}
