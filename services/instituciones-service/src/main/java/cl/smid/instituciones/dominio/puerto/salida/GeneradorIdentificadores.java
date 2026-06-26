package cl.smid.instituciones.dominio.puerto.salida;

/**
 * Puerto de salida para generar identificadores públicos opacos ({@code alt_key}).
 * Se inyecta para poder usar un generador determinista en las pruebas.
 */
public interface GeneradorIdentificadores {

    /**
     * @return un nuevo identificador público opaco (UUID en la implementación real).
     */
    String nuevoAltKey();
}
