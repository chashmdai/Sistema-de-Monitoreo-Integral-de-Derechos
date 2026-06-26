package cl.smid.requerimientos.integracion;

import cl.smid.requerimientos.dominio.puerto.salida.CatalogoDerechos;
import cl.smid.requerimientos.dominio.puerto.salida.DirectorioIdentidad;
import cl.smid.requerimientos.dominio.puerto.salida.DirectorioPersonas;
import cl.smid.requerimientos.dominio.puerto.salida.RelojDominio;
import cl.smid.requerimientos.soporte.CatalogoDerechosFalso;
import cl.smid.requerimientos.soporte.DirectorioIdentidadFalso;
import cl.smid.requerimientos.soporte.DirectorioPersonasFalso;
import cl.smid.requerimientos.soporte.JwtDePrueba;
import cl.smid.requerimientos.soporte.RelojFijo;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Prueba de integración extremo a extremo del requerimientos-service contra una base MySQL real
 * (Testcontainers). Verifica la seguridad (401/403), el ingreso, la admisibilidad con rol de
 * Coordinación (camino 403 alcanzable), el folio (oficial vs beta) y el recorte territorial (404).
 *
 * <p><b>Requiere Docker en ejecución.</b> Las dependencias externas (Personas, Catálogo, Identidad)
 * se sustituyen por dobles en memoria (no se usa WireMock); la persistencia y Flyway sí son reales.</p>
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Testcontainers
@Import(RequerimientoIntegracionTest.Stubs.class)
class RequerimientoIntegracionTest {

    // --- Parámetros JWT compartidos entre el emisor de prueba y el validador del servicio ---
    private static final String SECRETO = "clave-de-prueba-hs256-con-mas-de-32-bytes-1234567890";
    private static final String KID = "test-kid";
    private static final String ISSUER = "smid-auth";
    private static final String AUDIENCE = "smid-servicios";

    private static final String SEDE_A = "sede-a";
    private static final String SEDE_B = "sede-b";
    private static final String UNIDAD = "unidad-1";
    private static final Instant MOMENTO_PRUEBA = Instant.parse("2027-04-10T09:00:00Z");

    @Container
    @SuppressWarnings("resource")
    static final MySQLContainer<?> MYSQL = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("db_requerimientos")
            .withUsername("smid")
            .withPassword("smid");

    @DynamicPropertySource
    static void propiedades(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", MYSQL::getJdbcUrl);
        registry.add("spring.datasource.username", MYSQL::getUsername);
        registry.add("spring.datasource.password", MYSQL::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "com.mysql.cj.jdbc.Driver");
        // Secreto/kid del JWT (defensa en profundidad): el emisor de prueba firma con estos valores.
        registry.add("smid.jwt.issuer", () -> ISSUER);
        registry.add("smid.jwt.audience", () -> AUDIENCE);
        registry.add("smid.jwt.kid-activo", () -> KID);
        registry.add("smid.jwt.secreto-activo", () -> SECRETO);
    }

    @Autowired
    private TestRestTemplate rest;

    @Autowired
    private ObjectMapper json;

    // ------------------------------------------------------------------------
    //  Seguridad
    // ------------------------------------------------------------------------

    @Test
    @DisplayName("Sin token, POST /requerimientos responde 401 con código AUTZ-003")
    void sinTokenNoAutenticado() throws Exception {
        ResponseEntity<String> r = rest.postForEntity("/requerimientos", jsonSinToken(Map.of()), String.class);
        assertThat(r.getStatusCode().value()).isEqualTo(401);
        assertThat(codigo(r)).isEqualTo("AUTZ-003");
    }

    @Test
    @DisplayName("Con token mal firmado, POST /requerimientos responde 401")
    void tokenMalFirmadoNoAutenticado() throws Exception {
        String token = JwtDePrueba.tokenMalFirmado(KID, ISSUER, AUDIENCE, "intruso");
        ResponseEntity<String> r = rest.exchange("/requerimientos", HttpMethod.POST,
                conToken(token, Map.of()), String.class);
        assertThat(r.getStatusCode().value()).isEqualTo(401);
        assertThat(codigo(r)).isEqualTo("AUTZ-003");
    }

