package cl.smid.requerimientos.soporte;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.List;

/**
 * Generador de JWT HS256 para las pruebas de integración. Firma con el mismo secreto/kid que se
 * inyecta al servicio por {@code @DynamicPropertySource}, de modo que el {@code ProveedorValidacionJwt}
 * los acepte. Permite también firmar con un secreto erróneo para ejercitar el camino 401.
 */
public final class JwtDePrueba {

    private JwtDePrueba() {
    }

    /**
     * Construye un token válido con los claims del ecosistema (Núcleo 2.3).
     */
    public static String token(String secreto, String kid, String issuer, String audience,
                               String sub, List<String> roles, String idSede, String idUnidad,
                               String alcance, String nombre) {
        SecretKey clave = Keys.hmacShaKeyFor(secreto.getBytes(StandardCharsets.UTF_8));
        Instant ahora = Instant.now();
        return Jwts.builder()
                .header().keyId(kid).and()
                .subject(sub)
                .issuer(issuer)
                .audience().add(audience).and()
                .claim("roles", roles)
                .claim("idSede", idSede)
                .claim("idUnidad", idUnidad)
                .claim("alcance", alcance)
                .claim("nombre", nombre)
                .issuedAt(Date.from(ahora))
                .expiration(Date.from(ahora.plusSeconds(3600)))
                .signWith(clave)
                .compact();
    }

    /**
     * Construye un token con firma inválida (secreto distinto) para probar el rechazo 401.
     */
    public static String tokenMalFirmado(String kid, String issuer, String audience, String sub) {
        String secretoErroneo = "secreto-erroneo-para-pruebas-de-firma-invalida-0123456789";
        return token(secretoErroneo, kid, issuer, audience, sub, List.of("PROFESIONAL"),
                "sede-a", "unidad-1", "NACIONAL", "Mal Firmado");
    }
}
