package cl.smid.instituciones.integracion;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.List;

/**
 * Generador de tokens JWT HS256 para las pruebas de integración. Reproduce el contrato de
 * {@code smid-auth} (claims de la sección 2.4 del README de Identidad).
 *
 * <p>Importante (jjwt 0.12.5): se usan los <em>setters</em> tipados
 * ({@code subject/issuer/audience/issuedAt/expiration}) y {@code claim()} por entrada,
 * porque {@code claims(Map)} sobreescribe los claims registrados como {@code sub}/{@code exp}.</p>
 */
public class GeneradorTokens {

    private final SecretKey clave;
    private final String kid;
    private final String issuer;
    private final String audiencia;

    public GeneradorTokens(String secreto, String kid, String issuer, String audiencia) {
        this.clave = Keys.hmacShaKeyFor(secreto.getBytes(StandardCharsets.UTF_8));
        this.kid = kid;
        this.issuer = issuer;
        this.audiencia = audiencia;
    }

    /** Token con los roles indicados, válido por una hora. */
    public String token(String sub, List<String> roles, String alcance) {
        Instant ahora = Instant.now();
        return Jwts.builder()
                .header().keyId(kid).and()
                .subject(sub)
                .issuer(issuer)
                .audience().add(audiencia).and()
                .claim("roles", roles)
                .claim("idSede", "sede-1")
                .claim("idUnidad", "unidad-1")
                .claim("alcance", alcance)
                .claim("nombre", "Usuario de Prueba")
                .issuedAt(Date.from(ahora))
                .expiration(Date.from(ahora.plusSeconds(3600)))
                .signWith(clave, Jwts.SIG.HS256)
                .compact();
    }

    /** Token con un rol administrador (habilita escrituras). */
    public String tokenAdmin() {
        return token("u-admin", List.of("ADMIN_INSTITUCIONES"), "NACIONAL");
    }

    /** Token autenticado pero sin rol administrador (solo lecturas). */
    public String tokenSinRol() {
        return token("u-lector", List.of("PROFESIONAL_UPRJ"), "UNIDAD");
    }
}
