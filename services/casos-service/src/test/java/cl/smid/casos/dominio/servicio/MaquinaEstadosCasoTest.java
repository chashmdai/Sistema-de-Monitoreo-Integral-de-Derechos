package cl.smid.casos.dominio.servicio;

import cl.smid.casos.dominio.excepcion.TransicionInvalidaException;
import cl.smid.casos.dominio.modelo.AccionCaso;
import cl.smid.casos.dominio.modelo.EstadoCaso;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/** Verifica la tabla de transiciones del expediente: caminos válidos e inválidos. */
class MaquinaEstadosCasoTest {

    private final MaquinaEstadosCaso maquina = new MaquinaEstadosCaso();

    @Test
    void transicionesValidasDesdeAbierto() {
        assertThat(maquina.transicionar(EstadoCaso.ABIERTO, AccionCaso.INICIAR_INVESTIGACION))
                .isEqualTo(EstadoCaso.EN_INVESTIGACION);
        assertThat(maquina.transicionar(EstadoCaso.ABIERTO, AccionCaso.CERRAR))
                .isEqualTo(EstadoCaso.CERRADO);
    }

    @Test
    void cicloInvestigacionSeguimientoSuspension() {
        assertThat(maquina.transicionar(EstadoCaso.EN_INVESTIGACION, AccionCaso.DERIVAR_A_SEGUIMIENTO))
                .isEqualTo(EstadoCaso.EN_SEGUIMIENTO);
        assertThat(maquina.transicionar(EstadoCaso.EN_SEGUIMIENTO, AccionCaso.REANUDAR))
                .isEqualTo(EstadoCaso.EN_INVESTIGACION);
        assertThat(maquina.transicionar(EstadoCaso.EN_INVESTIGACION, AccionCaso.SUSPENDER))
                .isEqualTo(EstadoCaso.SUSPENDIDO);
        assertThat(maquina.transicionar(EstadoCaso.SUSPENDIDO, AccionCaso.REANUDAR))
                .isEqualTo(EstadoCaso.EN_INVESTIGACION);
    }

    @Test
    void reaperturaYArchivoDesdeCerrado() {
        assertThat(maquina.transicionar(EstadoCaso.CERRADO, AccionCaso.REABRIR))
                .isEqualTo(EstadoCaso.EN_INVESTIGACION);
        assertThat(maquina.transicionar(EstadoCaso.CERRADO, AccionCaso.ARCHIVAR))
                .isEqualTo(EstadoCaso.ARCHIVADO);
    }

    @Test
    void transicionInvalidaLanzaExcepcion() {
        assertThatThrownBy(() -> maquina.transicionar(EstadoCaso.ABIERTO, AccionCaso.ARCHIVAR))
                .isInstanceOf(TransicionInvalidaException.class);
        assertThatThrownBy(() -> maquina.transicionar(EstadoCaso.ARCHIVADO, AccionCaso.REABRIR))
                .isInstanceOf(TransicionInvalidaException.class);
    }

    @Test
    void archivadoEsTerminal() {
        assertThat(maquina.esTerminal(EstadoCaso.ARCHIVADO)).isTrue();
        assertThat(maquina.esTerminal(EstadoCaso.CERRADO)).isFalse();
    }
}
