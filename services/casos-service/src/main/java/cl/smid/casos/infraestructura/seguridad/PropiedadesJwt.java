package cl.smid.casos.infraestructura.seguridad;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Propiedades de validación del JWT (Núcleo 2.4 / DT-3). Cada servicio revalida el token de forma
 * autónoma. La firma es HS256 con identificador de clave ({@code kid}) para permitir rotación: hay
 * una clave ACTIVA y, opcionalmente, una PREVIA, válida durante la ventana de rotación.
 *
 * <p>Se usan campos explícitos {@code kidActivo/secretoActivo} (+ opcionales {@code kidPrevio/
 * secretoPrevio}) en lugar de un mapa dinámico de claves: los marcadores de variables de entorno de
 * Spring no se resuelven sobre claves dinámicas de un mapa YAML (lección aprendida en catálogo).</p>
 *
 * @param issuer        emisor esperado ({@code iss}).
 * @param audience      audiencia esperada que debe estar contenida en {@code aud}.
 * @param kidActivo     identificador de la clave de firma activa.
 * @param secretoActivo secreto HMAC de la clave activa.
 * @param kidPrevio     identificador de la clave previa (opcional, ventana de rotación).
 * @param secretoPrevio secreto HMAC de la clave previa (opcional).
 */
@ConfigurationProperties(prefix = "smid.seguridad.jwt")
public record PropiedadesJwt(String issuer, String audience, String kidActivo, String secretoActivo,
                             String kidPrevio, String secretoPrevio) {
}
