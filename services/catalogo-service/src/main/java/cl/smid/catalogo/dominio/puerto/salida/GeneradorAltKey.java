package cl.smid.catalogo.dominio.puerto.salida;

/**
 * Puerto de salida para generar identificadores públicos opacos (alt_key).
 *
 * <p>Igual que con {@link RelojDominio}, inyectar la generación de identificadores mantiene
 * el dominio determinista: las pruebas pueden proveer una secuencia controlada de alt_keys
 * en lugar de UUID aleatorios. La implementación de producción genera un UUID v4.</p>
 */
public interface GeneradorAltKey {

    /** Genera un nuevo identificador opaco (en producción, un UUID v4 en su forma canónica). */
    String nuevo();
}
