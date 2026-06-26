package cl.smid.antecedentes.infraestructura.seguridad;

import cl.smid.antecedentes.config.PropiedadesJwt;
import cl.smid.antecedentes.dominio.modelo.Alcance;
import cl.smid.antecedentes.dominio.modelo.ContextoSesion;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
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
import java.util.Map;
import java.util.Set;

/**
 * Valida el JWT HS256 emitido por Identidad (6.1) y construye el {@link ContextoSesion}.
 *
 * <ul>
 *   <li>Resolucion de clave por {@code kid} mediante {@link Locator}{@code <Key>} (no
 *       {@code Locator<SecretKey>}: invariancia de genericos en jjwt 0.12.5). Soporta clave
 *       activa y previa (rotacion); si el token no trae {@code kid}, usa la activa.</li>
 *   <li>{@code parseSignedClaims} valida firma y expiracion.</li>
 *   <li>{@code iss} y {@code aud} se validan a mano: el emisor debe coincidir y la audiencia
 *       esperada debe estar contenida en el claim {@code aud}.</li>
 * </ul>
 *
 * Cualquier fallo lanza {@link TokenInvalidoException}.
 */
@Component
public class ValidadorToken {

    private final String issuerEsperado;
    private final String audienciaEsperada;
    private final SecretKey claveActiva;
    private final String kidActivo;
    private final Map<String, SecretKey> clavesPorKid;
    private final io.jsonwebtoken.JwtParser parser;

    public ValidadorToken(PropiedadesJwt propiedades) {
        if (propiedades.secretoActivo() == null || propiedades.secretoActivo().isBlank()) {
            throw new IllegalStateException("smid.jwt.secreto-activo (JWT_SECRET) es obligatorio.");
        }
        this.issuerEsperado = propiedades.issuer();
        this.audienciaEsperada = propiedades.audience();
        this.kidActivo = propiedades.kidActivo();
        this.claveActiva = aClave(propiedades.secretoActivo());

        Map<String, SecretKey> mapa = new java.util.HashMap<>();
        if (propiedades.kidActivo() != null && !propiedades.kidActivo().isBlank()) {
            mapa.put(propiedades.kidActivo(), claveActiva);
        }
        if (propiedades.kidPrevio() != null && !propiedades.kidPrevio().isBlank()
                && propiedades.secretoPrevio() != null && !propiedades.secretoPrevio().isBlank()) {
            mapa.put(propiedades.kidPrevio(), aClave(propiedades.secretoPrevio()));
        }
        this.clavesPorKid = Map.copyOf(mapa);

        Locator<Key> locator = new LocatorAdapter<>() {
            @Override
            protected Key locate(ProtectedHeader header) {
                return resolverClave(header.getKeyId());
            }
        };
        this.parser = io.jsonwebtoken.Jwts.parser().keyLocator(locator).build();
    }

    /**
     * Valida el token y devuelve el contexto de sesion.
     *
     * @param token JWT compacto (sin el prefijo {@code Bearer })
     * @return contexto de sesion derivado de los claims
     * @throws TokenInvalidoException si la firma, expiracion, emisor o audiencia no son validos
     */
    public ContextoSesion validar(String token) {
        try {
            Jws<Claims> jws = parser.parseSignedClaims(token);
            Claims claims = jws.getPayload();

            if (!issuerEsperado.equals(claims.getIssuer())) {
                throw new TokenInvalidoException("Emisor del token no valido.");
            }
            Set<String> audiencias = claims.getAudience();
            if (audiencias == null || !audiencias.contains(audienciaEsperada)) {
                throw new TokenInvalidoException("Audiencia del token no valida.");
            }
            return aContexto(claims);
        } catch (TokenInvalidoException e) {
            throw e;
        } catch (Exception e) {
            throw new TokenInvalidoException("Token invalido: " + e.getMessage());
        }
    }

    private Key resolverClave(String kid) {
        if (kid == null || kid.isBlank()) {
            return claveActiva;
        }
        SecretKey clave = clavesPorKid.get(kid);
        return (clave != null) ? clave : null;
    }

    private ContextoSesion aContexto(Claims claims) {
        String sub = claims.getSubject();
        if (sub == null || sub.isBlank()) {
            throw new TokenInvalidoException("El token no contiene el claim 'sub'.");
        }
        String nombre = claims.get("nombre", String.class);
        String idSede = claims.get("idSede", String.class);
        String idUnidad = claims.get("idUnidad", String.class);
        Alcance alcance = Alcance.desde(claims.get("alcance", String.class));
        Set<String> roles = extraerRoles(claims.get("roles"));
        return new ContextoSesion(sub, nombre, roles, idSede, idUnidad, alcance);
    }

    @SuppressWarnings("unchecked")
    private static Set<String> extraerRoles(Object valor) {
        Set<String> roles = new LinkedHashSet<>();
        if (valor instanceof List<?> lista) {
            for (Object r : lista) {
                if (r != null) {
                    roles.add(r.toString());
                }
            }
        } else if (valor instanceof String s && !s.isBlank()) {
            for (String r : s.split(",")) {
                if (!r.isBlank()) {
                    roles.add(r.trim());
                }
            }
        }
        return roles;
    }

    private static SecretKey aClave(String secreto) {
        return Keys.hmacShaKeyFor(secreto.getBytes(StandardCharsets.UTF_8));
    }

    /** Expone el kid activo (diagnostico). */
    public String kidActivo() {
        return kidActivo;
    }
}
