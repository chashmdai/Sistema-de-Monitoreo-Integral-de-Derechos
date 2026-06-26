package cl.smid.requerimientos.dominio.servicio;

import cl.smid.requerimientos.dominio.excepcion.CodigoError;
import cl.smid.requerimientos.dominio.excepcion.TransicionInvalida;
import cl.smid.requerimientos.dominio.modelo.AccionAdmisibilidad;
import cl.smid.requerimientos.dominio.modelo.EstadoRequerimiento;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("MaquinaEstados — tabla de transiciones pura")
class MaquinaEstadosTest {

    @Test
    @DisplayName("Las transiciones válidas del ciclo de vida producen el estado esperado")
    void transicionesValidas() {
        assertThat(MaquinaEstados.siguiente(EstadoRequerimiento.BORRADOR, EventoTransicion.ENVIAR))
                .isEqualTo(EstadoRequerimiento.INGRESADO);
        assertThat(MaquinaEstados.siguiente(EstadoRequerimiento.INGRESADO, EventoTransicion.ABRIR_ADMISIBILIDAD))
                .isEqualTo(EstadoRequerimiento.EN_ADMISIBILIDAD);
        assertThat(MaquinaEstados.siguiente(EstadoRequerimiento.EN_ADMISIBILIDAD, EventoTransicion.DECIDIR_INADMISIBLE))
                .isEqualTo(EstadoRequerimiento.INADMISIBLE);
        assertThat(MaquinaEstados.siguiente(EstadoRequerimiento.EN_ADMISIBILIDAD, EventoTransicion.DECIDIR_RESPUESTA_INMEDIATA))
                .isEqualTo(EstadoRequerimiento.RESPONDIDO);
        assertThat(MaquinaEstados.siguiente(EstadoRequerimiento.EN_ADMISIBILIDAD, EventoTransicion.DECIDIR_ASIGNACION))
                .isEqualTo(EstadoRequerimiento.ASIGNADO);
    }

    @Test
    @DisplayName("Una transición inexistente lanza TransicionInvalida con código REQ-409")
    void transicionInvalida() {
        assertThatThrownBy(() ->
                MaquinaEstados.siguiente(EstadoRequerimiento.BORRADOR, EventoTransicion.ABRIR_ADMISIBILIDAD))
                .isInstanceOf(TransicionInvalida.class)
                .extracting(e -> ((TransicionInvalida) e).codigoError())
                .isEqualTo(CodigoError.CONFLICTO);
    }

    @Test
    @DisplayName("No se puede decidir admisibilidad desde un estado terminal")
    void noSePuedeDecidirDesdeTerminal() {
        assertThat(MaquinaEstados.puedeTransitar(EstadoRequerimiento.ASIGNADO, EventoTransicion.DECIDIR_INADMISIBLE))
                .isFalse();
        assertThat(MaquinaEstados.puedeTransitar(EstadoRequerimiento.INADMISIBLE, EventoTransicion.DECIDIR_ASIGNACION))
                .isFalse();
    }

    @Test
    @DisplayName("eventoDeAccion mapea cada acción de admisibilidad a su evento")
    void mapeoAccionEvento() {
        assertThat(MaquinaEstados.eventoDeAccion(AccionAdmisibilidad.INADMISIBLE))
                .isEqualTo(EventoTransicion.DECIDIR_INADMISIBLE);
        assertThat(MaquinaEstados.eventoDeAccion(AccionAdmisibilidad.RESPUESTA_INMEDIATA))
                .isEqualTo(EventoTransicion.DECIDIR_RESPUESTA_INMEDIATA);
        assertThat(MaquinaEstados.eventoDeAccion(AccionAdmisibilidad.ASIGNACION))
                .isEqualTo(EventoTransicion.DECIDIR_ASIGNACION);
    }
}
