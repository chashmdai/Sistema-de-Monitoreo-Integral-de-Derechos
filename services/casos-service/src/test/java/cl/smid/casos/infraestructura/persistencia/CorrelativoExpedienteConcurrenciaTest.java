package cl.smid.casos.infraestructura.persistencia;

import cl.smid.casos.dominio.modelo.SerieExpediente;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Verifica que la reserva de correlativos es segura ante concurrencia: con N hilos compitiendo por la
 * misma serie {@code (sede, año, serie)}, se obtienen exactamente los valores 1..N, sin huecos ni
 * repeticiones. Requiere Docker (Testcontainers); se OMITE automáticamente si no está disponible.
 */
@Testcontainers(disabledWithoutDocker = true)
class CorrelativoExpedienteConcurrenciaTest {

    @Container
    static final MySQLContainer<?> MYSQL = contenedorMysql();

    private static final String DDL = """
            CREATE TABLE IF NOT EXISTS correlativo_expediente (
                id_sede_alt VARCHAR(36) NOT NULL,
                anio        INT         NOT NULL,
                serie       VARCHAR(8)  NOT NULL,
                ultimo      BIGINT      NOT NULL DEFAULT 0,
                PRIMARY KEY (id_sede_alt, anio, serie)
            ) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4
            """;

    @SuppressWarnings("resource")
    private static MySQLContainer<?> contenedorMysql() {
        return new MySQLContainer<>("mysql:8.0")
                .withDatabaseName("db_casos")
                .withUsername("smid")
                .withPassword("smid");
    }

    private JdbcTemplate jdbcTemplate() {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        ds.setUrl(MYSQL.getJdbcUrl());
        ds.setUsername(MYSQL.getUsername());
        ds.setPassword(MYSQL.getPassword());
        return new JdbcTemplate(ds);
    }

    @Test
    void reservasConcurrentesSonUnicasYContiguas() throws Exception {
        JdbcTemplate jdbcTemplate = jdbcTemplate();
        jdbcTemplate.execute(DDL);
        jdbcTemplate.update("DELETE FROM correlativo_expediente");

        CorrelativoExpedienteJdbc correlativo = new CorrelativoExpedienteJdbc(jdbcTemplate);

        int hilos = 16;
        int reservasPorHilo = 25;
        int total = hilos * reservasPorHilo;

        ExecutorService pool = Executors.newFixedThreadPool(hilos);
        List<Callable<List<Long>>> tareas = new ArrayList<>();
        for (int i = 0; i < hilos; i++) {
            tareas.add(() -> {
                List<Long> obtenidos = new ArrayList<>();
                for (int j = 0; j < reservasPorHilo; j++) {
                    obtenidos.add(correlativo.reservarSiguiente("sede-RM", 2027, SerieExpediente.OFICIAL));
                }
                return obtenidos;
            });
        }

        List<Long> todos = new ArrayList<>();
        try {
            List<Future<List<Long>>> futuros = pool.invokeAll(tareas);
            for (Future<List<Long>> f : futuros) {
                todos.addAll(f.get());
            }
        } finally {
            pool.shutdownNow();
        }

        // Sin repeticiones y exactamente el conjunto 1..total.
        assertThat(todos).hasSize(total);
        assertThat(todos.stream().distinct().count()).isEqualTo(total);
        List<Long> esperado = new ArrayList<>();
        for (long n = 1; n <= total; n++) {
            esperado.add(n);
        }
        List<Long> ordenado = todos.stream().sorted().collect(Collectors.toList());
        assertThat(ordenado).isEqualTo(esperado);
        Collections.sort(todos);
        assertThat(todos.get(0)).isEqualTo(1L);
        assertThat(todos.get(todos.size() - 1)).isEqualTo((long) total);
    }
}
