package cl.smid.instituciones.integracion;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Prueba de integración de extremo a extremo sobre MySQL real (Testcontainers): valida
 * el arranque con Flyway + {@code ddl-auto=validate}, la seguridad por JWT (401/403), el
 * flujo de creación de tipo e institución, la búsqueda por RUT y la invariante de un solo
 * punto focal principal activo.
 *
 * <p>Se omite automáticamente si no hay Docker disponible
 * ({@code @Testcontainers(disabledWithoutDocker = true)}).</p>
 */
@Testcontainers(disabledWithoutDocker = true)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class InstitucionesIntegracionIT {

    private static final String SECRETO = "clave-de-pruebas-hs256-suficientemente-larga-1234567890";
    private static final String KID = "smid-2026-06";

    @Container
    @SuppressWarnings("resource")
    static MySQLContainer<?> MYSQL = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("db_instituciones")
            .withUsername("smid")
            .withPassword("smid")
            .withUrlParam("tinyInt1isBit", "true")
            .withUrlParam("connectionTimeZone", "UTC")
            .withUrlParam("allowPublicKeyRetrieval", "true")
            .withUrlParam("useSSL", "false");

    @DynamicPropertySource
    static void propiedades(DynamicPropertyRegistry registro) {
        registro.add("spring.datasource.url", MYSQL::getJdbcUrl);
        registro.add("spring.datasource.username", MYSQL::getUsername);
        registro.add("spring.datasource.password", MYSQL::getPassword);
        registro.add("smid.jwt.secreto-activo", () -> SECRETO);
        registro.add("smid.jwt.kid-activo", () -> KID);
        registro.add("smid.eventos.transporte", () -> "log");
    }

    @Autowired
    private TestRestTemplate cliente;

    private final GeneradorTokens tokens = new GeneradorTokens(SECRETO, KID, "smid-auth", "smid-servicios");

    private HttpHeaders cabeceras(String token) {
        HttpHeaders h = new HttpHeaders();
        h.setContentType(MediaType.APPLICATION_JSON);
        if (token != null) {
            h.setBearerAuth(token);
        }
        return h;
    }

    @Test
    void exigeAutenticacion() {
        ResponseEntity<String> respuesta = cliente.exchange(
                "/tipos", HttpMethod.GET, new HttpEntity<>(cabeceras(null)), String.class);
        assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(respuesta.getBody()).contains("AUTZ-003");
    }

    @Test
    void rechazaEscrituraSinRolAdmin() {
        String cuerpo = "{\"nombre\":\"No Permitido\",\"ambito\":\"SALUD\"}";
        ResponseEntity<String> respuesta = cliente.exchange(
                "/tipos", HttpMethod.POST, new HttpEntity<>(cuerpo, cabeceras(tokens.tokenSinRol())), String.class);
        assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(respuesta.getBody()).contains("AUTZ-004");
    }

    @Test
    void flujoCompletoCrearTipoInstitucionYBuscarPorRut() throws Exception {
        String admin = tokens.tokenAdmin();

        // 1) Crear tipo
        String tipoJson = "{\"nombre\":\"Hospital\",\"ambito\":\"SALUD\"}";
        ResponseEntity<String> tipoResp = cliente.exchange(
                "/tipos", HttpMethod.POST, new HttpEntity<>(tipoJson, cabeceras(admin)), String.class);
        assertThat(tipoResp.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        String tipoAlt = leer(tipoResp.getBody(), "altKey");
        assertThat(tipoAlt).isNotBlank();

        // 2) Crear institución con RUT
        String instJson = "{\"codigo\":\"H-001\",\"nombre\":\"Hospital Central\",\"tipoAlt\":\"" + tipoAlt
                + "\",\"rut\":\"12.345.678-5\",\"regionCodigo\":\"13\"}";
        ResponseEntity<String> instResp = cliente.exchange(
                "/instituciones", HttpMethod.POST, new HttpEntity<>(instJson, cabeceras(admin)), String.class);
        assertThat(instResp.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        JsonNode inst = JSON.readTree(instResp.getBody());
        assertThat(inst.get("tipoNombre").asText()).isEqualTo("Hospital");
        assertThat(inst.get("rut").asText()).isEqualTo("12345678-5");
        String instAlt = inst.get("altKey").asText();

        // 3) Detalle
        ResponseEntity<String> detalle = cliente.exchange(
                "/instituciones/" + instAlt, HttpMethod.GET, new HttpEntity<>(cabeceras(admin)), String.class);
        assertThat(detalle.getStatusCode()).isEqualTo(HttpStatus.OK);

        // 4) Búsqueda por RUT
        ResponseEntity<String> busqueda = cliente.exchange(
                "/instituciones?rut=12345678-5", HttpMethod.GET, new HttpEntity<>(cabeceras(admin)), String.class);
        assertThat(busqueda.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(JSON.readTree(busqueda.getBody()).get("totalElementos").asInt()).isEqualTo(1);

        // 5) Puntos focales: dos principales -> solo uno queda principal
        cliente.exchange("/instituciones/" + instAlt + "/puntos-focales", HttpMethod.POST,
                new HttpEntity<>("{\"nombre\":\"Ana\",\"principal\":true}", cabeceras(admin)), String.class);
        ResponseEntity<String> segundo = cliente.exchange("/instituciones/" + instAlt + "/puntos-focales",
                HttpMethod.POST, new HttpEntity<>("{\"nombre\":\"Beto\",\"principal\":true}", cabeceras(admin)),
                String.class);
        String betoAlt = JSON.readTree(segundo.getBody()).get("altKey").asText();

        JsonNode detalleFinal = JSON.readTree(cliente.exchange(
                "/instituciones/" + instAlt, HttpMethod.GET, new HttpEntity<>(cabeceras(admin)), String.class)
                .getBody());
        JsonNode puntos = detalleFinal.get("puntosFocales");
        long principales = 0;
        String principalAlt = null;
        for (JsonNode p : puntos) {
            if (p.get("principal").asBoolean()) {
                principales++;
                principalAlt = p.get("altKey").asText();
            }
        }
        assertThat(principales).isEqualTo(1);
        assertThat(principalAlt).isEqualTo(betoAlt);

        // 6) Baja lógica de la institución
        ResponseEntity<String> baja = cliente.exchange("/instituciones/" + instAlt + "/activacion",
                HttpMethod.POST, new HttpEntity<>("{\"activa\":false}", cabeceras(admin)), String.class);
        assertThat(baja.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(JSON.readTree(baja.getBody()).get("activa").asBoolean()).isFalse();
    }

    private static final com.fasterxml.jackson.databind.ObjectMapper JSON =
            new com.fasterxml.jackson.databind.ObjectMapper();

    private static String leer(String cuerpo, String campo) throws Exception {
        return JSON.readTree(cuerpo).get(campo).asText();
    }
}
