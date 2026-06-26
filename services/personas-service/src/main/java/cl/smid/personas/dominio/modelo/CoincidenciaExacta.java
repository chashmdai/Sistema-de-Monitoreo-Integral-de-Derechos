package cl.smid.personas.dominio.modelo;

/**
 * Coincidencia exacta hallada en la prevalidación de duplicados. En esta entrega el único
 * motivo posible es la igualdad de RUT normalizado (Núcleo 5.5).
 *
 * @param altKey identificador público de la persona coincidente
 * @param motivo razón de la coincidencia (p. ej. {@code "RUT"})
 */
public record CoincidenciaExacta(String altKey, String motivo) {
}
