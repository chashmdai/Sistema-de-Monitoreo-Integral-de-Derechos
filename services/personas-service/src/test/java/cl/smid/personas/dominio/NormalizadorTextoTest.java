package cl.smid.personas.dominio;

import cl.smid.personas.dominio.servicio.NormalizadorTexto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Verifica las utilidades de normalización de texto que sustentan la deduplicación y la
 * búsqueda difusa (Núcleo 5.5): supresión de acentos, minúsculas, colapso de espacios y
 * variante alfanumérica.
 */
class NormalizadorTextoTest {

    @Test
    @DisplayName("Quita acentos, pasa a minúsculas y colapsa espacios")
    void normalizaBasico() {
        assertThat(NormalizadorTexto.normalizar("José  PÉREZ")).isEqualTo("jose perez");
        assertThat(NormalizadorTexto.normalizar("  Ñuñoa  ")).isEqualTo("ñuñoa");
        assertThat(NormalizadorTexto.normalizar("MARÍA josé")).isEqualTo("maria jose");
    }

    @Test
    @DisplayName("Dos grafías equivalentes convergen a la misma forma")
    void convergenciaDeGrafias() {
        assertThat(NormalizadorTexto.normalizar("José Pérez"))
                .isEqualTo(NormalizadorTexto.normalizar("jose   perez"));
    }

    @Test
    @DisplayName("La entrada nula o en blanco produce cadena vacía")
    void nuloOVacio() {
        assertThat(NormalizadorTexto.normalizar(null)).isEmpty();
        assertThat(NormalizadorTexto.normalizar("   ")).isEmpty();
    }

    @Test
    @DisplayName("La variante alfanumérica elimina separadores y signos")
    void alfanumerico() {
        assertThat(NormalizadorTexto.normalizarAlfanumerico("O'Higgins")).isEqualTo("ohiggins");
        assertThat(NormalizadorTexto.normalizarAlfanumerico("De la Fuente")).isEqualTo("delafuente");
        assertThat(NormalizadorTexto.normalizarAlfanumerico("Pérez-Rosales")).isEqualTo("perezrosales");
        assertThat(NormalizadorTexto.normalizarAlfanumerico(null)).isEmpty();
    }
}
