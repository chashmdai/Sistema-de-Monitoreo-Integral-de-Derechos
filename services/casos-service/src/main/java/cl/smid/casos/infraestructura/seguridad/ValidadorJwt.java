package cl.smid.casos.infraestructura.seguridad;

import cl.smid.casos.dominio.modelo.Alcance;
import cl.smid.casos.dominio.modelo.ContextoTerritorial;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Locator;
import io.jsonwebtoken.ProtectedHeader;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * Valida el JWT entrante y construye el {@link ContextoTerritorial} del usuario (DT-3: cada servicio
 * revalida el token). Reglas:
 * <ul>
 *   <li>Firma HS256 verificada por {@code kid} (clave activa o previa, ventana de rotación).</li>
 *   <li>{@code iss} debe coincidir con el emisor esperado.</li>
 *   <li>{@code aud} (conjunto) DEBE contener la audiencia esperada ({@code smid-servicios}).</li>
 *   <li>{@code exp} validado por la propia librería al parsear.</li>
 * </ul>
 * Cualquier incumplimiento produce {@link TokenInvalidoException}.
 */
public class ValidadorJwt {

    private final String issuerEsperado;
    private final String audienciaEsperada;
    private final Map<String, SecretKey> clavesPorKid;

    public ValidadorJwt(PropiedadesJwt propiedades) {
        this.issuerEsperado = propiedades.issuer();
        this.audienciaEsperada = propiedades.audience();
        this.clavesPorKid = new HashMap<>();
        registrar(propiedades.kidActivo(), propiedades.secretoActivo());
        registrar(propiedades.kidPrevio(), propiedades.secretoPrevio());
        if (this.clavesPorKid.isEmpty()) {
            throw new IllegalStateException("No hay claves JWT configuradas (kid/secreto activo).");
        }
    }

    private void registrar(String kid, String secreto) {
        if (kid != null && !kid.isBlank() && secreto != null && !secreto.isBlank()) {
            this.clavesPorKid.put(kid, Keys.hmacShaKeyFor(secreto.getBytes(StandardCharsets.UTF_8)));
        }
    }

    /**
     * Valida el token y deriva el contexto del usuario.
     *
     * @param token JWT compacto, sin el prefijo "Bearer ".
     * @return contexto territorial e identitario del usuario.
     * @throws TokenInvalidoException si el token no es válido.
     */
    public ContextoTerritorial validar(String token) {
        try {
            Claims claims = Jwts.parser()
                    .keyLocator(localizadorDeClave())
                    .requireIssuer(issuerEsperado)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            validarAudiencia(claims);
            return aContexto(claims);
        } catch (JwtException | IllegalArgumentException ex) {
            throw new TokenInvalidoException("Token JWT inválido: " + ex.getMessage(), ex);
        }
    }

    /** Localizador que selecciona la clave HMAC según el {@code kid} del encabezado del token. */
    private Locator<Key> localizadorDeClave() {
        return new Locator<>() {
            @Override
            public Key locate(Header header) {
                String kid = (header instanceof ProtectedHeader ph) ? ph.getKeyId() : null;
                if (kid == null) {
                    throw new TokenInvalidoException("El token no declara 'kid'.");
                }
                SecretKey clave = clavesPorKid.get(kid);
                if (clave == null) {
                    throw new TokenInvalidoException("El 'kid' del token no corresponde a una clave vigente.");
                }
                return clave;
            }
        };
    }

    private void validarAudiencia(Claims claims) {
        Set<String> audiencias = claims.getAudience();
        if (audiencias == null || !audiencias.contains(audienciaEsperada)) {
            throw new TokenInvalidoException("La audiencia del token no incluye '" + audienciaEsperada + "'.");
        }
    }

    private ContextoTerritorial aContexto(Claims claims) {
        String sujeto = claims.getSubject();
        String idSede = claims.get("idSede", String.class);
        String idUnidad = claims.get("idUnidad", String.class);
        String nombre = claims.get("nombre", String.class);
        String alcanceTexto = claims.get("alcance", String.class);
        if (alcanceTexto == null) {
            throw new TokenInvalidoException("El token no declara 'alcance'.");
        }
        Alcance alcance;
        try {
            alcance = Alcance.valueOf(alcanceTexto);
        } catch (IllegalArgumentException ex) {
            throw new TokenInvalidoException("El 'alcance' del token no es válido: " + alcanceTexto);
        }
        return new ContextoTerritorial(sujeto, extraerRoles(claims), idSede, idUnidad, alcance, nombre);
    }

    private Set<String> extraerRoles(Claims claims) {
        Object roles = claims.get("roles");
        Set<String> resultado = new LinkedHashSet<>();
        if (roles instanceof Collection<?> coleccion) {
            for (Object rol : coleccion) {
                if (rol != null) {
                    resultado.add(String.valueOf(rol));
                }
            }
        }
        return resultado;
    }
}
