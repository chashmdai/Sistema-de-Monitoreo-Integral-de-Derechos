package cl.smid.casos.integracion;

import cl.smid.casos.infraestructura.eventos.ConfiguracionRabbit;
import cl.smid.casos.infraestructura.persistencia.CasoJpaRepository;
import cl.smid.casos.soporte.GeneradorTokenPrueba;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.function.BooleanSupplier;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Prueba de integración end-to-end del servicio de Casos con MySQL y RabbitMQ reales (Testcontainers).
 * Cubre: materialización desde el evento {@code requerimiento.asignado}, idempotencia ante reentrega,
 * autenticación (401) y recorte territorial (404), y la facultad por rol (403/200) en una acción
 * administrativa. Requiere Docker; se OMITE automáticamente si no está disponible.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers(disabledWithoutDocker = true)
class CasoIntegracionTest {

    private static final String JWT_SECRET = "clave-de-pruebas-hs256-suficientemente-larga-0123456789";
    private static final String JWT_KID = "smid-test";
    private static final String JWT_ISSUER = "smid-auth";
    private static final String JWT_AUDIENCE = "smid-servicios";
    private static final String SEDE = "sede-RM";
    private static final String UNIDAD = "unidad-1";

    @Container
    static final MySQLContainer<?> MYSQL = contenedorMysql();

    @Container
    static final RabbitMQContainer RABBIT = new RabbitMQContainer("rabbitmq:3.13-management");

    @SuppressWarnings("resource")
    private static MySQLContainer<?> contenedorMysql() {
        return new MySQLContainer<>("mysql:8.0")
                .withDatabaseName("db_casos")
                .withUsername("smid")
                .withPassword("smid");
    }

    @DynamicPropertySource
    static void propiedades(DynamicPropertyRegistry registro) {
        registro.add("spring.datasource.url", MYSQL::getJdbcUrl);
        registro.add("spring.datasource.username", MYSQL::getUsername);
        registro.add("spring.datasource.password", MYSQL::getPassword);

        registro.add("spring.rabbitmq.host", RABBIT::getHost);
        registro.add("spring.rabbitmq.port", RABBIT::getAmqpPort);
        registro.add("spring.rabbitmq.username", RABBIT::getAdminUsername);
        registro.add("spring.rabbitmq.password", RABBIT::getAdminPassword);

        registro.add("smid.eventos.consumo", () -> "rabbitmq");
        registro.add("smid.eventos.transporte", () -> "rabbitmq");
        registro.add("smid.enriquecimiento.activo", () -> "false");
        registro.add("smid.sedes.codigo-defecto", () -> "RM");

        registro.add("smid.seguridad.jwt.issuer", () -> JWT_ISSUER);
        registro.add("smid.seguridad.jwt.audience", () -> JWT_AUDIENCE);
        registro.add("smid.seguridad.jwt.kid-activo", () -> JWT_KID);
        registro.add("smid.seguridad.jwt.secreto-activo", () -> JWT_SECRET);
    }

    @LocalServerPort
    private int puerto;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private CasoJpaRepository casoJpaRepository;

    private final RestTemplate http = new RestTemplate();
    private final GeneradorTokenPrueba tokens =
            new GeneradorTokenPrueba(JWT_ISSUER, JWT_AUDIENCE, JWT_KID, JWT_SECRET);

    // ------------------------------------------------------------------------------------

    @Test
    void materializaUnCasoDesdeEventoYEsIdempotente() {
        String origen = "req-int-idem";
        publicarEvento(origen);

        esperar(() -> casoJpaRepository.findByIdRequerimientoOrigenAlt(origen).isPresent());
        long totalTrasPrimero = casoJpaRepository.count();
        assertThat(casoJpaRepository.findByIdRequerimientoOrigenAlt(origen)).isPresent();

        // Reentrega del MISMO evento: no debe crear un segundo caso.
        publicarEvento(origen);
        // Damos margen al consumidor; el total no debe aumentar.
        dormir(1500);
        assertThat(casoJpaRepository.count()).isEqualTo(totalTrasPrimero);
    }

    @Test
    void rechazaSolicitudesSinTokenOConFirmaInvalida() {
        // Sin token -> 401.
        ResponseEntity<String> sinToken = intercambiar(HttpMethod.GET, "/casos", null, null);
        assertThat(sinToken.getStatusCode().value()).isEqualTo(401);

        // Firma inválida -> 401.
        String tokenMalo = tokens.tokenConFirmaInvalida("user-x", "NACIONAL");
        ResponseEntity<String> firmaMala = intercambiar(HttpMethod.GET, "/casos", tokenMalo, null);
        assertThat(firmaMala.getStatusCode().value()).isEqualTo(401);
    }

