package cl.smid.instituciones.infraestructura.seguridad;

import cl.smid.instituciones.dominio.modelo.ContextoSesion;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Locator;
import io.jsonwebtoken.LocatorAdapter;
import io.jsonwebtoken.ProtectedHeader;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Valida tokens JWT HS256 emitidos por {@code smid-auth} y construye el
 * {@link ContextoSesion} a partir de sus claims.
 *
 * <p>La selección de clave se hace por el {@code kid} de la cabecera mediante un
 * {@link Locator}&lt;{@link Key}&gt; (la invariancia de genéricos obliga a
 * {@code Locator<Key>}, no {@code Locator<SecretKey>}). La firma y la expiración las
 * verifica {@code parseSignedClaims}; el emisor y la audiencia se comprueban a mano
 * (la audiencia debe estar <em>contenida</em> en el claim {@code aud}).</p>
 */
@Component
public class ValidadorJwt {

    private final PropiedadesJwt propiedades;
    private final Locator<Key> localizadorClave;

    public ValidadorJwt(PropiedadesJwt propiedades) {
        this.propiedades = propiedades;
        SecretKey claveActiva = claveDe(propiedades.secretoActivo());
        SecretKey clavePrevia = esVacio(propiedades.secretoPrevio()) ? null : claveDe(propiedades.secretoPrevio());
        String kidActivo = propiedades.kidActivo();
        String kidPrevio = propiedades.kidPrevio();
        this.localizadorClave = new LocatorAdapter<>() {
            @Override
            protected Key locate(ProtectedHeader header) {
                String kid = header.getKeyId();
                if (kid == null || kid.isBlank()) {
                    throw new JwtException("La cabecera del token no incluye 'kid'.");
                }
                if (kid.equals(kidActivo)) {
                    return claveActiva;
                }
                if (clavePrevia != null && kid.equals(kidPrevio)) {
                    return clavePrevia;
                }
                throw new JwtException("El 'kid' del token no corresponde a ninguna clave conocida: " + kid);
            }
        };
    }

    /**
     * Valida el token y construye el contexto de sesión.
     *
     * @param token JWT compacto (sin el prefijo {@code Bearer})
     * @return el contexto de sesión derivado de los claims
     * @throws JwtException si la firma, expiración, emisor o audiencia no son válidos
     */
    public ContextoSesion validar(String token) {
        Jws<Claims> jws = Jwts.parser()
                .keyLocator(localizadorClave)
                .build()
                .parseSignedClaims(token);
        Claims claims = jws.getPayload();

        if (!propiedades.issuer().equals(claims.getIssuer())) {
            throw new JwtException("Emisor del token no válido.");
        }
        Set<String> audiencia = claims.getAudience();
        if (audiencia == null || !audiencia.contains(propiedades.audiencia())) {
            throw new JwtException("Audiencia del token no válida.");
        }

        return new ContextoSesion(
                claims.getSubject(),
                rolesDe(claims),
                claims.get("idSede", String.class),
                claims.get("idUnidad", String.class),
                claims.get("alcance", String.class),
                claims.get("nombre", String.class));
    }

    private static Set<String> rolesDe(Claims claims) {
        Object valor = claims.get("roles");
        Set<String> roles = new LinkedHashSet<>();
        if (valor instanceof List<?> lista) {
            for (Object elemento : lista) {
                if (elemento != null) {
                    roles.add(elemento.toString());
                }
            }
        }
        return roles;
    }

    private static SecretKey claveDe(String secreto) {
        return Keys.hmacShaKeyFor(secreto.getBytes(StandardCharsets.UTF_8));
    }

    private static boolean esVacio(String valor) {
        return valor == null || valor.isBlank();
    }
}
