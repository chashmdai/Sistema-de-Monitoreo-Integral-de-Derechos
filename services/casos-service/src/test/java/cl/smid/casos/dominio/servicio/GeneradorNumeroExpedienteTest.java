package cl.smid.casos.dominio.servicio;

import cl.smid.casos.dominio.modelo.NumeroExpediente;
import cl.smid.casos.dominio.modelo.SerieExpediente;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/** Verifica el formato del número de expediente y el aislamiento de la serie BETA. */
class GeneradorNumeroExpedienteTest {

    private final GeneradorNumeroExpediente generador = new GeneradorNumeroExpediente();

    @Test
    void formatoSerieOficial() {
        NumeroExpediente numero = generador.generar("RM", SerieExpediente.OFICIAL, 1, 2027);
        assertThat(numero.valor()).isEqualTo("EXP-RM-1/2027");
        assertThat(numero.serie()).isEqualTo(SerieExpediente.OFICIAL);
        assertThat(numero.correlativo()).isEqualTo(1);
        assertThat(numero.anio()).isEqualTo(2027);
    }

    @Test
    void formatoSerieBetaLlevaPrefijoB() {
        NumeroExpediente numero = generador.generar("RM", SerieExpediente.BETA, 7, 2026);
        assertThat(numero.valor()).isEqualTo("EXP-RM-B7/2026");
        assertThat(numero.serie()).isEqualTo(SerieExpediente.BETA);
    }
}
