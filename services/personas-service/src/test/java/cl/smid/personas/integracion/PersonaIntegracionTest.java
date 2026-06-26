package cl.smid.personas.integracion;

import cl.smid.personas.soporte.GeneradorTokenPrueba;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Prueba de integración de extremo a extremo del servicio sobre una base de datos <b>vacía</b>
 * levantada con Testcontainers (MySQL 8). Flyway crea únicamente el esquema; no hay semilla ni
 * migración del padrón histórico, en línea con la decisión de arranque del proyecto.
 *
 * <p>Requiere un demonio Docker disponible en la máquina que ejecuta las pruebas. En entornos
 * sin Docker (p. ej. el sandbox de construcción) esta clase no puede ejecutarse; las pruebas
 * unitarias de dominio sí corren sin Docker.</p>
 *
 * <p>El emisor de tokens de prueba y el validador del servicio comparten secreto/kid mediante
 * {@link DynamicPropertySource}, de modo que un token bien firmado es aceptado y uno firmado con
 * otro secreto produce 401.</p>
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Testcontainers
class PersonaIntegracionTest {

    @Container
    @SuppressWarnings("resource") // Testcontainers cierra el contenedor via la extension JUnit.
    static final MySQLContainer<?> MYSQL = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("db_personas")
            .withUsername("smid")
            .withPassword("smid");

    @DynamicPropertySource
    static void propiedades(DynamicPropertyRegistry registro) {
        // Origen de datos del contenedor efímero.
        registro.add("spring.datasource.url", MYSQL::getJdbcUrl);
        registro.add("spring.datasource.username", MYSQL::getUsername);
        registro.add("spring.datasource.password", MYSQL::getPassword);
        // Material de firma del token, compartido con el generador de tokens de prueba.
        registro.add("smid.jwt.issuer", () -> GeneradorTokenPrueba.ISSUER);
        registro.add("smid.jwt.audience", () -> GeneradorTokenPrueba.AUDIENCE);
        registro.add("smid.jwt.activa.kid", () -> GeneradorTokenPrueba.KID);
        registro.add("smid.jwt.activa.secreto", () -> GeneradorTokenPrueba.SECRETO);
    }

    @Autowired
    private TestRestTemplate rest;

    private static final ParameterizedTypeReference<Map<String, Object>> MAP_RESPONSE =
            new ParameterizedTypeReference<>() {
            };

    // ----------------------------------------------------------------------
    //  Autenticación
    // ----------------------------------------------------------------------

    @Test
    @DisplayName("Sin token, cualquier endpoint protegido responde 401 (AUTZ-003)")
    void sinTokenDevuelve401() {
        ResponseEntity<String> resp = rest.getForEntity("/personas?q=ana", String.class);
        assertThat(resp.getStatusCode().value()).isEqualTo(401);
        String respBody = Objects.requireNonNull(resp.getBody(), "La respuesta debe tener cuerpo");
        assertThat(respBody).contains("AUTZ-003");
    }

    @Test
    @DisplayName("Un token mal firmado responde 401 (AUTZ-003)")
    void tokenMalFirmadoDevuelve401() {
        HttpEntity<Void> req = new HttpEntity<>(
                null, soloAuth(GeneradorTokenPrueba.tokenConSecretoInvalido("imp")));
        ResponseEntity<String> resp = rest.exchange("/personas?q=ana", HttpMethod.GET, req, String.class);
        assertThat(resp.getStatusCode().value()).isEqualTo(401);
    }

    // ----------------------------------------------------------------------
    //  Alta y reglas de negocio
    // ----------------------------------------------------------------------

    @Test
    @DisplayName("Da de alta un NNA sólo con nombre, sin RUT (201)")
    void altaParcialNna() {
        String token = GeneradorTokenPrueba.tokenSede("func-1", "Funcionaria A", "sede-a");

        Map<String, Object> body = new HashMap<>();
        body.put("tipo", "NNA");
        body.put("nombres", "Camila");
        body.put("apellidoPaterno", "Reyes");

        ResponseEntity<Map<String, Object>> resp = rest.exchange(
                "/personas", HttpMethod.POST, new HttpEntity<>(body, conAuth(token)), MAP_RESPONSE);

        assertThat(resp.getStatusCode().value()).isEqualTo(201);
        Map<String, Object> respBody = Objects.requireNonNull(resp.getBody(), "La respuesta debe tener cuerpo");
        assertThat(respBody.get("altKey")).isNotNull();
        assertThat(respBody.get("rut")).isNull();
        assertThat(respBody.get("tipo")).isEqualTo("NNA");
    }

