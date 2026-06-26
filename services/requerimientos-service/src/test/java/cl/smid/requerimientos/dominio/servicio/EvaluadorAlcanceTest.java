package cl.smid.requerimientos.dominio.servicio;

import cl.smid.requerimientos.dominio.modelo.Alcance;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("EvaluadorAlcance — visibilidad territorial registro a registro (Núcleo 2.3)")
class EvaluadorAlcanceTest {

    private static final String SEDE_A = "sede-a";
    private static final String SEDE_B = "sede-b";
    private static final String UNIDAD_1 = "unidad-1";
    private static final String UNIDAD_2 = "unidad-2";

    @Test
    @DisplayName("NACIONAL ve cualquier requerimiento")
    void nacionalVeTodo() {
        assertThat(EvaluadorAlcance.puedeVer(Alcance.NACIONAL, SEDE_A, UNIDAD_1, SEDE_B, UNIDAD_2)).isTrue();
    }

    @Test
    @DisplayName("SEDE ve solo los de su sede")
    void sedeVeSuSede() {
        assertThat(EvaluadorAlcance.puedeVer(Alcance.SEDE, SEDE_A, UNIDAD_1, SEDE_A, UNIDAD_2)).isTrue();
        assertThat(EvaluadorAlcance.puedeVer(Alcance.SEDE, SEDE_A, UNIDAD_1, SEDE_B, UNIDAD_2)).isFalse();
    }

    @Test
    @DisplayName("UNIDAD ve solo los de su unidad de destino")
    void unidadVeSuUnidad() {
        assertThat(EvaluadorAlcance.puedeVer(Alcance.UNIDAD, SEDE_A, UNIDAD_1, SEDE_A, UNIDAD_1)).isTrue();
        assertThat(EvaluadorAlcance.puedeVer(Alcance.UNIDAD, SEDE_A, UNIDAD_1, SEDE_A, UNIDAD_2)).isFalse();
    }

    @Test
    @DisplayName("UNIDAD no ve un requerimiento sin unidad de destino asignada")
    void unidadNoVeSinUnidad() {
        assertThat(EvaluadorAlcance.puedeVer(Alcance.UNIDAD, SEDE_A, UNIDAD_1, SEDE_A, null)).isFalse();
    }
}
