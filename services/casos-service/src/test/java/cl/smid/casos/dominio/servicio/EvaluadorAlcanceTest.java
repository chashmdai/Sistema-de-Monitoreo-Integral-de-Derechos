package cl.smid.casos.dominio.servicio;

import cl.smid.casos.dominio.modelo.Alcance;
import cl.smid.casos.dominio.modelo.Caso;
import cl.smid.casos.dominio.modelo.ContextoTerritorial;
import cl.smid.casos.dominio.modelo.EstadoCaso;
import cl.smid.casos.dominio.modelo.NumeroExpediente;
import cl.smid.casos.dominio.modelo.SerieExpediente;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/** Verifica la visibilidad territorial registro a registro (NACIONAL / SEDE / UNIDAD). */
class EvaluadorAlcanceTest {

    private final EvaluadorAlcance evaluador = new EvaluadorAlcance();

    private Caso casoEn(String sedeAlt, String unidadAlt) {
        NumeroExpediente numero = new NumeroExpediente("EXP-RM-1/2027", "RM", SerieExpediente.OFICIAL, 1, 2027);
        Instant t = Instant.parse("2027-02-01T10:00:00Z");
        return Caso.reconstituir("caso-1", numero, "req-1", "F-1", sedeAlt, unidadAlt, "prof-1",
                EstadoCaso.ABIERTO, null, false, false, t, null, t, t, "sistema", List.of());
    }

    private ContextoTerritorial ctx(Alcance alcance, String sedeAlt, String unidadAlt) {
        return new ContextoTerritorial("user-1", Set.of("PROFESIONAL"), sedeAlt, unidadAlt, alcance, "Ana");
    }

    @Test
    void nacionalVeTodo() {
        Caso caso = casoEn("sede-A", "unidad-A");
        assertThat(evaluador.puedeVer(caso, ctx(Alcance.NACIONAL, "sede-Z", "unidad-Z"))).isTrue();
    }

    @Test
    void sedeVeSoloSuSede() {
        Caso caso = casoEn("sede-A", "unidad-A");
        assertThat(evaluador.puedeVer(caso, ctx(Alcance.SEDE, "sede-A", "unidad-X"))).isTrue();
        assertThat(evaluador.puedeVer(caso, ctx(Alcance.SEDE, "sede-B", "unidad-X"))).isFalse();
    }

    @Test
    void unidadVeSoloSuUnidad() {
        Caso caso = casoEn("sede-A", "unidad-A");
        assertThat(evaluador.puedeVer(caso, ctx(Alcance.UNIDAD, "sede-A", "unidad-A"))).isTrue();
        assertThat(evaluador.puedeVer(caso, ctx(Alcance.UNIDAD, "sede-A", "unidad-B"))).isFalse();
    }
}