    // ------------------------------------------------------------------------
    //  Ingreso y folio
    // ------------------------------------------------------------------------

    @Test
    @DisplayName("Crear devuelve 201 con un folio de la serie oficial (RM-n/2027)")
    void crearDevuelve201() throws Exception {
        ResponseEntity<String> r = rest.exchange("/requerimientos", HttpMethod.POST,
                conToken(tokenCoordinador(SEDE_A), Map.of("canal", "WEB", "idUnidadDestinoAlt", UNIDAD)),
                String.class);
        assertThat(r.getStatusCode().value()).isEqualTo(201);
        JsonNode cuerpo = json.readTree(r.getBody());
        assertThat(cuerpo.get("altKey").asText()).isNotBlank();
        assertThat(cuerpo.get("folio").asText()).matches("^RM-\\d+/2027$");
        assertThat(cuerpo.has("idInterno")).isFalse();   // nunca se expone la PK interna
    }

    @Test
    @DisplayName("Con override esBeta, el folio pertenece a la serie BETA (RM-Bn/2027)")
    void crearBetaConOverride() throws Exception {
        ResponseEntity<String> r = rest.exchange("/requerimientos", HttpMethod.POST,
                conToken(tokenCoordinador(SEDE_A),
                        Map.of("canal", "WEB", "idUnidadDestinoAlt", UNIDAD, "esBeta", true)),
                String.class);
        assertThat(r.getStatusCode().value()).isEqualTo(201);
        JsonNode cuerpo = json.readTree(r.getBody());
        assertThat(cuerpo.get("folio").asText()).matches("^RM-B\\d+/2027$");
    }

    @Test
    @DisplayName("Flujo completo de ingreso: crear -> agregar NNA -> enviar deja el estado en INGRESADO")
    void flujoIngreso() throws Exception {
        String token = tokenCoordinador(SEDE_A);
        String altKey = crear(token);

        ResponseEntity<String> nna = rest.exchange("/requerimientos/{k}/nna", HttpMethod.POST,
                conToken(token, Map.of("idPersonaAlt", "nna-1",
                        "derechos", List.of(Map.of("idDerechoAlt", "der-1")))),
                String.class, altKey);
        assertThat(nna.getStatusCode().value()).isEqualTo(200);

        ResponseEntity<String> enviar = rest.exchange("/requerimientos/{k}/enviar", HttpMethod.POST,
                conToken(token, Map.of()), String.class, altKey);
        assertThat(enviar.getStatusCode().value()).isEqualTo(200);
        assertThat(json.readTree(enviar.getBody()).get("estado").asText()).isEqualTo("INGRESADO");
    }

    // ------------------------------------------------------------------------
    //  Admisibilidad (camino 403 alcanzable) y autorización de Coordinación
    // ------------------------------------------------------------------------

    @Test
    @DisplayName("La admisibilidad sin rol de Coordinación responde 403 con código AUTZ-004")
    void admisibilidadSinRolDevuelve403() throws Exception {
        String coordinador = tokenCoordinador(SEDE_A);
        String altKey = ingresar(coordinador);

        // Token de un profesional SIN rol de Coordinación.
        String profesional = JwtDePrueba.token(SECRETO, KID, ISSUER, AUDIENCE, "prof-x",
                List.of("PROFESIONAL_UPRJ"), SEDE_A, UNIDAD, "NACIONAL", "Profesional");

        ResponseEntity<String> r = rest.exchange("/requerimientos/{k}/admisibilidad", HttpMethod.POST,
                conToken(profesional, Map.of("accion", "RESPUESTA_INMEDIATA")), String.class, altKey);

        assertThat(r.getStatusCode().value()).isEqualTo(403);
        assertThat(codigo(r)).isEqualTo("AUTZ-004");
    }

    @Test
    @DisplayName("La admisibilidad con rol de Coordinación (RESPUESTA_INMEDIATA) deja RESPONDIDO")
    void admisibilidadConRolRespondido() throws Exception {
        String coordinador = tokenCoordinador(SEDE_A);
        String altKey = ingresar(coordinador);

        ResponseEntity<String> r = rest.exchange("/requerimientos/{k}/admisibilidad", HttpMethod.POST,
                conToken(coordinador, Map.of("accion", "RESPUESTA_INMEDIATA",
                        "observacion", "Orientación entregada")), String.class, altKey);

        assertThat(r.getStatusCode().value()).isEqualTo(200);
        assertThat(json.readTree(r.getBody()).get("estado").asText()).isEqualTo("RESPONDIDO");
    }

