package cl.smid.instituciones.infraestructura.seguridad;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Propiedades de validación de JWT (prefijo {@code smid.jwt}). El secreto se comparte
 * byte a byte con el emisor ({@code smid-auth}) y el Gateway. Se admite una clave
 * <em>previa</em> opcional para soportar rotación sin invalidar tokens vigentes
 * (override #10: par fijo {@code kidActivo/secretoActivo} + opcional
 * {@code kidPrevio/secretoPrevio}, en vez de un mapa YAML dinámico).
 *
 * @param kidActivo     identificador de la clave activa (cabecera {@code kid})
 * @param secretoActivo secreto HS256 de la clave activa (obligatorio)
 * @param kidPrevio     identificador de la clave previa (opcional)
 * @param secretoPrevio secreto HS256 de la clave previa (opcional)
 * @param issuer        emisor esperado (claim {@code iss})
 * @param audiencia     audiencia esperada (debe estar contenida en el claim {@code aud})
 */
@ConfigurationProperties(prefix = "smid.jwt")
public record PropiedadesJwt(
        String kidActivo,
        String secretoActivo,
        String kidPrevio,
        String secretoPrevio,
        String issuer,
        String audiencia) {
}
