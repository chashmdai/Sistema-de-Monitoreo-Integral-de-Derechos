package cl.smid.catalogo.infraestructura.seguridad;

import cl.smid.catalogo.dominio.excepcion.CatalogoException;
import cl.smid.catalogo.dominio.excepcion.CodigoError;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.LocatorAdapter;
import io.jsonwebtoken.ProtectedHeader;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Valida los JWT entrantes según el contrato 2.4 del Núcleo, de forma <b>independiente</b> al
 * Gateway (defensa en profundidad, DT-3).
 *
 * <h2>Qué se verifica</h2>
 * <ol>
 *   <li><b>Firma HS256</b>: la clave se selecciona por el {@code kid} de la cabecera, lo que
 *       habilita la rotación de claves (se admiten la activa y la previa simultáneamente).</li>
 *   <li><b>Emisor</b> ({@code iss}) igual al configurado ({@code smid-auth}).</li>
 *   <li><b>Audiencia</b> ({@code aud}) que contenga el valor configurado ({@code smid-servicios}).</li>
 *   <li><b>Expiración</b> ({@code exp}): la librería la valida automáticamente al parsear.</li>
 * </ol>
 *
 * <p>Cualquier fallo se traduce a {@link CodigoError#NO_AUTENTICADO} (401), sin filtrar el
 * detalle técnico al cliente.</p>
 */
@Component
public class ValidadorTokenJwt {

    private final PropiedadesJwt propiedades;
    private final LocalizadorClavePorKid localizador;

    public ValidadorTokenJwt(PropiedadesJwt propiedades) {
        this.propiedades = propiedades;

        // Construye el mapa kid → SecretKey con la clave activa y, si existe, la previa
        // (falla al arrancar si alguna clave es débil, <256 bits, o si falta la activa).
        Map<String, SecretKey> claves = new LinkedHashMap<>();
        if (propiedades.getKidActivo() == null || propiedades.getKidActivo().isBlank()
                || propiedades.getSecretoActivo() == null || propiedades.getSecretoActivo().isBlank()) {
            throw new IllegalStateException(
                    "Debe configurarse la clave activa del JWT ('smid.jwt.kid-activo' y 'smid.jwt.secreto-activo').");
        }
        claves.put(propiedades.getKidActivo(),
                Keys.hmacShaKeyFor(propiedades.getSecretoActivo().getBytes(StandardCharsets.UTF_8)));
        if (propiedades.tieneClavePrevia()) {
            claves.put(propiedades.getKidPrevio(),
                    Keys.hmacShaKeyFor(propiedades.getSecretoPrevio().getBytes(StandardCharsets.UTF_8)));
        }
        this.localizador = new LocalizadorClavePorKid(claves);
    }

    /**
     * Valida el token compacto y devuelve el contexto de sesión derivado de sus claims.
     *
     * @throws CatalogoException con {@link CodigoError#NO_AUTENTICADO} si el token es inválido,
     *                           su emisor/audiencia no corresponden o está expirado
     */
    public ContextoSesion validar(String tokenCompacto) {
        final Claims claims;
        try {
            claims = Jwts.parser()
                    .keyLocator(localizador)
                    .build()
                    .parseSignedClaims(tokenCompacto)
                    .getPayload();
        } catch (JwtException | IllegalArgumentException e) {
            // Firma inválida, kid desconocido, token malformado o expirado.
            throw new CatalogoException(CodigoError.NO_AUTENTICADO, "Token inválido o expirado.");
        }

        if (propiedades.getIssuer() != null && !propiedades.getIssuer().equals(claims.getIssuer())) {
            throw new CatalogoException(CodigoError.NO_AUTENTICADO, "El emisor del token no es válido.");
        }
        Set<String> audiencias = claims.getAudience();
        if (propiedades.getAudience() != null
                && (audiencias == null || !audiencias.contains(propiedades.getAudience()))) {
            throw new CatalogoException(CodigoError.NO_AUTENTICADO, "El token no está dirigido a este servicio.");
        }

        return aContexto(claims);
    }

    private ContextoSesion aContexto(Claims claims) {
        return new ContextoSesion(
                claims.getSubject(),
                leerRoles(claims),
                claims.get("idSede", String.class),
                claims.get("idUnidad", String.class),
                claims.get("alcance", String.class),
                claims.get("nombre", String.class));
    }

    /** Lee el claim {@code roles} (lista) de forma tolerante al tipo concreto deserializado. */
    private static List<String> leerRoles(Claims claims) {
        Object valor = claims.get("roles");
        if (valor instanceof Collection<?> coleccion) {
            List<String> roles = new ArrayList<>(coleccion.size());
            for (Object elemento : coleccion) {
                if (elemento != null) {
                    roles.add(elemento.toString());
                }
            }
            return roles;
        }
        return List.of();
    }

    /**
     * Localiza la clave de verificación según el {@code kid} de la cabecera firmada.
     * Si falta el {@code kid} o no corresponde a ninguna clave conocida, lanza
     * {@link IllegalArgumentException}, que el validador traduce a 401.
     */
    private static final class LocalizadorClavePorKid extends LocatorAdapter<Key> {

        private final Map<String, SecretKey> clavesPorKid;

        private LocalizadorClavePorKid(Map<String, SecretKey> clavesPorKid) {
            this.clavesPorKid = clavesPorKid;
        }

        @Override
        protected Key locate(ProtectedHeader header) {
            String kid = header.getKeyId();
            if (kid == null || kid.isBlank()) {
                throw new IllegalArgumentException("La cabecera del token no incluye 'kid'.");
            }
            SecretKey clave = clavesPorKid.get(kid);
            if (clave == null) {
                throw new IllegalArgumentException("El 'kid' del token no corresponde a ninguna clave: " + kid);
            }
            return clave;
        }
    }
}
