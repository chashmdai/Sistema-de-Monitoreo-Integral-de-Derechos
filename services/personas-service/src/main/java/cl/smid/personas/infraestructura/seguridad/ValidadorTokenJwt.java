package cl.smid.personas.infraestructura.seguridad;

import cl.smid.personas.dominio.modelo.Alcance;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Locator;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Valida el JWT emitido por Identidad (6.1) como defensa en profundidad (DT-3, Núcleo 2.4). El
 * Gateway ya autentica, pero cada servicio revalida: comprueba la firma HMAC, el emisor, la
 * audiencia y la expiración, y extrae el contexto de seguridad.
 *
 * <p><b>Rotación de claves.</b> Se construye un mapa {@code kid → clave} con la clave activa y, si
 * está configurada, la previa. El encabezado {@code kid} del token selecciona cuál usar mediante un
 * {@link Locator}. Un {@code kid} desconocido aborta la validación (→ 401).</p>
 *
 * <p>El secreto se usa como clave HMAC desde sus bytes UTF-8, idéntico a auth (requiere ≥32 bytes
 * para HS256; de lo contrario el arranque falla de forma explícita).</p>
 */
@Component
public class ValidadorTokenJwt {

    private final String issuerEsperado;
    private final String audienceEsperada;
    private final Map<String, SecretKey> clavesPorKid;
    private final JwtParser parser;

    public ValidadorTokenJwt(PropiedadesJwt propiedades) {
        this.issuerEsperado = propiedades.issuer();
        this.audienceEsperada = propiedades.audience();
        this.clavesPorKid = construirClaves(propiedades);

        // Locator que elige la clave de verificación según el `kid` del encabezado del JWS.
        Locator<java.security.Key> locator = (Header header) -> {
            if (header instanceof JwsHeader jws) {
                String kid = jws.getKeyId();
                SecretKey clave = (kid == null) ? null : clavesPorKid.get(kid);
                if (clave == null) {
                    throw new JwtException("El 'kid' del token no corresponde a ninguna clave conocida");
                }
                return clave;
            }
            throw new JwtException("El encabezado del token no es un JWS firmado");
        };

        this.parser = Jwts.parser()
                .keyLocator(locator)
                .requireIssuer(issuerEsperado)
                // Pequeña tolerancia de reloj entre servicios del clúster.
                .clockSkewSeconds(30)
                .build();
    }

    /**
     * Valida el token y devuelve el principal con el contexto de seguridad.
     *
     * @param token JWS compacto (sin el prefijo {@code Bearer})
     * @return principal autenticado
     * @throws JwtException             si la firma, el emisor o la expiración no son válidos
     * @throws IllegalArgumentException si la audiencia o los claims obligatorios son inválidos
     */
    public UsuarioAutenticado validar(String token) {
        Jws<Claims> jws = parser.parseSignedClaims(token);
        Claims claims = jws.getPayload();

        // La audiencia se valida manualmente: el claim `aud` debe CONTENER la audiencia esperada.
        Set<String> audiencias = claims.getAudience();
        if (audiencias == null || !audiencias.contains(audienceEsperada)) {
            throw new IllegalArgumentException("La audiencia del token no incluye '" + audienceEsperada + "'");
        }

        String sub = claims.getSubject();
        if (sub == null || sub.isBlank()) {
            throw new IllegalArgumentException("El token no trae 'sub'");
        }

        Alcance alcance = Alcance.desde(claims.get("alcance", String.class));
        if (alcance == null) {
            throw new IllegalArgumentException("El token no trae un 'alcance' válido");
        }

        String idSede = claims.get("idSede", String.class);
        String idUnidad = claims.get("idUnidad", String.class);
        String nombre = claims.get("nombre", String.class);
        List<String> roles = extraerRoles(claims);

        return new UsuarioAutenticado(sub, nombre, alcance, idSede, idUnidad, roles);
    }

    /** Extrae el claim {@code roles} como lista de cadenas, tolerando su ausencia. */
    private static List<String> extraerRoles(Claims claims) {
        Object valor = claims.get("roles");
        if (valor instanceof List<?> lista) {
            return lista.stream().map(String::valueOf).toList();
        }
        return List.of();
    }

    /** Construye el mapa {@code kid → clave HMAC} con la clave activa y la previa (si existe). */
    private static Map<String, SecretKey> construirClaves(PropiedadesJwt propiedades) {
        Map<String, SecretKey> mapa = new HashMap<>();
        PropiedadesJwt.Clave activa = propiedades.activa();
        if (activa == null || !activa.presente()) {
            throw new IllegalStateException("Debe configurarse la clave JWT activa (smid.jwt.activa)");
        }
        mapa.put(activa.kid(), Keys.hmacShaKeyFor(activa.secreto().getBytes(StandardCharsets.UTF_8)));

        PropiedadesJwt.Clave previa = propiedades.previa();
        if (previa != null && previa.presente()) {
            mapa.put(previa.kid(), Keys.hmacShaKeyFor(previa.secreto().getBytes(StandardCharsets.UTF_8)));
        }
        return mapa;
    }
}
