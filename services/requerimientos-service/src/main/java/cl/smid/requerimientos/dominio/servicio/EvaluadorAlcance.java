package cl.smid.requerimientos.dominio.servicio;

import cl.smid.requerimientos.dominio.modelo.Alcance;

import java.util.Objects;

/**
 * Evaluador de alcance territorial (Núcleo 2.3), idéntico en espíritu al de personas-service.
 * Decide, registro a registro, si un usuario puede ver un requerimiento dado su alcance y su
 * sede/unidad de pertenencia.
 *
 * <p>Reglas:</p>
 * <ul>
 *   <li>{@code NACIONAL}: ve todo (sin filtro).</li>
 *   <li>{@code SEDE}: ve solo los requerimientos cuya {@code id_sede} coincide con la suya.</li>
 *   <li>{@code UNIDAD}: ve solo los requerimientos cuya {@code id_unidad_destino} coincide con
 *       la suya.</li>
 * </ul>
 *
 * <p>Cuando un requerimiento queda fuera de alcance, la capa de aplicación responde 404 (no 403),
 * para no revelar su existencia. Este evaluador es dominio puro: funciones sin efectos.</p>
 */
public final class EvaluadorAlcance {

    private EvaluadorAlcance() {
        // Utilidad estática: no se instancia.
    }

    /**
     * Determina si un usuario puede ver un requerimiento concreto.
     *
     * @param alcanceUsuario       alcance del usuario (del claim {@code alcance})
     * @param sedeUsuarioAlt       alt_key de la sede del usuario (claim {@code idSede})
     * @param unidadUsuarioAlt     alt_key de la unidad del usuario (claim {@code idUnidad})
     * @param sedeRequerimientoAlt alt_key de la sede del requerimiento
     * @param unidadRequerimientoAlt alt_key de la unidad de destino del requerimiento (nulable)
     * @return {@code true} si el requerimiento es visible para el usuario
     */
    public static boolean puedeVer(Alcance alcanceUsuario,
                                   String sedeUsuarioAlt,
                                   String unidadUsuarioAlt,
                                   String sedeRequerimientoAlt,
                                   String unidadRequerimientoAlt) {
        Objects.requireNonNull(alcanceUsuario, "El alcance del usuario es obligatorio");
        return switch (alcanceUsuario) {
            case NACIONAL -> true;
            case SEDE -> Objects.equals(sedeUsuarioAlt, sedeRequerimientoAlt);
            case UNIDAD -> Objects.equals(unidadUsuarioAlt, unidadRequerimientoAlt);
        };
    }
}
