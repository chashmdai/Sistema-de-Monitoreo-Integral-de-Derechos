package cl.smid.personas.dominio.modelo;

import cl.smid.personas.dominio.servicio.NormalizadorTexto;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;

/**
 * Objeto de valor que materializa la <b>clave de deduplicación difusa</b> de una persona
 * (Núcleo 5.5). Se construye a partir de apellidos, nombres y fecha de nacimiento
 * normalizados (sin acentos ni mayúsculas) y expone:
 *
 * <ul>
 *   <li>{@link #comparable()}: la cadena normalizada usada para el cálculo de similitud.</li>
 *   <li>{@link #hash()}: su digest SHA-256 en hexadecimal, persistido en {@code hash_dedup}
 *       para agrupación/indexación.</li>
 * </ul>
 *
 * <p>El hash se calcula con utilidades estándar del JDK ({@link MessageDigest}); el dominio
 * permanece puro, sin dependencias externas.</p>
 *
 * @param comparable cadena normalizada de comparación (apellidos + nombres + fecha)
 * @param hash       SHA-256 en hexadecimal de {@link #comparable()}
 */
public record ClaveDedup(String comparable, String hash) {

    /**
     * Construye la clave a partir de los componentes de identidad de la persona. Cualquiera
     * puede ser nulo; se normaliza y concatena de forma estable con separador "|".
     *
     * @param apellidoPaterno apellido paterno (o nulo)
     * @param apellidoMaterno apellido materno (o nulo)
     * @param nombres         nombres (o nulo)
     * @param fechaNacimiento fecha de nacimiento (o nula)
     * @return la clave de deduplicación
     */
    public static ClaveDedup desde(String apellidoPaterno, String apellidoMaterno,
                                    String nombres, LocalDate fechaNacimiento) {
        String comparable = String.join("|",
                NormalizadorTexto.normalizar(apellidoPaterno),
                NormalizadorTexto.normalizar(apellidoMaterno),
                NormalizadorTexto.normalizar(nombres),
                (fechaNacimiento == null) ? "" : fechaNacimiento.toString());
        return new ClaveDedup(comparable, sha256Hex(comparable));
    }

    /**
     * Calcula el SHA-256 de una cadena y lo devuelve en hexadecimal en minúsculas (64 chars).
     */
    private static String sha256Hex(String texto) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(texto.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder(digest.length * 2);
            for (byte b : digest) {
                sb.append(Character.forDigit((b >> 4) & 0xF, 16));
                sb.append(Character.forDigit(b & 0xF, 16));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            // SHA-256 es parte obligatoria de toda JVM; este caso no debería ocurrir jamás.
            throw new IllegalStateException("El algoritmo SHA-256 no está disponible en la JVM", e);
        }
    }
}
