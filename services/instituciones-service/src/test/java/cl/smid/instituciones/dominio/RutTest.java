package cl.smid.instituciones.dominio;

import cl.smid.instituciones.dominio.excepcion.ReglaNegocioException;
import cl.smid.instituciones.dominio.modelo.Rut;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Pruebas del objeto de valor {@link Rut}: validación por módulo 11, normalización de
 * formato y reconstrucción desde persistencia.
 */
class RutTest {

    @Test
    void aceptaRutValidoSinFormato() {
        Rut rut = Rut.desde("123456785");
        assertThat(rut.cuerpo()).isEqualTo("12345678");
        assertThat(rut.dv()).isEqualTo('5');
        assertThat(rut.canonico()).isEqualTo("12345678-5");
    }

    @Test
    void aceptaRutConPuntosYGuion() {
        Rut rut = Rut.desde("12.345.678-5");
        assertThat(rut.cuerpo()).isEqualTo("12345678");
        assertThat(rut.dv()).isEqualTo('5');
    }

    @Test
    void aceptaDigitoVerificadorK() {
        Rut rut = Rut.desde("6-K");
        assertThat(rut.cuerpo()).isEqualTo("6");
        assertThat(rut.dv()).isEqualTo('K');
    }

    @Test
    void normalizaKMinuscula() {
        Rut rut = Rut.desde("6-k");
        assertThat(rut.dv()).isEqualTo('K');
    }

    @Test
    void rechazaDigitoVerificadorIncorrecto() {
        assertThatThrownBy(() -> Rut.desde("12345678-9"))
                .isInstanceOf(ReglaNegocioException.class);
    }

    @Test
    void rechazaCuerpoNoNumerico() {
        assertThatThrownBy(() -> Rut.desde("12A45678-5"))
                .isInstanceOf(ReglaNegocioException.class);
    }

    @Test
    void rechazaValorVacio() {
        assertThatThrownBy(() -> Rut.desde("   "))
                .isInstanceOf(ReglaNegocioException.class);
    }

    @Test
    void rechazaValorDemasiadoCorto() {
        assertThatThrownBy(() -> Rut.desde("5"))
                .isInstanceOf(ReglaNegocioException.class);
    }

    @Test
    void reconstruyeSinRevalidar() {
        // Reconstrucción desde persistencia: no recalcula el dígito verificador.
        Rut rut = Rut.reconstruir("99", '9');
        assertThat(rut.cuerpo()).isEqualTo("99");
        assertThat(rut.dv()).isEqualTo('9');
        assertThat(rut.canonico()).isEqualTo("99-9");
    }

    @Test
    void igualdadPorValor() {
        assertThat(Rut.desde("12345678-5")).isEqualTo(Rut.desde("12.345.678-5"));
    }
}
