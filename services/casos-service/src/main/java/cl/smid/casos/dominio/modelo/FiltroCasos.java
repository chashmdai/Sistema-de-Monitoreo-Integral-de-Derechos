package cl.smid.casos.dominio.modelo;

import java.util.Objects;

/**
 * Criterios para listar casos dentro del alcance del solicitante. Combina filtros explícitos
 * (estado, unidad) con la restricción territorial derivada del {@link ContextoTerritorial}.
 *
 * <p>El adaptador de persistencia traduce {@link #alcance()} al filtro correspondiente
 * (NACIONAL: sin filtro; SEDE: por sede del contexto; UNIDAD: por unidad del contexto).</p>
 *
 * @param estado        filtro opcional por estado (nulo = todos).
 * @param idUnidadAltFiltro filtro opcional explícito por unidad (nulo = no aplicar).
 * @param alcance       alcance territorial del solicitante.
 * @param ctxSedeAlt    alt_key de la sede del solicitante (para alcance SEDE).
 * @param ctxUnidadAlt  alt_key de la unidad del solicitante (para alcance UNIDAD).
 */
public record FiltroCasos(EstadoCaso estado, String idUnidadAltFiltro, Alcance alcance,
                          String ctxSedeAlt, String ctxUnidadAlt) {

    public FiltroCasos {
        Objects.requireNonNull(alcance, "alcance");
    }

    /** Construye el filtro a partir del contexto del usuario y los parámetros de consulta. */
    public static FiltroCasos desde(ContextoTerritorial ctx, EstadoCaso estado, String unidadFiltro) {
        return new FiltroCasos(estado, unidadFiltro, ctx.alcance(), ctx.idSedeAlt(), ctx.idUnidadAlt());
    }
}
