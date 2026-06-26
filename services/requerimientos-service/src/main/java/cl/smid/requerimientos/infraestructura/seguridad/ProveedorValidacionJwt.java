package cl.smid.requerimientos.infraestructura.seguridad;

import cl.smid.requerimientos.dominio.modelo.Alcance;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Valida el JWT entrante con jjwt 0.12.5 (HS256) y construye el {@link UsuarioAutenticado}. Aplica
 * defensa en profundidad (DT-3): verifica firma, emisor ({@code iss}), audiencia ({@code aud}) y
 * expiración ({@code exp}) de forma autónoma, sin depender del Gateway.
 *
 * <p><b>Rotación de claves:</b> intenta primero con la clave activa; si la firma no valida y hay
 * una clave previa configurada (ventana de rotación por {@code kid}), reintenta con ella. Cualquier
 * otra causa (emisor/audiencia/expiración) no se reintenta.</p>
 */
@Component
public class ProveedorValidacionJwt {

    private final PropiedadesJwt propiedades;
    private final SecretKey claveActiva;
    private final SecretKey clavePrevia;

    public ProveedorValidacionJwt(PropiedadesJwt propiedades) {
        this.propiedades = propiedades;
        this.claveActiva = Keys.hmacShaKeyFor(propiedades.secretoActivo().getBytes(StandardCharsets.UTF_8));
        this.clavePrevia = propiedades.tieneClavePrevia()
                ? Keys.hmacShaKeyFor(propiedades.secretoPrevio().getBytes(StandardCharsets.UTF_8))
                : null;
    }

    /**
     * Valida el token y devuelve el principal autenticado.
     *
     * @param token JWT crudo (sin el prefijo {@code Bearer })
     * @return el principal derivado de los claims
     * @throws JwtException si el token es inválido (firma, emisor, audiencia, expiración o claims)
     */
    public UsuarioAutenticado validar(String token) {
        Claims claims = parsear(token);
        return construirPrincipal(claims);
    }

    private Claims parsear(String token) {
        try {
            return parsearConClave(token, claveActiva);
        } catch (SignatureException firmaInvalida) {
            if (clavePrevia != null) {
                // Reintento solo ante fallo de firma, usando la clave previa (rotación).
                return parsearConClave(token, clavePrevia);
            }
            throw firmaInvalida;
        }
    }

    private Claims parsearConClave(String token, SecretKey clave) {
        return Jwts.parser()
                .verifyWith(clave)
                .requireIssuer(propiedades.issuer())
                .requireAudience(propiedades.audience())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private UsuarioAutenticado construirPrincipal(Claims claims) {
        String usuarioAlt = claims.getSubject();
        String idSede = claims.get("idSede", String.class);
        String idUnidad = claims.get("idUnidad", String.class);
        String nombre = claims.get("nombre", String.class);
        Alcance alcance = parsearAlcance(claims.get("alcance", String.class));
        Set<String> roles = parsearRoles(claims.get("roles", Object.class));
        return new UsuarioAutenticado(usuarioAlt, idSede, idUnidad, alcance, roles, nombre);
    }

    private Alcance parsearAlcance(String valor) {
        if (valor == null || valor.isBlank()) {
            throw new JwtException("El token no declara un alcance territorial.");
        }
        try {
            return Alcance.valueOf(valor.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new JwtException("Alcance territorial desconocido en el token: " + valor);
        }
    }

    private Set<String> parsearRoles(Object rolesClaim) {
        Set<String> roles = new LinkedHashSet<>();
        if (rolesClaim instanceof Collection<?> coleccion) {
            for (Object r : coleccion) {
                if (r != null) {
                    roles.add(r.toString());
                }
            }
        }
        return roles;
    }
}
