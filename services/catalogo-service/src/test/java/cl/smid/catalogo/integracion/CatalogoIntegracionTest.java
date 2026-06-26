package cl.smid.catalogo.integracion;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Prueba de integración de extremo a extremo del servicio de Catálogo.
 *
 * <p>Levanta el contexto completo de Spring contra una base <b>MySQL real</b> en un contenedor
 * (Testcontainers), de modo que se ejercitan de verdad: Flyway (esquema + semilla), Hibernate en
 * modo {@code validate}, las consultas recursivas, la cadena de seguridad (validación de JWT,
 * 401/403) y la serialización del árbol.</p>
 *
 * <p><b>Requisito:</b> un entorno con Docker disponible. Si no lo hay, Testcontainers no puede
 * arrancar el contenedor y esta clase se omite/fala por entorno; las pruebas de dominio
 * ({@code ServicioCatalogoTest}, {@code EnsambladorArbolTest}) cubren la lógica sin Docker.</p>
 */
@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class CatalogoIntegracionTest {

    // --- Parámetros de JWT de prueba (deben coincidir con los inyectados al contexto) ---
    private static final String KID = "test-kid";
    private static final String SECRETO = "clave-de-pruebas-de-al-menos-32-caracteres-1234";
    private static final String EMISOR = "smid-auth";
    private static final String AUDIENCIA = "smid-servicios";
    private static final SecretKey CLAVE =
            Keys.hmacShaKeyFor(SECRETO.getBytes(StandardCharsets.UTF_8));

    @Container
    static final MySQLContainer<?> MYSQL = crearContenedorMysql();

    private static MySQLContainer<?> crearContenedorMysql() {
        MySQLContainer<?> mysql = new MySQLContainer<>(DockerImageName.parse("mysql:8.0"));
        mysql.withDatabaseName("db_catalogo");
        mysql.withUsername("catalogo");
        mysql.withPassword("catalogo");
        mysql.withUrlParam("connectionTimeZone", "UTC");
        mysql.withUrlParam("characterEncoding", "UTF-8");
        return mysql;
    }

    /** Inyecta la configuración del contenedor y del JWT al entorno antes de arrancar el contexto. */
    @DynamicPropertySource
    static void propiedades(DynamicPropertyRegistry registro) {
        registro.add("spring.datasource.url", MYSQL::getJdbcUrl);
        registro.add("spring.datasource.username", MYSQL::getUsername);
        registro.add("spring.datasource.password", MYSQL::getPassword);
        registro.add("smid.jwt.kid-activo", () -> KID);
        registro.add("smid.jwt.secreto-activo", () -> SECRETO);
        registro.add("smid.jwt.issuer", () -> EMISOR);
        registro.add("smid.jwt.audience", () -> AUDIENCIA);
    }

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    /** Construye un JWT HS256 válido con los roles indicados. */
    private static String token(List<String> roles) {
        Instant ahora = Instant.now();
        return Jwts.builder()
                .header().keyId(KID).and()
                .subject("usuario-1")
                .issuer(EMISOR)
                .audience().add(AUDIENCIA).and()
                .claim("roles", roles)
                .claim("idSede", "sede-1")
                .claim("idUnidad", "unidad-1")
                .claim("alcance", "NACIONAL")
                .claim("nombre", "Usuario de Prueba")
                .issuedAt(Date.from(ahora))
                .expiration(Date.from(ahora.plusSeconds(3600)))
                .signWith(CLAVE)
                .compact();
    }

    private static String bearerAdmin() {
        return "Bearer " + token(List.of("ADMIN_NACIONAL"));
    }

    private static String bearerUsuario() {
        return "Bearer " + token(List.of("USUARIO"));
    }

    @NonNull
    private static MediaType json() {
        return Objects.requireNonNull(MediaType.APPLICATION_JSON);
    }

    // ============================== Seguridad ==============================

    @Test
    @DisplayName("Lectura sin token responde 401 (AUTZ-003)")
    void lecturaSinTokenEs401() throws Exception {
        mvc.perform(get("/catalogo/derechos"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.codigo").value("AUTZ-003"));
    }

    @Test
    @DisplayName("Lectura con token no administrador funciona (200)")
    void lecturaConUsuarioEs200() throws Exception {
        mvc.perform(get("/catalogo/derechos").header("Authorization", bearerUsuario()))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Escritura sin token responde 401 (AUTZ-003)")
    void escrituraSinTokenEs401() throws Exception {
        mvc.perform(post("/catalogo/derechos")
                        .contentType(json())
                        .content("{\"codigo\":\"X.1\",\"nombre\":\"X\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.codigo").value("AUTZ-003"));
    }

    @Test
    @DisplayName("Escritura con token no administrador responde 403 (AUTZ-004)")
    void escrituraConUsuarioEs403() throws Exception {
        mvc.perform(post("/catalogo/derechos")
                        .header("Authorization", bearerUsuario())
                        .contentType(json())
                        .content("{\"codigo\":\"X.1\",\"nombre\":\"X\"}"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.codigo").value("AUTZ-004"));
    }

    // ============================== Árbol y búsqueda ==============================

    @Test
    @DisplayName("El árbol incluye la semilla, anidada y en orden estable")
    void arbolIncluyeSemilla() throws Exception {
        mvc.perform(get("/catalogo/derechos").header("Authorization", bearerUsuario()))
                .andExpect(status().isOk())
                // La semilla trae 11 raíces; otros tests pueden agregar raíces en la misma BD temporal.
                .andExpect(result -> {
                    JsonNode derechos = objectMapper.readTree(result.getResponse().getContentAsString());
                    assertThat(derechos.size()).isGreaterThanOrEqualTo(11);

                    JsonNode educacion = StreamSupport.stream(derechos.spliterator(), false)
                            .filter(nodo -> "EDU".equals(nodo.path("codigo").asText()))
                            .findFirst()
                            .orElseThrow();
                    List<String> hijos = StreamSupport.stream(educacion.path("hijos").spliterator(), false)
                            .map(nodo -> nodo.path("codigo").asText())
                            .toList();

                    assertThat(hijos).containsExactly("EDU.ACCESO", "EDU.CALIDAD");
                });
    }

    @Test
    @DisplayName("La búsqueda es acento- e insensible a mayúsculas")
    void busquedaAcentoInsensible() throws Exception {
        mvc.perform(get("/catalogo/derechos/buscar")
                        .param("q", "educacion")
                        .header("Authorization", bearerUsuario()))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    List<String> codigos = objectMapper.readTree(result.getResponse().getContentAsString())
                            .findValuesAsText("codigo");
                    assertThat(codigos).contains("EDU");
                });
    }

    // ============================== Ciclo de vida ==============================

    @Test
    @DisplayName("Crear (201) y luego consultar el derecho recién creado")
    void crearYConsultar() throws Exception {
        String cuerpo = mvc.perform(post("/catalogo/derechos")
                        .header("Authorization", bearerAdmin())
                        .contentType(json())
                        .content("{\"codigo\":\"TEST.NUEVO\",\"nombre\":\"Derecho de prueba\"}"))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.altKey").exists())
                .andExpect(jsonPath("$.codigo").value("TEST.NUEVO"))
                .andReturn().getResponse().getContentAsString();

        String altKey = objectMapper.readTree(cuerpo).get("altKey").asText();

        mvc.perform(get("/catalogo/derechos/{altKey}", altKey)
                        .header("Authorization", bearerUsuario()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.codigo").value("TEST.NUEVO"))
                .andExpect(jsonPath("$.vigente").value(true));
    }

    @Test
    @DisplayName("La baja responde 204 y el derecho queda no vigente pero consultable")
    void bajaResponde204() throws Exception {
        String cuerpo = mvc.perform(post("/catalogo/derechos")
                        .header("Authorization", bearerAdmin())
                        .contentType(json())
                        .content("{\"codigo\":\"TEST.BAJA\",\"nombre\":\"Para dar de baja\"}"))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        String altKey = objectMapper.readTree(cuerpo).get("altKey").asText();

        mvc.perform(delete("/catalogo/derechos/{altKey}", altKey)
                        .header("Authorization", bearerAdmin()))
                .andExpect(status().isNoContent());

        mvc.perform(get("/catalogo/derechos/{altKey}", altKey)
                        .header("Authorization", bearerUsuario()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vigente").value(false));
    }
}
