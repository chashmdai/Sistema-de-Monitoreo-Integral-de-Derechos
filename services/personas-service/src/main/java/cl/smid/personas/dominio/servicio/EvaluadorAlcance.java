package cl.smid.personas.dominio.servicio;

import cl.smid.personas.dominio.modelo.ContextoTerritorial;

/**
 * Evalúa, de forma pura y aislada, si un registro de persona es visible para un solicitante
 * según su alcance territorial (Núcleo 2.3). Esta es la regla de acceso registro a registro
 * que distingue a Personas del Catálogo (dato de referencia compartido).
 *
 * <p>Reglas:</p>
 * <ul>
 *   <li><b>NACIONAL</b>: ve todos los registros, sin filtro.</li>
 *   <li><b>SEDE</b>: ve los registros cuya {@code id_sede} coincide con la del token.</li>
 *   <li><b>UNIDAD</b>: ve los registros cuya {@code id_unidad} coincide con la del token.</li>
 * </ul>
 *
 * <p>Una persona fuera de alcance se trata como inexistente (HTTP 404), no como prohibida
 * (403), para no revelar registros de otras jurisdicciones.</p>
 */
public final class EvaluadorAlcance {

    private EvaluadorAlcance() {
        // Clase de utilidades: no instanciable.
    }

    /**
     * Determina si la persona, ubicada en {@code idSedePersona}/{@code idUnidadPersona}, cae
     * dentro del alcance del contexto.
     *
     * @param idSedePersona   alt_key de la sede del registro (puede ser nulo)
     * @param idUnidadPersona alt_key de la unidad del registro (puede ser nulo)
     * @param ctx             contexto territorial del solicitante
     * @return {@code true} si el registro es visible para el solicitante
     */
    public static boolean dentroDeAlcance(String idSedePersona, String idUnidadPersona,
                                          ContextoTerritorial ctx) {
        if (ctx == null || ctx.alcance() == null) {
            return false;
        }
        switch (ctx.alcance()) {
            case NACIONAL:
                return true;
            case SEDE:
                return ctx.idSede() != null && ctx.idSede().equals(idSedePersona);
            case UNIDAD:
                return ctx.idUnidad() != null && ctx.idUnidad().equals(idUnidadPersona);
            default:
                return false;
        }
    }
}
