package cl.smid.requerimientos.dominio.servicio;

import cl.smid.requerimientos.dominio.modelo.Folio;
import cl.smid.requerimientos.soporte.CorrelativoEnMemoria;
import cl.smid.requerimientos.soporte.DirectorioSedesFalso;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("GeneradorFolio — serie, correlativo y seguridad bajo concurrencia")
class GeneradorFolioTest {

    private static final LocalDate INICIO_OFICIAL = LocalDate.parse("2027-01-01");
    private static final Instant EN_2027 = Instant.parse("2027-06-15T10:00:00Z");
    private static final Instant EN_2026 = Instant.parse("2026-06-15T10:00:00Z");
    private static final String SEDE = "sede-rm";

    private GeneradorFolio nuevoGenerador() {
        DirectorioSedesFalso sedes = new DirectorioSedesFalso().registrar(SEDE, "RM");
        return new GeneradorFolio(sedes, new CorrelativoEnMemoria(), INICIO_OFICIAL);
    }

    @Test
    @DisplayName("La primera serie oficial de 2027 arranca en 1: RM-1/2027, luego RM-2/2027")
    void serieOficialArrancaEnUno() {
        GeneradorFolio gen = nuevoGenerador();
        assertThat(gen.generar(SEDE, EN_2027, null).valor()).isEqualTo("RM-1/2027");
        assertThat(gen.generar(SEDE, EN_2027, null).valor()).isEqualTo("RM-2/2027");
    }

    @Test
    @DisplayName("Antes del corte (marcha blanca) la política asigna serie BETA: RM-B1/2026")
    void antesDelCorteEsBeta() {
        GeneradorFolio gen = nuevoGenerador();
        assertThat(gen.generar(SEDE, EN_2026, null).valor()).isEqualTo("RM-B1/2026");
    }

    @Test
    @DisplayName("La serie BETA está aislada y no consume la numeración oficial del mismo año")
    void betaNoConsumeOficial() {
        GeneradorFolio gen = nuevoGenerador();
        // Dos folios beta forzados por override en 2027.
        assertThat(gen.generar(SEDE, EN_2027, Boolean.TRUE).valor()).isEqualTo("RM-B1/2027");
        assertThat(gen.generar(SEDE, EN_2027, Boolean.TRUE).valor()).isEqualTo("RM-B2/2027");
        // La serie oficial del mismo año sigue empezando en 1.
        assertThat(gen.generar(SEDE, EN_2027, Boolean.FALSE).valor()).isEqualTo("RM-1/2027");
    }

    @Test
    @DisplayName("Bajo alta concurrencia, los folios reservados son únicos (sin colisiones)")
    void unicidadBajoConcurrencia() throws InterruptedException {
        GeneradorFolio gen = nuevoGenerador();
        int hilos = 32;
        int porHilo = 25;
        int total = hilos * porHilo;

        ExecutorService pool = Executors.newFixedThreadPool(hilos);
        CountDownLatch listos = new CountDownLatch(hilos);
        CountDownLatch partida = new CountDownLatch(1);
        Set<String> folios = Collections.synchronizedSet(new HashSet<>());

        for (int h = 0; h < hilos; h++) {
            pool.submit(() -> {
                listos.countDown();
                try {
                    partida.await();
                    for (int i = 0; i < porHilo; i++) {
                        Folio folio = gen.generar(SEDE, EN_2027, null);
                        folios.add(folio.valor());
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        listos.await();
        partida.countDown();
        pool.shutdown();
        assertThat(pool.awaitTermination(10, TimeUnit.SECONDS)).isTrue();

        // Cada folio es único => no hubo dos reservas con el mismo correlativo.
        assertThat(folios).hasSize(total);
        List<String> esperados = java.util.stream.IntStream.rangeClosed(1, total)
                .mapToObj(n -> "RM-" + n + "/2027").toList();
        assertThat(folios).containsExactlyInAnyOrderElementsOf(esperados);
    }
}
