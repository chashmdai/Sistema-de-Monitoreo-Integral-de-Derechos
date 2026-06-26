package cl.smid.antecedentes.dominio;

import cl.smid.antecedentes.dominio.excepcion.ReglaNegocioException;
import cl.smid.antecedentes.dominio.modelo.AccionRevision;
import cl.smid.antecedentes.dominio.modelo.EstadoFicha;
import cl.smid.antecedentes.dominio.servicio.MaquinaEstadosFicha;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MaquinaEstadosFichaTest {

    private final MaquinaEstadosFicha maquina = new MaquinaEstadosFicha();

    @Test
    void transicionesValidas() {
        assertEquals(EstadoFicha.EN_REVISION,
                maquina.siguiente(EstadoFicha.BORRADOR, AccionRevision.ENVIAR_REVISION));
        assertEquals(EstadoFicha.BORRADOR,
                maquina.siguiente(EstadoFicha.EN_REVISION, AccionRevision.DEVOLVER));
        assertEquals(EstadoFicha.APROBADA,
                maquina.siguiente(EstadoFicha.EN_REVISION, AccionRevision.APROBAR));
        assertEquals(EstadoFicha.RECHAZADA,
                maquina.siguiente(EstadoFicha.EN_REVISION, AccionRevision.RECHAZAR));
    }

    @Test
    void noSePuedeEnviarRevisionDesdeEstadoTerminal() {
        assertThrows(ReglaNegocioException.class,
                () -> maquina.siguiente(EstadoFicha.APROBADA, AccionRevision.ENVIAR_REVISION));
    }

    @Test
    void noSePuedeAprobarUnBorrador() {
        assertThrows(ReglaNegocioException.class,
                () -> maquina.siguiente(EstadoFicha.BORRADOR, AccionRevision.APROBAR));
    }

    @Test
    void noSePuedeDevolverUnEstadoTerminal() {
        assertThrows(ReglaNegocioException.class,
                () -> maquina.siguiente(EstadoFicha.RECHAZADA, AccionRevision.DEVOLVER));
    }
}
