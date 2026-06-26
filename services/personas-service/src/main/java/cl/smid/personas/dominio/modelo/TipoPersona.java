package cl.smid.personas.dominio.modelo;

/**
 * Tipo de persona registrada en el maestro (Núcleo 5.3).
 * <ul>
 *   <li>{@code NNA}: niño, niña o adolescente (sujeto de derechos).</li>
 *   <li>{@code ADULTO}: persona adulta (típicamente requirente).</li>
 *   <li>{@code JURIDICA}: persona jurídica (usa {@code razonSocial}).</li>
 *   <li>{@code TESTIGO}: persona que aporta testimonio en un caso.</li>
 * </ul>
 */
public enum TipoPersona {
    NNA,
    ADULTO,
    JURIDICA,
    TESTIGO
}
