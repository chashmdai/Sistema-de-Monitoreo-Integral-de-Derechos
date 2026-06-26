package cl.smid.personas.dominio;

import cl.smid.personas.dominio.modelo.Alcance;
import cl.smid.personas.dominio.modelo.ContextoTerritorial;
import cl.smid.personas.dominio.servicio.EvaluadorAlcance;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Verifica la regla de visibilidad territorial registro a registro (Núcleo 2.3): el corazón de
 * la diferencia entre Personas (acceso acotado) y el Catálogo (dato compartido).
 */
class EvaluadorAlcanceTest {

    private static final String SEDE_A = "sede-aaa";
    private static final String SEDE_B = "sede-bbb";
    private static final String UNIDAD_1 = "unidad-111";
    private static final String UNIDAD_2 = "unidad-222";

    @Test
    @DisplayName("NACIONAL ve cualquier registro")
    void nacionalVeTodo() {
        ContextoTerritorial ctx = new ContextoTerritorial("u1", Alcance.NACIONAL, null, null);
        assertThat(EvaluadorAlcance.dentroDeAlcance(SEDE_A, UNIDAD_1, ctx)).isTrue();
        assertThat(EvaluadorAlcance.dentroDeAlcance(SEDE_B, UNIDAD_2, ctx)).isTrue();
        assertThat(EvaluadorAlcance.dentroDeAlcance(null, null, ctx)).isTrue();
    }

    @Test
    @DisplayName("SEDE ve sólo los registros de su misma sede")
    void sedeFiltraPorSede() {
        ContextoTerritorial ctx = new ContextoTerritorial("u1", Alcance.SEDE, SEDE_A, UNIDAD_1);
        assertThat(EvaluadorAlcance.dentroDeAlcance(SEDE_A, UNIDAD_2, ctx)).isTrue();
        assertThat(EvaluadorAlcance.dentroDeAlcance(SEDE_B, UNIDAD_1, ctx)).isFalse();
    }

    @Test
    @DisplayName("UNIDAD ve sólo los registros de su misma unidad")
    void unidadFiltraPorUnidad() {
        ContextoTerritorial ctx = new ContextoTerritorial("u1", Alcance.UNIDAD, SEDE_A, UNIDAD_1);
        assertThat(EvaluadorAlcance.dentroDeAlcance(SEDE_A, UNIDAD_1, ctx)).isTrue();
        assertThat(EvaluadorAlcance.dentroDeAlcance(SEDE_A, UNIDAD_2, ctx)).isFalse();
    }

    @Test
    @DisplayName("Un contexto nulo nunca concede acceso")
    void contextoNulo() {
        assertThat(EvaluadorAlcance.dentroDeAlcance(SEDE_A, UNIDAD_1, null)).isFalse();
    }
}