    @Test
    void recorteTerritorialRespondeNotFoundFueraDeAlcance() {
        String origen = "req-int-terr";
        publicarEvento(origen);
        esperar(() -> casoJpaRepository.findByIdRequerimientoOrigenAlt(origen).isPresent());
        String altKey = casoJpaRepository.findByIdRequerimientoOrigenAlt(origen).orElseThrow().getAltKey();

        // Usuario de UNIDAD distinta -> 404 (no se revela la existencia).
        String tokenOtraUnidad = tokens.token("user-2", List.of("PROFESIONAL"), SEDE, "unidad-otra",
                "UNIDAD", "Beto");
        ResponseEntity<String> fuera = intercambiar(HttpMethod.GET, "/casos/" + altKey, tokenOtraUnidad, null);
        assertThat(fuera.getStatusCode().value()).isEqualTo(404);

        // Usuario NACIONAL -> 200.
        String tokenNacional = tokens.token("user-1", List.of("PROFESIONAL"), SEDE, UNIDAD,
                "NACIONAL", "Ana");
        ResponseEntity<String> dentro = intercambiar(HttpMethod.GET, "/casos/" + altKey, tokenNacional, null);
        assertThat(dentro.getStatusCode().value()).isEqualTo(200);
        assertThat(dentro.getBody()).contains(altKey);
    }

    @Test
    void accionAdministrativaExigeRolDeCoordinacion() {
        String origen = "req-int-cerrar";
        publicarEvento(origen);
        esperar(() -> casoJpaRepository.findByIdRequerimientoOrigenAlt(origen).isPresent());
        String altKey = casoJpaRepository.findByIdRequerimientoOrigenAlt(origen).orElseThrow().getAltKey();
        String ruta = "/casos/" + altKey + "/transiciones";
        String cuerpo = "{\"accion\":\"CERRAR\",\"observacion\":\"cierre integracion\"}";

        // Profesional sin rol de Coordinación -> 403.
        String tokenProfesional = tokens.token("user-1", List.of("PROFESIONAL"), SEDE, UNIDAD,
                "NACIONAL", "Ana");
        ResponseEntity<String> prohibido = intercambiar(HttpMethod.POST, ruta, tokenProfesional, cuerpo);
        assertThat(prohibido.getStatusCode().value()).isEqualTo(403);

        // Coordinación -> 200 y el caso queda CERRADO.
        String tokenCoordinacion = tokens.token("user-9", List.of("COORDINADOR"), SEDE, UNIDAD,
                "NACIONAL", "Coord");
        ResponseEntity<String> ok = intercambiar(HttpMethod.POST, ruta, tokenCoordinacion, cuerpo);
        assertThat(ok.getStatusCode().value()).isEqualTo(200);
        assertThat(ok.getBody()).contains("CERRADO");
    }

    // ------------------------------- Utilidades -------------------------------

    private void publicarEvento(String origenAlt) {
        String json = """
                {
                  "tipo": "requerimiento.asignado",
                  "altKey": "%s",
                  "ocurridoEn": "%s",
                  "metadatos": {
                    "folio": "F-1",
                    "estado": "ASIGNADO",
                    "idSede": "%s",
                    "idUnidadDestino": "%s",
                    "complejidad": "MEDIANA",
                    "requiereFichaReservada": false,
                    "esBeta": false,
                    "accion": "ASIGNAR",
                    "idProfesionalAsignadoAlt": "prof-1"
                  }
                }
                """.formatted(origenAlt, Instant.parse("2027-02-01T10:00:00Z"), SEDE, UNIDAD);
        rabbitTemplate.convertAndSend(ConfiguracionRabbit.EXCHANGE_EVENTOS,
                ConfiguracionRabbit.RK_REQUERIMIENTO_ASIGNADO, json);
    }

    private ResponseEntity<String> intercambiar(HttpMethod metodo, String ruta, String token, String cuerpo) {
        HttpHeaders cabeceras = new HttpHeaders();
        cabeceras.setContentType(MediaType.APPLICATION_JSON);
        if (token != null) {
            cabeceras.setBearerAuth(token);
        }
        HttpEntity<String> entidad = new HttpEntity<>(cuerpo, cabeceras);
        try {
            return http.exchange(
                    Objects.requireNonNull(url(ruta), "La URL de prueba no debe ser null"),
                    Objects.requireNonNull(metodo, "El metodo HTTP de prueba no debe ser null"),
                    entidad,
                    String.class);
        } catch (org.springframework.web.client.HttpStatusCodeException ex) {
            // RestTemplate lanza ante 4xx/5xx; normalizamos a ResponseEntity para aserciones.
            return ResponseEntity.status(ex.getStatusCode()).body(ex.getResponseBodyAsString());
        }
    }

    private String url(String ruta) {
        return "http://localhost:" + puerto + ruta;
    }

    private void esperar(BooleanSupplier condicion) {
        long limite = System.currentTimeMillis() + Duration.ofSeconds(15).toMillis();
        while (System.currentTimeMillis() < limite) {
            if (condicion.getAsBoolean()) {
                return;
            }
            dormir(200);
        }
        throw new AssertionError("La condición no se cumplió dentro del tiempo de espera.");
    }

    private void dormir(long milis) {
        try {
            Thread.sleep(milis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException(e);
        }
    }
}
