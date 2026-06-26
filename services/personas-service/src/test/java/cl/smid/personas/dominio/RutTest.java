package cl.smid.personas.dominio;

import cl.smid.personas.dominio.excepcion.RutInvalidoException;
import cl.smid.personas.dominio.modelo.Rut;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Verifica el objeto de valor {@link Rut}: el algoritmo de módulo 11, la normalización de
 * formatos de captura y el rechazo de entradas inválidas (Núcleo 5.5).
 */
class RutTest {

    @Test
    @DisplayName("Acepta RUTs válidos y los deja en forma canónica")
    void aceptaValidos() {
        assertThat(Rut.normalizarYValidar("12345678-5").canonico()).isEqualTo("12345678-5");
        assertThat(Rut.normalizarYValidar("11111111-1").canonico()).isEqualTo("11111111-1");
    }

    @Test
    @DisplayName("Calcula correctamente el dígito verificador 'K'")
    void digitoK() {
        // El cuerpo '6' tiene dígito verificador K por módulo 11; cubre la rama del resto = 10.
        Rut rut = Rut.normalizarYValidar("6-K");
        assertThat(rut.dv()).isEqualTo('K');
        assertThat(rut.canonico()).isEqualTo("6-K");
    }

    @Test
    @DisplayName("Normaliza puntos, espacios y minúscula del DV")
    void normalizaFormatos() {
        // Con puntos de miles.
        assertThat(Rut.normalizarYValidar("12.345.678-5").canonico()).isEqualTo("12345678-5");
        // Sin guion: el último carácter es el DV.
        assertThat(Rut.normalizarYValidar("123456785").canonico()).isEqualTo("12345678-5");
        // DV en minúscula y espacios externos.
        assertThat(Rut.normalizarYValidar("  6-k  ").canonico()).isEqualTo("6-K");
    }

    @Test
    @DisplayName("El cálculo del DV es coherente para todos los cuerpos de prueba (round-trip)")
    void roundTrip() {
        int[] cuerpos = {1, 6, 999, 7654321, 12345678, 22222222};
        for (int n : cuerpos) {
            String cuerpo = String.valueOf(n);
            char dv = Rut.calcularDigitoVerificador(cuerpo);
            // Construido con su propio DV, debe validar sin excepción y conservar el cuerpo.
            Rut rut = Rut.normalizarYValidar(cuerpo + "-" + dv);
            assertThat(rut.cuerpo()).isEqualTo(cuerpo);
            assertThat(rut.dv()).isEqualTo(dv);
        }
    }

    @ParameterizedTest
    @DisplayName("Rechaza RUTs con dígito verificador incorrecto o formato inválido")
    @ValueSource(strings = {
            "12345678-9",   // DV incorrecto (esperado 5)
            "12345678-0",   // DV incorrecto
            "1234-X",       // DV no válido (ni dígito ni K)
            "123456789-1",  // cuerpo de 9 dígitos (excede el máximo)
            "abcdefg-1",    // cuerpo no numérico
            "1",            // demasiado corto
            ""              // vacío
    })
    void rechazaInvalidos(String entrada) {
        assertThatThrownBy(() -> Rut.normalizarYValidar(entrada))
                .isInstanceOf(RutInvalidoException.class);
    }

    @Test
    @DisplayName("Rechaza la entrada nula")
    void rechazaNulo() {
        assertThatThrownBy(() -> Rut.normalizarYValidar(null))
                .isInstanceOf(RutInvalidoException.class);
    }
}
