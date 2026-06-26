package cl.smid.personas.api;

import cl.smid.personas.api.dto.CoincidenciaExactaDTO;
import cl.smid.personas.api.dto.CoincidenciaProbableDTO;
import cl.smid.personas.api.dto.ContactoDTO;
import cl.smid.personas.api.dto.DuplicadosResponse;
import cl.smid.personas.api.dto.PaginaResponse;
import cl.smid.personas.api.dto.PersonaResponse;
import cl.smid.personas.api.dto.PersonaResumenResponse;
import cl.smid.personas.dominio.modelo.Pagina;
import cl.smid.personas.dominio.modelo.Persona;
import cl.smid.personas.dominio.modelo.ResultadoDuplicados;

import java.util.List;

/**
 * Traduce los objetos de dominio a los DTOs de la frontera HTTP. Es el punto donde se aplica la
 * <b>encapsulación jerárquica</b> (G7): la PK interna y cualquier identificador secuencial nunca
 * se proyectan; sólo viaja el {@code altKey} opaco.
 *
 * <p>Clase de utilidades sin estado: todos los métodos son estáticos y puros.</p>
 */
public final class MapeadorRespuesta {

    private MapeadorRespuesta() {
        // Clase de utilidades: no instanciable.
    }

    /** Proyección detallada de una persona (detalle, alta y edición). */
    public static PersonaResponse aDetalle(Persona p) {
        return new PersonaResponse(
                p.altKey(),
                p.tipo() == null ? null : p.tipo().name(),
                p.rut(),
                p.dv(),
                p.nombres(),
                p.apellidoPaterno(),
                p.apellidoMaterno(),
                p.razonSocial(),
                p.nombreLegible(),
                p.fechaNacimiento(),
                p.sexo() == null ? null : p.sexo().name(),
                p.nacionalidad(),
                p.idSede(),
                p.idUnidad(),
                p.vigente(),
                p.creadoEn(),
                p.actualizadoEn(),
                aContactosDTO(p));
    }

    /** Proyección reducida de una persona para listados. */
    public static PersonaResumenResponse aResumen(Persona p) {
        return new PersonaResumenResponse(
                p.altKey(),
                p.tipo() == null ? null : p.tipo().name(),
                p.nombreLegible(),
                p.rut(),
                p.fechaNacimiento());
    }

    /** Proyecta una página de dominio a su sobre de paginación con elementos resumidos. */
    public static PaginaResponse<PersonaResumenResponse> aPaginaResumen(Pagina<Persona> pagina) {
        List<PersonaResumenResponse> contenido = pagina.contenido().stream()
                .map(MapeadorRespuesta::aResumen)
                .toList();
        return new PaginaResponse<>(contenido, pagina.pagina(), pagina.tamano(), pagina.total());
    }

    /** Proyecta el resultado de la prevalidación de duplicados a su DTO. */
    public static DuplicadosResponse aDuplicados(ResultadoDuplicados resultado) {
        CoincidenciaExactaDTO exacta = resultado.coincidenciaExacta()
                .map(c -> new CoincidenciaExactaDTO(c.altKey(), c.motivo()))
                .orElse(null);

        List<CoincidenciaProbableDTO> probables = resultado.coincidenciasProbables().stream()
                .map(c -> new CoincidenciaProbableDTO(c.altKey(), c.nombre(), c.fechaNacimiento(), c.score()))
                .toList();

        return new DuplicadosResponse(exacta, probables);
    }

    /** Convierte los contactos del agregado a su lista de DTOs. */
    private static List<ContactoDTO> aContactosDTO(Persona p) {
        return p.contactos().stream()
                .map(c -> new ContactoDTO(c.tipo().name(), c.valor()))
                .toList();
    }
}
