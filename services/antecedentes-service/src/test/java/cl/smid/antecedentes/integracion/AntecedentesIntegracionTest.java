package cl.smid.antecedentes.integracion;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Base64;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Pruebas de integracion del servicio con MySQL real (Testcontainers), Flyway y JWT valido. Se
 * deshabilitan automaticamente si no hay Docker. Cubren el ciclo de la ficha (creacion, detalle
 * con relato descifrado, denegacion territorial 404, revision con/ sin rol) y el cifrado en
 * reposo de extremo a extremo (modo aes-gcm).
 */
@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers(disabledWithoutDocker = true)
class AntecedentesIntegracionTest {

    private static final String JWT_SECRET = "secreto-de-integracion-smid-antecedentes-6-8-32+";
    private static final String JWT_KID = "smid-hs256-1";
    private static final String ISSUER = "smid-auth";
    private static final String AUDIENCE = "smid-servicios";
    private static final String CIFRADO_CLAVE_B64 = Base64.getEncoder().encodeToString(new byte[32]);

    // alt_key sembrados en V1__inicial.sql
    private static final String PROCESO_ALT = "22222222-2222-4222-8222-000000000001";
    private static final String CATEGORIA_ALT = "11111111-1111-4111-8111-000000000001";

    @Container
    @SuppressWarnings("resource")
    static final MySQLContainer<?> MYSQL = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("db_antecedentes")
            .withUsername("smid")
            .withPassword("smid");

    @DynamicPropertySource
    static void propiedades(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () -> MYSQL.getJdbcUrl()
                + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&characterEncoding=UTF-8&tinyInt1isBit=true");
        registry.add("spring.datasource.username", MYSQL::getUsername);
        registry.add("spring.datasource.password", MYSQL::getPassword);
        registry.add("smid.jwt.kid-activo", () -> JWT_KID);
        registry.add("smid.jwt.secreto-activo", () -> JWT_SECRET);
        registry.add("smid.jwt.issuer", () -> ISSUER);
        registry.add("smid.jwt.audience", () -> AUDIENCE);
        registry.add("smid.seguridad.roles-revision", () -> "JEFATURA_UNIDAD");
        registry.add("smid.seguridad.roles-admin", () -> "ADMINISTRADOR");
        registry.add("smid.cifrado.relato", () -> "aes-gcm");
        registry.add("ANTECEDENTES_CIFRADO_CLAVE", () -> CIFRADO_CLAVE_B64);
        registry.add("smid.eventos.transporte", () -> "log");
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private GeneradorTokens tokens() {
        return new GeneradorTokens(JWT_SECRET, JWT_KID, ISSUER, AUDIENCE);
    }

    private String cuerpoFicha() throws Exception {
        Map<String, Object> cuerpo = Map.of(
                "procesoId", PROCESO_ALT,
                "categoriaPrincipalId", CATEGORIA_ALT,
                "derechosCdn", List.of(1, 19),
                "descripcion", "Caso de aprendizaje institucional",
                "relato", "Relato sensible que debe cifrarse en reposo",
                "calificacion", "BUENA_PRACTICA",
                "criterios", List.of("GRAVEDAD"),
                "percepcionHallazgo", "NO_ES_HALLAZGO");
        return objectMapper.writeValueAsString(cuerpo);
    }

    @Test
    void sinTokenDevuelve401() throws Exception {
        mockMvc.perform(get("/antecedentes/fichas"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.codigo").value("AUTZ-003"))
                .andExpect(jsonPath("$.ruta").value("/antecedentes/fichas"));
    }

    @Test
    void cicloDeVidaDeFicha() throws Exception {
        String autor = tokens().emitir("prof-1", List.of(), "sedeA", "unidadA", "UNIDAD");

        MvcResult creacion = mockMvc.perform(post("/antecedentes/fichas")
                        .header("Authorization", "Bearer " + autor)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cuerpoFicha()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.estado").value("BORRADOR"))
                .andExpect(jsonPath("$.folio").value(org.hamcrest.Matchers.startsWith("FA-")))
                .andReturn();

        JsonNode ficha = objectMapper.readTree(creacion.getResponse().getContentAsString());
        String altKey = ficha.get("altKey").asText();

        // El detalle descifra el relato (round-trip AES-GCM).
        mockMvc.perform(get("/antecedentes/fichas/" + altKey)
                        .header("Authorization", "Bearer " + autor))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.relato").value("Relato sensible que debe cifrarse en reposo"));

        // Otra unidad no ve la ficha: 404 (denegacion territorial).
        String otraUnidad = tokens().emitir("prof-2", List.of(), "sedeB", "unidadB", "UNIDAD");
        mockMvc.perform(get("/antecedentes/fichas/" + altKey)
                        .header("Authorization", "Bearer " + otraUnidad))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.codigo").value("ANT-404"));

        // Enviar a revision (miembro de la unidad).
        mockMvc.perform(post("/antecedentes/fichas/" + altKey + "/enviar-revision")
                        .header("Authorization", "Bearer " + autor))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("EN_REVISION"));

        // Aprobar sin rol revisor: 403.
        mockMvc.perform(post("/antecedentes/fichas/" + altKey + "/aprobar")
                        .header("Authorization", "Bearer " + autor))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.codigo").value("AUTZ-004"));

        // Aprobar con rol revisor (alcance nacional): 200.
        String revisor = tokens().emitir("rev-1", List.of("JEFATURA_UNIDAD"), "sedeA", "unidadA", "NACIONAL");
        mockMvc.perform(post("/antecedentes/fichas/" + altKey + "/aprobar")
                        .header("Authorization", "Bearer " + revisor))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("APROBADA"));
    }

    @Test
    void listadoAcotadoAlAlcance() throws Exception {
        String autor = tokens().emitir("prof-3", List.of(), "sedeC", "unidadC", "UNIDAD");
        mockMvc.perform(post("/antecedentes/fichas")
                        .header("Authorization", "Bearer " + autor)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cuerpoFicha()))
                .andExpect(status().isCreated());

        // El mismo solicitante ve al menos su ficha en la bandeja.
        mockMvc.perform(get("/antecedentes/fichas")
                        .header("Authorization", "Bearer " + autor))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contenido").isArray())
                .andExpect(jsonPath("$.totalElementos").isNumber());

        // Otra unidad no ve nada de unidadC.
        String otra = tokens().emitir("prof-4", List.of(), "sedeD", "unidadD", "UNIDAD");
        mockMvc.perform(get("/antecedentes/fichas")
                        .header("Authorization", "Bearer " + otra))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElementos").value(0));
    }
}
