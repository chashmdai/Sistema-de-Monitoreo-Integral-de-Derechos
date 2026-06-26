package cl.smid.personas.soporte;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

/**
 * Acuña tokens JWT (HS256) equivalentes a los que emite Identidad (6.1), para las pruebas de
 * integración. Expone como constantes el emisor, la audiencia, el {@code kid} y el secreto, de
 * modo que la prueba pueda inyectarlos en la configuración del servicio (vía
 * {@code @DynamicPropertySource}) y así el validador comparta exactamente el mismo material de
 * firma. El secreto tiene ≥32 bytes, requisito de HS256.
 *
 * <p>Los tokens incluyen todos los claims que el servicio revalida: {@code sub}, {@code iss},
 * {@code aud} (como arreglo que contiene la audiencia esperada), {@code jti}, {@code roles},
 * {@code idSede}, {@code idUnidad}, {@code alcance}, {@code nombre}, {@code iat} y {@code exp}.</p>
 */
public final class GeneradorTokenPrueba {

    /** Emisor esperado, idéntico al configurado en el servicio durante las pruebas. */
    public static final String ISSUER = "smid-auth";

    /** Audiencia que el claim {@code aud} debe contener. */
    public static final String AUDIENCE = "smid-servicios";

    /** Identificador de la clave activa de prueba (encabezado {@code kid}). */
    public static final String KID = "test-key-1";

    /** Secreto HMAC de prueba (≥32 bytes para HS256). */
    public static final String SECRETO = "clave-secreta-de-pruebas-smid-personas-hs256-0123456789";

    private GeneradorTokenPrueba() {
        // Clase de utilidades: no instanciable.
    }

    /** Token de alcance NACIONAL (ve todos los registros). */
    public static String tokenNacional(String actor, String nombre) {
        return token(actor, nombre, "NACIONAL", null, null, List.of("FUNCIONARIO"));
    }

    /** Token de alcance SEDE (ve los registros de su sede). */
    public static String tokenSede(String actor, String nombre, String idSede) {
        return token(actor, nombre, "SEDE", idSede, null, List.of("FUNCIONARIO"));
    }

    /** Token de alcance UNIDAD (ve los registros de su unidad). */
    public static String tokenUnidad(String actor, String nombre, String idSede, String idUnidad) {
        return token(actor, nombre, "UNIDAD", idSede, idUnidad, List.of("FUNCIONARIO"));
    }

    /**
     * Construye un token firmado con el secreto y kid <b>oficiales de prueba</b>.
     *
     * @param actor    valor del claim {@code sub}
     * @param nombre   valor del claim {@code nombre}
     * @param alcance  valor del claim {@code alcance} (NACIONAL|SEDE|UNIDAD)
     * @param idSede   valor del claim {@code idSede} (o nulo)
     * @param idUnidad valor del claim {@code idUnidad} (o nulo)
     * @param roles    valor del claim {@code roles}
     */
    public static String token(String actor, String nombre, String alcance,
                               String idSede, String idUnidad, List<String> roles) {
        return construir(SECRETO, KID, ISSUER, AUDIENCE, actor, nombre, alcance, idSede, idUnidad, roles,
                Instant.now().plus(1, ChronoUnit.HOURS));
    }

    /**
     * Construye un token firmado con un secreto <b>distinto</b> del oficial, para simular una
     * firma inválida y verificar la respuesta 401 (AUTZ-003).
     */
    public static String tokenConSecretoInvalido(String actor) {
        String otroSecreto = "secreto-totalmente-distinto-pero-de-32-bytes-minimo-xyz";
        return construir(otroSecreto, KID, ISSUER, AUDIENCE, actor, "Impostor", "NACIONAL", null, null,
                List.of("FUNCIONARIO"), Instant.now().plus(1, ChronoUnit.HOURS));
    }

    // ----------------------------- Construcción -----------------------------

    private static String construir(String secreto, String kid, String issuer, String audience,
                                    String actor, String nombre, String alcance,
                                    String idSede, String idUnidad, List<String> roles, Instant exp) {
        SecretKey clave = Keys.hmacShaKeyFor(secreto.getBytes(StandardCharsets.UTF_8));
        Instant ahora = Instant.now();

        var builder = Jwts.builder()
                .header().keyId(kid).and()
                .subject(actor)
                .issuer(issuer)
                .audience().add(audience).and()
                .id(java.util.UUID.randomUUID().toString())
                .claim("roles", roles)
                .claim("alcance", alcance)
                .claim("nombre", nombre)
                .issuedAt(Date.from(ahora))
                .expiration(Date.from(exp));

        if (idSede != null) {
            builder.claim("idSede", idSede);
        }
        if (idUnidad != null) {
            builder.claim("idUnidad", idUnidad);
        }

        return builder.signWith(clave, Jwts.SIG.HS256).compact();
    }
}
