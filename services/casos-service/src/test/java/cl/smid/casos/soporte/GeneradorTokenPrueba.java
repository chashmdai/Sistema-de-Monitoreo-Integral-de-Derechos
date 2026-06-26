package cl.smid.casos.soporte;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.List;

/**
 * Generador de tokens JWT (HS256 con {@code kid}) para pruebas de integración. Construye tokens
 * válidos o deliberadamente inválidos (firma con secreto distinto) para ejercitar el filtro de
 * seguridad del servicio.
 */
public final class GeneradorTokenPrueba {

    private final String issuer;
    private final String audience;
    private final String kid;
    private final SecretKey clave;

    public GeneradorTokenPrueba(String issuer, String audience, String kid, String secreto) {
        this.issuer = issuer;
        this.audience = audience;
        this.kid = kid;
        this.clave = Keys.hmacShaKeyFor(secreto.getBytes(StandardCharsets.UTF_8));
    }

    /** Token válido con los claims del contexto territorial. */
    public String token(String sujeto, List<String> roles, String idSede, String idUnidad,
                        String alcance, String nombre) {
        Instant ahora = Instant.now();
        return Jwts.builder()
                .header().keyId(kid).and()
                .subject(sujeto)
                .issuer(issuer)
                .audience().add(audience).and()
                .claim("roles", roles)
                .claim("idSede", idSede)
                .claim("idUnidad", idUnidad)
                .claim("alcance", alcance)
                .claim("nombre", nombre)
                .issuedAt(Date.from(ahora))
                .expiration(Date.from(ahora.plusSeconds(3600)))
                .signWith(clave, Jwts.SIG.HS256)
                .compact();
    }

    /** Token con firma inválida (secreto distinto): debe ser rechazado con 401. */
    public String tokenConFirmaInvalida(String sujeto, String alcance) {
        SecretKey otraClave = Keys.hmacShaKeyFor(
                "secreto-totalmente-distinto-para-pruebas-0123456789".getBytes(StandardCharsets.UTF_8));
        Instant ahora = Instant.now();
        return Jwts.builder()
                .header().keyId(kid).and()
                .subject(sujeto)
                .issuer(issuer)
                .audience().add(audience).and()
                .claim("alcance", alcance)
                .issuedAt(Date.from(ahora))
                .expiration(Date.from(ahora.plusSeconds(3600)))
                .signWith(otraClave, Jwts.SIG.HS256)
                .compact();
    }
}
