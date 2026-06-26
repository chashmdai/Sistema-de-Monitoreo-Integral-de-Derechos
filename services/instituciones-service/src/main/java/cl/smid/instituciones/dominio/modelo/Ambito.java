package cl.smid.instituciones.dominio.modelo;

import cl.smid.instituciones.dominio.excepcion.ExcepcionValidacion;

/**
 * Ámbito sectorial al que pertenece un {@link TipoInstitucion}.
 *
 * <p>Se persiste como {@code VARCHAR(20)} con restricción {@code CHECK}
 * (override #3: nunca {@code ENUM} nativo) y se expone como {@code String}
 * en los DTOs públicos.</p>
 */
public enum Ambito {

    JUDICIAL,
    SALUD,
    EDUCACION,
    PROTECCION,
    POLICIAL,
    MUNICIPAL,
    OTRO;

    /**
     * Convierte un texto a su {@link Ambito}, tolerando espacios y mayúsculas/minúsculas.
     *
     * @param valor texto a interpretar (no nulo ni en blanco)
     * @return el ámbito correspondiente
     * @throws ExcepcionValidacion si el valor es nulo, vacío o no corresponde a un ámbito conocido
     */
    public static Ambito desde(String valor) {
        if (valor == null || valor.isBlank()) {
            throw new ExcepcionValidacion("El ámbito es obligatorio.");
        }
        try {
            return Ambito.valueOf(valor.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new ExcepcionValidacion(
                "Ámbito no reconocido: '" + valor + "'. Valores válidos: "
                + "JUDICIAL, SALUD, EDUCACION, PROTECCION, POLICIAL, MUNICIPAL, OTRO.");
        }
    }
}
