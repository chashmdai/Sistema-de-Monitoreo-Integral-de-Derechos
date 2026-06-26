package cl.smid.personas.dominio;

import cl.smid.personas.dominio.modelo.CriterioDuplicados;
import cl.smid.personas.dominio.modelo.Persona;
import cl.smid.personas.dominio.modelo.ResultadoDuplicados;
import cl.smid.personas.dominio.modelo.TipoPersona;
import cl.smid.personas.dominio.servicio.BuscadorDuplicadosBaseLocal;
import cl.smid.personas.soporte.PersonaRepositorioEnMemoria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Verifica la prevalidación de duplicados sobre la base local (Núcleo 5.5): coincidencia exacta
 * por RUT, coincidencias probables por similitud de nombre/fecha, orden por score y el caso sin
 * datos suficientes.
 */
class BuscadorDuplicadosBaseLocalTest {

    private PersonaRepositorioEnMemoria repositorio;
    private BuscadorDuplicadosBaseLocal buscador;

    @BeforeEach
    void preparar() {
        repositorio = new PersonaRepositorioEnMemoria();
        buscador = new BuscadorDuplicadosBaseLocal(repositorio);
    }

    @Test
    @DisplayName("Detecta coincidencia exacta por RUT con motivo 'RUT'")
    void exactaPorRut() {
        guardar("alt-rut", TipoPersona.ADULTO, "12345678-5", "Ana", "Soto", "Vega",
                LocalDate.of(1990, 1, 1));

        CriterioDuplicados criterio = new CriterioDuplicados(
                TipoPersona.ADULTO, "12.345.678-5", null, null, null, null);

        ResultadoDuplicados resultado = buscador.buscar(criterio);

        assertThat(resultado.coincidenciaExacta()).isPresent();
        assertThat(resultado.coincidenciaExacta().get().altKey()).isEqualTo("alt-rut");
        assertThat(resultado.coincidenciaExacta().get().motivo()).isEqualTo("RUT");
    }

    @Test
    @DisplayName("Detecta coincidencias probables por nombre y fecha, ordenadas por score")
    void probablesOrdenadas() {
        LocalDate nacimiento = LocalDate.of(2015, 3, 10);
        // A: nombre idéntico al criterio → score máximo.
        guardar("alt-a", TipoPersona.NNA, null, "Juan", "Pérez", "González", nacimiento);
        // B: apellido parecido → score alto pero menor que A.
        guardar("alt-b", TipoPersona.NNA, null, "Juan", "Pereira", "Rojas", nacimiento);
        // C: sin relación de nombre ni fecha → no debe aparecer.
        guardar("alt-c", TipoPersona.NNA, null, "Sofía", "Martínez", "Lagos",
                LocalDate.of(2001, 12, 5));

        CriterioDuplicados criterio = new CriterioDuplicados(
                TipoPersona.NNA, null, "Juan", "Perez", null, nacimiento);

        ResultadoDuplicados resultado = buscador.buscar(criterio);

        assertThat(resultado.coincidenciasProbables()).hasSizeGreaterThanOrEqualTo(2);
        // El primero es la coincidencia de mayor score (nombre idéntico).
        assertThat(resultado.coincidenciasProbables().get(0).altKey()).isEqualTo("alt-a");
        // Orden descendente por score.
        double s0 = resultado.coincidenciasProbables().get(0).score();
        double s1 = resultado.coincidenciasProbables().get(1).score();
        assertThat(s0).isGreaterThanOrEqualTo(s1);
        // 'alt-c' no debe estar entre los resultados.
        assertThat(resultado.coincidenciasProbables())
                .noneMatch(c -> c.altKey().equals("alt-c"));
        // Sólo se expone el dato mínimo: nombre legible compuesto.
        assertThat(resultado.coincidenciasProbables().get(0).nombre()).isEqualTo("Juan Pérez");
    }

    @Test
    @DisplayName("Sin RUT ni nombre ni fecha, no devuelve coincidencias")
    void sinDatosSuficientes() {
        guardar("alt-x", TipoPersona.NNA, null, "Juan", "Pérez", "González",
                LocalDate.of(2015, 3, 10));

        CriterioDuplicados criterio = new CriterioDuplicados(
                TipoPersona.NNA, null, null, null, null, null);

        ResultadoDuplicados resultado = buscador.buscar(criterio);

        assertThat(resultado.coincidenciaExacta()).isEmpty();
        assertThat(resultado.coincidenciasProbables()).isEmpty();
    }

    // ----------------------------- Apoyo -----------------------------

    private void guardar(String altKey, TipoPersona tipo, String rutCanonico,
                         String nombres, String apPaterno, String apMaterno, LocalDate fechaNac) {
        Persona p = Persona.builder()
                .altKey(altKey)
                .tipo(tipo)
                .rut(rutCanonico)
                .nombres(nombres)
                .apellidoPaterno(apPaterno)
                .apellidoMaterno(apMaterno)
                .fechaNacimiento(fechaNac)
                .idSede("sede-x")
                .idUnidad("unidad-x")
                .vigente(true)
                .construir();
        repositorio.guardar(p);
    }
}
