package cl.smid.personas.infraestructura.seguridad;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Propiedades de validación del token (Núcleo 2.4), bajo el prefijo {@code smid.jwt}. Modela el
 * emisor y la audiencia esperados, y el par de claves <b>activa</b> + <b>previa</b> que habilita la
 * rotación sin invalidar tokens en vuelo: el servicio acepta ambas y selecciona por el {@code kid}
 * del encabezado.
 *
 * <p>Los secretos provienen exclusivamente de variables de entorno (DT-2); aquí sólo se mapean.</p>
 *
 * @param issuer   emisor esperado (claim {@code iss}, p. ej. {@code smid-auth})
 * @param audience audiencia esperada que el claim {@code aud} debe contener (p. ej. {@code smid-servicios})
 * @param activa   clave de firma activa (siempre presente)
 * @param previa   clave de firma previa (opcional; habilitada sólo si trae {@code kid})
 */
@ConfigurationProperties(prefix = "smid.jwt")
public record PropiedadesJwt(String issuer, String audience, Clave activa, Clave previa) {

    /**
     * Par identificador-secreto de una clave HMAC.
     *
     * @param kid     identificador de la clave (encabezado {@code kid} del JWS)
     * @param secreto secreto compartido; se usa como clave HMAC desde sus bytes UTF-8 (≥32 bytes)
     */
    public record Clave(String kid, String secreto) {

        /** Indica si la clave está efectivamente configurada (kid y secreto no vacíos). */
        public boolean presente() {
            return kid != null && !kid.isBlank() && secreto != null && !secreto.isBlank();
        }
    }
}