    // ------------------------------------------------------------------------
    //  Recorte territorial
    // ------------------------------------------------------------------------

    @Test
    @DisplayName("Un usuario de otra sede no ve el requerimiento: GET responde 404 (no 403)")
    void accesoTerritorialFueraDeAlcance() throws Exception {
        String altKey = crear(tokenCoordinador(SEDE_A));

        // Usuario con alcance SEDE en una sede distinta.
        String otraSede = JwtDePrueba.token(SECRETO, KID, ISSUER, AUDIENCE, "otro",
                List.of("ADMIN_SEDE"), SEDE_B, "unidad-z", "SEDE", "Otra Sede");

        ResponseEntity<String> r = rest.exchange("/requerimientos/{k}", HttpMethod.GET,
                conToken(otraSede, null), String.class, altKey);

        assertThat(r.getStatusCode().value()).isEqualTo(404);
        assertThat(codigo(r)).isEqualTo("REQ-404");
    }

    // ------------------------------------------------------------------------
    //  Utilidades de prueba
    // ------------------------------------------------------------------------

    private String crear(String token) throws Exception {
        ResponseEntity<String> r = rest.exchange("/requerimientos", HttpMethod.POST,
                conToken(token, Map.of("canal", "WEB", "idUnidadDestinoAlt", UNIDAD)), String.class);
        assertThat(r.getStatusCode().value()).isEqualTo(201);
        return json.readTree(r.getBody()).get("altKey").asText();
    }

    /** Crea y envía un requerimiento (queda en INGRESADO) y devuelve su alt_key. */
    private String ingresar(String token) throws Exception {
        String altKey = crear(token);
        rest.exchange("/requerimientos/{k}/nna", HttpMethod.POST,
                conToken(token, Map.of("idPersonaAlt", "nna-1",
                        "derechos", List.of(Map.of("idDerechoAlt", "der-1")))),
                String.class, altKey);
        rest.exchange("/requerimientos/{k}/enviar", HttpMethod.POST,
                conToken(token, Map.of()), String.class, altKey);
        return altKey;
    }

    private String tokenCoordinador(String idSede) {
        return JwtDePrueba.token(SECRETO, KID, ISSUER, AUDIENCE, "coord-1",
                List.of("COORDINADOR"), idSede, UNIDAD, "NACIONAL", "Coordinador");
    }

    private HttpEntity<Object> conToken(String token, Object cuerpo) {
        HttpHeaders h = new HttpHeaders();
        h.setContentType(MediaType.APPLICATION_JSON);
        h.setBearerAuth(Objects.requireNonNull(token, "token es obligatorio"));
        return new HttpEntity<>(cuerpo, h);
    }

    private HttpEntity<Object> jsonSinToken(Object cuerpo) {
        HttpHeaders h = new HttpHeaders();
        h.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(cuerpo, h);
    }

    private String codigo(ResponseEntity<String> r) throws Exception {
        return json.readTree(r.getBody()).get("codigo").asText();
    }

    /**
     * Sustituye los puertos de salida hacia otros servicios por dobles en memoria precargados, de
     * modo que la prueba no dependa de Personas, Catálogo ni Identidad reales.
     */
    @TestConfiguration
    static class Stubs {

        @Bean
        @Primary
        DirectorioPersonas directorioPersonasFalso() {
            return new DirectorioPersonasFalso().registrar("nna-1", "NNA Uno", "11.111.111-1");
        }

        @Bean
        @Primary
        CatalogoDerechos catalogoDerechosFalso() {
            return new CatalogoDerechosFalso().registrarDerecho("der-1");
        }

        @Bean
        @Primary
        DirectorioIdentidad directorioIdentidadFalso() {
            return new DirectorioIdentidadFalso().registrar("prof-1", UNIDAD);
        }

        @Bean
        @Primary
        RelojDominio relojFijo() {
            return new RelojFijo(MOMENTO_PRUEBA);
        }
    }
}
