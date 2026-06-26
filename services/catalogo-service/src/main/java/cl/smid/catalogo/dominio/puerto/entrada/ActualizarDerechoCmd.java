package cl.smid.catalogo.dominio.puerto.entrada;

/**
 * Comando de actualización de un derecho existente.
 *
 * <p>Semántica de cada campo (la operación es un PUT, de reemplazo):</p>
 * <ul>
 *   <li>{@code codigo}: <b>opcional</b>. Si viene y difiere del código actual, se rechaza con
 *       {@code CAT-006} (el código es inmutable). Si viene igual o nulo, se ignora.</li>
 *   <li>{@code nombre}: obligatorio; reemplaza al actual.</li>
 *   <li>{@code descripcion}: reemplaza al actual (un valor nulo la deja vacía).</li>
 *   <li>{@code orden}: si viene, reemplaza al actual; si es nulo, se conserva.</li>
 *   <li>{@code idPadreAltKey}: si viene, <b>reubica</b> el derecho bajo ese nuevo padre
 *       (con verificación de ciclos y recálculo de nivel del subárbol); si es nulo, se
 *       conserva el padre actual. Mover un derecho a la raíz no está soportado en esta versión.</li>
 * </ul>
 */
public record ActualizarDerechoCmd(
        String codigo,
        String nombre,
        String descripcion,
        Short orden,
        String idPadreAltKey
) {}
