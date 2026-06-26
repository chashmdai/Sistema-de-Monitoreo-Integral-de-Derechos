package cl.smid.personas.dominio.modelo;

import java.time.LocalDate;

/**
 * Coincidencia probable (difusa) hallada en la prevalidación de duplicados. Se expone sólo el
 * dato mínimo necesario para que el operador humano decida; jamás el registro completo (G7).
 *
 * @param altKey          identificador público de la persona candidata
 * @param nombre          nombre legible compacto (p. ej. {@code "Juan Pérez"})
 * @param fechaNacimiento fecha de nacimiento de la candidata (puede ser nula)
 * @param score           grado de similitud en el rango {@code [0,1]} (mayor = más parecida)
 */
public record CoincidenciaProbable(String altKey, String nombre, LocalDate fechaNacimiento, double score) {
}
