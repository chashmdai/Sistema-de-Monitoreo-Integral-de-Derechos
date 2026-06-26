package cl.smid.antecedentes.dominio.puerto.salida;

/**
 * Puerto de salida para la generacion de identificadores publicos opacos (alt_key UUID).
 */
public interface GeneradorIdentificadores {

    /** Devuelve un nuevo alt_key (UUID en formato canonico de 36 caracteres). */
    String nuevoAltKey();
}
