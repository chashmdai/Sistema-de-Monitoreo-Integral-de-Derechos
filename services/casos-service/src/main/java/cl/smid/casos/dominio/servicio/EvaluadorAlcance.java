package cl.smid.casos.dominio.servicio;

import cl.smid.casos.dominio.modelo.Caso;
import cl.smid.casos.dominio.modelo.ContextoTerritorial;

import java.util.Objects;

/**
 * Evalúa la visibilidad territorial registro a registro (Núcleo 2.3) de un caso para un solicitante.
 *
 * <ul>
 *   <li>NACIONAL: ve todo.</li>
 *   <li>SEDE: ve los casos de su sede ({@code id_sede_alt} coincide).</li>
 *   <li>UNIDAD: ve los casos de su unidad ({@code id_unidad_alt} coincide).</li>
 * </ul>
 *
 * <p>Cuando un recurso individual queda fuera del alcance, el servicio responde 404 (no 403), para no
 * revelar su existencia.</p>
 */
public final class EvaluadorAlcance {

    /** Indica si el solicitante puede ver el caso según su alcance territorial. */
    public boolean puedeVer(Caso caso, ContextoTerritorial ctx) {
        Objects.requireNonNull(caso, "caso");
        Objects.requireNonNull(ctx, "ctx");
        return switch (ctx.alcance()) {
            case NACIONAL -> true;
            case SEDE -> Objects.equals(caso.idSedeAlt(), ctx.idSedeAlt());
            case UNIDAD -> caso.idUnidadAlt() != null
                    && Objects.equals(caso.idUnidadAlt(), ctx.idUnidadAlt());
        };
    }
}
