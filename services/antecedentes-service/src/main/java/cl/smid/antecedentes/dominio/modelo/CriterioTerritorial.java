package cl.smid.antecedentes.dominio.modelo;

/**
 * Acotacion territorial derivada del contexto de sesion, que el adaptador de persistencia
 * traduce a la clausula de filtro de fichas (Nucleo 2.3):
 *
 * <ul>
 *   <li>{@code NACIONAL}: sin filtro territorial.</li>
 *   <li>{@code SEDE}: {@code WHERE sede_alt = :sedeAlt}.</li>
 *   <li>{@code UNIDAD}: {@code WHERE unidad_alt = :unidadAlt}.</li>
 * </ul>
 *
 * @param alcance   alcance del solicitante
 * @param sedeAlt   alt_key de la sede del solicitante
 * @param unidadAlt alt_key de la unidad del solicitante
 */
public record CriterioTerritorial(Alcance alcance, String sedeAlt, String unidadAlt) {

    public CriterioTerritorial {
        if (alcance == null) {
            alcance = Alcance.UNIDAD;
        }
    }

    /** Construye la acotacion a partir de un contexto de sesion. */
    public static CriterioTerritorial desde(ContextoSesion contexto) {
        return new CriterioTerritorial(contexto.alcance(), contexto.idSede(), contexto.idUnidad());
    }
}
