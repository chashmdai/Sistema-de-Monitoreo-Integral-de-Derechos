package cl.smid.personas.dominio.puerto.salida;

/**
 * Puerto generador de identificadores públicos opacos (alt_key, UUID). Aislarlo del dominio
 * permite sustituirlo en pruebas por uno determinista y mantiene al núcleo libre de detalles de
 * implementación.
 */
public interface GeneradorAltKey {

    /** Nuevo identificador público (UUID v4 en su representación canónica de 36 caracteres). */
    String nuevo();
}
