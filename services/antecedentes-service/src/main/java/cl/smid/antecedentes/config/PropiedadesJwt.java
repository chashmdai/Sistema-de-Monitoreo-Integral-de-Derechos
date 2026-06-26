package cl.smid.antecedentes.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuracion fija del JWT (HS256). No es un mapa YAML dinamico: Spring no resuelve variables
 * de entorno como claves de mapa, de modo que se usan pares nombrados
 * {@code kidActivo/secretoActivo} y opcionalmente {@code kidPrevio/secretoPrevio} (rotacion).
 * El {@code secretoActivo} se comparte byte a byte con el Gateway (variable {@code JWT_SECRET}).
 *
 * @param kidActivo     identificador de la clave activa (header {@code kid})
 * @param secretoActivo secreto HMAC activo (Base64 o texto; minimo 32 bytes para HS256)
 * @param kidPrevio     identificador de la clave previa (opcional, rotacion)
 * @param secretoPrevio secreto HMAC previo (opcional)
 * @param issuer        emisor esperado (claim {@code iss}); por defecto {@code smid-auth}
 * @param audience      audiencia esperada (debe estar contenida en el claim {@code aud})
 */
@ConfigurationProperties(prefix = "smid.jwt")
public record PropiedadesJwt(
        String kidActivo,
        String secretoActivo,
        String kidPrevio,
        String secretoPrevio,
        String issuer,
        String audience) {

    public PropiedadesJwt {
        if (issuer == null || issuer.isBlank()) {
            issuer = "smid-auth";
        }
        if (audience == null || audience.isBlank()) {
            audience = "smid-servicios";
        }
    }
}
