package cl.smid.personas.dominio;

import cl.smid.personas.dominio.servicio.CalculadorSimilitud;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Verifica el cálculo de similitud Jaro-Winkler usado en la deduplicación difusa (Núcleo 5.5):
 * identidad, disparidad, realce por prefijo común, rango y manejo de vacíos.
 */
class CalculadorSimilitudTest {

    @Test
    @DisplayName("Cadenas idénticas no vacías tienen similitud 1.0")
    void identicas() {
        assertThat(CalculadorSimilitud.similitud("juan", "juan")).isEqualTo(1.0);
    }

    @Test
    @DisplayName("Cadenas sin caracteres en común tienen similitud 0.0")
    void disjuntas() {
        assertThat(CalculadorSimilitud.similitud("juan", "wxyz")).isEqualTo(0.0);
    }

    @Test
    @DisplayName("Una pequeña transposición conserva una similitud alta")
    void transposicionLeve() {
        // Caso clásico: 'martha' vs 'marhta' ronda 0.96.
        assertThat(CalculadorSimilitud.similitud("martha", "marhta")).isGreaterThan(0.9);
    }

    @Test
    @DisplayName("El prefijo común realza la similitud (componente Winkler)")
    void realcePorPrefijo() {
        // Ambos pares tienen 7/8 coincidencias y 0 transposiciones (igual Jaro base),
        // pero sólo el primero comparte prefijo, por lo que debe puntuar más alto.
        double conPrefijo = CalculadorSimilitud.similitud("abcdefgh", "abcdxfgh");
        double sinPrefijo = CalculadorSimilitud.similitud("abcdefgh", "xbcdefgh");
        assertThat(conPrefijo).isGreaterThan(sinPrefijo);
    }

    @Test
    @DisplayName("La similitud siempre cae en el rango [0,1]")
    void rangoAcotado() {
        String[][] pares = {
                {"gonzalez", "gonzales"},
                {"maria", "mario"},
                {"perez", "peres"},
                {"a", "abcdefg"}
        };
        for (String[] par : pares) {
            double s = CalculadorSimilitud.similitud(par[0], par[1]);
            assertThat(s).isBetween(0.0, 1.0);
        }
    }

    @Test
    @DisplayName("Maneja cadenas vacías y nulas sin error")
    void vaciosYNulos() {
        assertThat(CalculadorSimilitud.similitud("", "")).isEqualTo(0.0);
        assertThat(CalculadorSimilitud.similitud("juan", "")).isEqualTo(0.0);
        assertThat(CalculadorSimilitud.similitud(null, "juan")).isEqualTo(0.0);
        assertThat(CalculadorSimilitud.similitud(null, null)).isEqualTo(0.0);
    }
}
