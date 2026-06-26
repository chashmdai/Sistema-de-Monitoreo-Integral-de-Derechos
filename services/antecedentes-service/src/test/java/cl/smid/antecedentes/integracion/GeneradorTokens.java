package cl.smid.antecedentes.integracion;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

/**
 * Generador de JWT HS256 para las pruebas de integracion. Usa setters tipados y llamadas
 * {@code .claim()} por entrada (NO {@code .claims(Map)}, que sobreescribe {@code sub}/{@code exp}/
 * {@code jti} en jjwt 0.12.5). Replica el contrato de claims del cluster.
 */
public final class GeneradorTokens {

    private final SecretKey clave;
    private final String kid;
    private final String issuer;
    private final String audience;

    public GeneradorTokens(String secreto, String kid, String issuer, String audience) {
        this.clave = Keys.hmacShaKeyFor(secreto.getBytes(StandardCharsets.UTF_8));
        this.kid = kid;
        this.issuer = issuer;
        this.audience = audience;
    }

    /**
     * Emite un token valido por una hora con los claims indicados.
     *
     * @param sub      alt_key del usuario
     * @param roles    roles
     * @param idSede   alt_key de la sede
     * @param idUnidad alt_key de la unidad
     * @param alcance  alcance territorial (UNIDAD|SEDE|NACIONAL)
     */
    public String emitir(String sub, List<String> roles, String idSede, String idUnidad, String alcance) {
        Instant ahora = Instant.now();
        return Jwts.builder()
                .header().keyId(kid).and()
                .subject(sub)
                .issuer(issuer)
                .audience().add(audience).and()
                .issuedAt(Date.from(ahora))
                .expiration(Date.from(ahora.plus(1, ChronoUnit.HOURS)))
                .claim("roles", roles)
                .claim("idSede", idSede)
                .claim("idUnidad", idUnidad)
                .claim("alcance", alcance)
                .claim("nombre", "Usuario " + sub)
                .signWith(clave, Jwts.SIG.HS256)
                .compact();
    }
}
