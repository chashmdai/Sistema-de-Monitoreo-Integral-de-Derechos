package cl.smid.casos.dominio.puerto.entrada;

import cl.smid.casos.dominio.modelo.Caso;
import cl.smid.casos.dominio.modelo.CasoEnriquecido;
import cl.smid.casos.dominio.modelo.ContextoTerritorial;
import cl.smid.casos.dominio.modelo.EstadoCaso;
import cl.smid.casos.dominio.modelo.Pagina;

/**
 * Casos de uso de consulta de Casos, siempre acotados por el alcance territorial del solicitante.
 */
public interface ConsultarCasos {

    /**
     * Detalle de un caso por su identificador opaco, con enriquecimiento on-demand (tolerante).
     * Si el caso no existe o queda fuera del alcance, se lanza {@code CasoNoEncontradoException}
     * (404), sin distinguir ambos casos.
     *
     * @param altKey      identificador opaco del caso.
     * @param ctx         contexto territorial del solicitante.
     * @param tokenBearer token del usuario, para propagar al enriquecimiento (puede ser nulo).
     */
    CasoEnriquecido detalle(String altKey, ContextoTerritorial ctx, String tokenBearer);

    /**
     * Listado paginado de casos dentro del alcance, con filtros opcionales por estado y unidad.
     */
    Pagina<Caso> listar(ContextoTerritorial ctx, EstadoCaso estado, String unidadFiltroAlt,
                        int pagina, int tamano);
}
