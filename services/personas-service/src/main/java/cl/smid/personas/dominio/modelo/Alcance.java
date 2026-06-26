package cl.smid.personas.dominio.modelo;

/**
 * Alcance territorial del usuario, transportado por el claim {@code alcance} del token
 * (Núcleo 2.3). Define el radio de visibilidad sobre los registros de personas.
 *
 * <p>El orden de amplitud es {@code NACIONAL > SEDE > UNIDAD}. El servicio de negocio
 * <b>confía en el claim</b> emitido por Identidad (6.1) y no lo recalcula.</p>
 */
public enum Alcance {

    /** Solo registros de la propia unidad del usuario. */
    UNIDAD(1),

    /** Todos los registros de la sede del usuario. */
    SEDE(2),

    /** Todo el país, sin filtro territorial. */
    NACIONAL(3);

    private final int amplitud;

    Alcance(int amplitud) {
        this.amplitud = amplitud;
    }

    /** Magnitud relativa del alcance (mayor número = mayor radio de visibilidad). */
    public int amplitud() {
        return amplitud;
    }

    /**
     * Resuelve el enum de forma tolerante (mayúsculas/minúsculas, espacios). Devuelve
     * {@code null} si el valor no corresponde a un alcance conocido, para que la capa de
     * seguridad lo trate como token inválido (401) sin lanzar excepción de parsing.
     */
    public static Alcance desde(String valor) {
        if (valor == null) {
            return null;
        }
        try {
            return Alcance.valueOf(valor.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
