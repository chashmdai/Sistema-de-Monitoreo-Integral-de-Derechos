package cl.smid.antecedentes.dominio;

import cl.smid.antecedentes.dominio.modelo.Folio;
import cl.smid.antecedentes.dominio.servicio.GeneradorFolio;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GeneradorFolioTest {

    private static Instant enAnio(int anio) {
        return LocalDateTime.of(anio, 3, 15, 12, 0).toInstant(ZoneOffset.UTC);
    }

    @Test
    void seriesIndependientesYFormato() {
        DoblesEnMemoria.CorrelativoFake correlativo = new DoblesEnMemoria.CorrelativoFake();
        DoblesEnMemoria.RelojFijo reloj = new DoblesEnMemoria.RelojFijo(enAnio(2027));
        GeneradorFolio generador = new GeneradorFolio(correlativo, reloj);

        assertEquals("FA-1/2027", generador.siguienteFicha().valor());
        assertEquals("FA-2/2027", generador.siguienteFicha().valor());
        // La serie de hallazgos es independiente y arranca en 1.
        assertEquals("HZ-1/2027", generador.siguienteHallazgo().valor());
        assertEquals("FA-3/2027", generador.siguienteFicha().valor());
    }

    @Test
    void reinicioAnual() {
        DoblesEnMemoria.CorrelativoFake correlativo = new DoblesEnMemoria.CorrelativoFake();
        DoblesEnMemoria.RelojFijo reloj = new DoblesEnMemoria.RelojFijo(enAnio(2027));
        GeneradorFolio generador = new GeneradorFolio(correlativo, reloj);

        assertEquals("FA-1/2027", generador.siguienteFicha().valor());
        reloj.avanzar(enAnio(2028));
        Folio primeroSiguienteAnio = generador.siguienteFicha();
        assertEquals("FA-1/2028", primeroSiguienteAnio.valor());
    }
}