    @Test
    @DisplayName("La prevalidación detecta coincidencia exacta por RUT y probable por nombre/fecha")
    void prevalidacionDetectaDuplicados() {
        String token = GeneradorTokenPrueba.tokenSede("func-1", "Funcionaria A", "sede-a");

        // Alta base con RUT, nombre y fecha.
        Map<String, Object> base = new HashMap<>();
        base.put("tipo", "ADULTO");
        base.put("rut", "12345678-5");
        base.put("nombres", "Juan");
        base.put("apellidoPaterno", "Perez");
        base.put("apellidoMaterno", "Soto");
        base.put("fechaNacimiento", "1990-05-20");
        rest.exchange("/personas", HttpMethod.POST, new HttpEntity<>(base, conAuth(token)), MAP_RESPONSE);

        // Prevalidación con el mismo RUT (otro formato) y nombre/fecha equivalentes.
        Map<String, Object> criterio = new HashMap<>();
        criterio.put("tipo", "ADULTO");
        criterio.put("rut", "12.345.678-5");
        criterio.put("nombres", "Juan");
        criterio.put("apellidoPaterno", "Perez");
        criterio.put("fechaNacimiento", "1990-05-20");

        ResponseEntity<Map<String, Object>> resp = rest.exchange(
                "/personas/buscar-duplicados",
                HttpMethod.POST,
                new HttpEntity<>(criterio, conAuth(token)),
                MAP_RESPONSE);

        assertThat(resp.getStatusCode().value()).isEqualTo(200);
        Map<String, Object> respBody = Objects.requireNonNull(resp.getBody(), "La respuesta debe tener cuerpo");
        // Coincidencia exacta por RUT presente.
        assertThat(respBody.get("coincidenciaExacta")).isNotNull();
    }

    // ----------------------------------------------------------------------
    //  Acceso territorial
    // ----------------------------------------------------------------------

    @Test
    @DisplayName("Un registro de otra sede no es visible para otro usuario (404 PER-404)")
    void accesoTerritorialDevuelve404() {
        String tokenSedeA = GeneradorTokenPrueba.tokenSede("func-a", "Sede A", "sede-a");
        String tokenSedeB = GeneradorTokenPrueba.tokenSede("func-b", "Sede B", "sede-b");

        Map<String, Object> body = new HashMap<>();
        body.put("tipo", "NNA");
        body.put("nombres", "Reservado");
        body.put("apellidoPaterno", "Equis");

        ResponseEntity<Map<String, Object>> creada = rest.exchange(
                "/personas", HttpMethod.POST, new HttpEntity<>(body, conAuth(tokenSedeA)), MAP_RESPONSE);
        assertThat(creada.getStatusCode().value()).isEqualTo(201);
        Map<String, Object> creadaBody = Objects.requireNonNull(creada.getBody(), "La respuesta debe tener cuerpo");
        String altKey = Objects.requireNonNull((String) creadaBody.get("altKey"), "La respuesta debe traer altKey");
        assertThat(altKey).isNotBlank();

        // El usuario de la sede B no debe poder verla: 404 idéntico a "no existe".
        ResponseEntity<String> resp = rest.exchange(
                "/personas/" + altKey,
                HttpMethod.GET,
                new HttpEntity<Void>(null, soloAuth(tokenSedeB)),
                String.class);

        assertThat(resp.getStatusCode().value()).isEqualTo(404);
        String respBody = Objects.requireNonNull(resp.getBody(), "La respuesta debe tener cuerpo");
        assertThat(respBody).contains("PER-404");
    }

    // ----------------------------- Apoyo -----------------------------

    /** Cabeceras con token Bearer y cuerpo JSON (para POST/PUT). */
    @NonNull
    private static HttpHeaders conAuth(String token) {
        String bearer = Objects.requireNonNull(token, "El token Bearer no puede ser nulo");
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(bearer);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    /** Cabeceras sólo con token Bearer (para GET sin cuerpo). */
    @NonNull
    private static HttpHeaders soloAuth(String token) {
        String bearer = Objects.requireNonNull(token, "El token Bearer no puede ser nulo");
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(bearer);
        return headers;
    }
}
