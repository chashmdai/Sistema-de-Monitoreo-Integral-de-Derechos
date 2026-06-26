package cl.smid.casos.dominio.puerto.salida;

/**
 * Puerto de salida para generar identificadores opacos {@code alt_key} (UUID). Se abstrae para que el
 * dominio permanezca puro y para poder inyectar generadores deterministas en pruebas.
 */
public interface GeneradorIdentificadores {

    /** Genera un nuevo {@code alt_key} (UUID v4 en formato canónico de 36 caracteres). */
    String nuevoAltKey();
}
