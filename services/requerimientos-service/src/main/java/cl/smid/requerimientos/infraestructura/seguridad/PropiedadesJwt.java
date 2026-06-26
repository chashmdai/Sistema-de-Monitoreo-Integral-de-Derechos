package cl.smid.requerimientos.infraestructura.seguridad;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Propiedades de validación del JWT (prefijo {@code smid.jwt}). Defensa en profundidad (DT-3):
 * cada servicio valida el token de forma autónoma. HS256 con identificador de clave ({@code kid})
 * y ventana de rotación (clave activa + clave previa opcional).
 *
 * @param issuer        emisor esperado (claim {@code iss})
 * @param audience      audiencia que el claim {@code aud} debe contener
 * @param kidActivo     identificador de la clave de firma activa
 * @param secretoActivo secreto HS256 de la clave activa (obligatorio)
 * @param kidPrevio     identificador de la clave previa (opcional; ventana de rotación)
 * @param secretoPrevio secreto HS256 de la clave previa (opcional)
 */
@ConfigurationProperties(prefix = "smid.jwt")
public record PropiedadesJwt(
        String issuer,
        String audience,
        String kidActivo,
        String secretoActivo,
        String kidPrevio,
        String secretoPrevio) {

    /** @return {@code true} si hay una clave previa configurada (rotación en curso). */
    public boolean tieneClavePrevia() {
        return kidPrevio != null && !kidPrevio.isBlank()
                && secretoPrevio != null && !secretoPrevio.isBlank();
    }
}
