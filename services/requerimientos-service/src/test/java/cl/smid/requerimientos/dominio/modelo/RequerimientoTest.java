package cl.smid.requerimientos.dominio.modelo;

import cl.smid.requerimientos.dominio.excepcion.CodigoError;
import cl.smid.requerimientos.dominio.excepcion.ReglaNegocioViolada;
import cl.smid.requerimientos.dominio.excepcion.RequerimientoException;
import cl.smid.requerimientos.dominio.excepcion.TransicionInvalida;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Requerimiento — invariantes del agregado raíz")
class RequerimientoTest {

    private static final Instant T0 = Instant.parse("2027-03-01T12:00:00Z");
    private static final Folio FOLIO = new Folio("RM-1/2027");

    private Requerimiento borradorMinimo() {
        return Requerimiento.crear("req-1", FOLIO, "sede-a", false, "usuario-1",
                CanalIngreso.PRESENCIAL, null, null, "unidad-1", "Resumen", null, null, T0);
    }

    @Test
    @DisplayName("Complejidad MEDIANA/ALTA exige ficha reservada; BAJA no")
    void fichaReservadaSegunComplejidad() {
        Requerimiento mediana = Requerimiento.crear("req-2", FOLIO, "sede-a", false, "u",
                CanalIngreso.PRESENCIAL, Complejidad.MEDIANA, null, "unidad-1", null, null, null, T0);
        Requerimiento baja = Requerimiento.crear("req-3", FOLIO, "sede-a", false, "u",
                CanalIngreso.PRESENCIAL, Complejidad.BAJA, null, "unidad-1", null, null, null, T0);

        assertThat(mediana.requiereFichaReservada()).isTrue();
        assertThat(baja.requiereFichaReservada()).isFalse();
    }

    @Test
    @DisplayName("Enviar sin los mínimos (canal y NNA) lanza REQ-422")
    void enviarSinMinimos() {
        Requerimiento sinCanalNiNna = Requerimiento.crear("req-4", FOLIO, "sede-a", false, "u",
                null, null, null, "unidad-1", null, null, null, T0);

        assertThatThrownBy(() -> sinCanalNiNna.enviar(T0))
                .isInstanceOf(ReglaNegocioViolada.class)
                .extracting(e -> ((RequerimientoException) e).codigoError())
                .isEqualTo(CodigoError.REGLA_NEGOCIO);
    }

    @Test
    @DisplayName("El ciclo feliz lleva el requerimiento a ASIGNADO y lo cierra a edición")
    void cicloHastaAsignado() {
        Requerimiento req = borradorMinimo();
        req.agregarNna(NnaAfectado.nuevo("nna-1", null, List.of()), T0);
        req.enviar(T0);
        assertThat(req.estado()).isEqualTo(EstadoRequerimiento.INGRESADO);

        req.abrirAdmisibilidad(T0);
        assertThat(req.estado()).isEqualTo(EstadoRequerimiento.EN_ADMISIBILIDAD);

        Admisibilidad asignacion = new Admisibilidad(null, AccionAdmisibilidad.ASIGNACION,
                "coord-1", false, "prof-1", null, T0);
        req.decidir(asignacion, T0);
        assertThat(req.estado()).isEqualTo(EstadoRequerimiento.ASIGNADO);

        // Cerrado a edición: cualquier intento de editar lanza REQ-409.
        assertThatThrownBy(() -> req.editar(CanalIngreso.WEB, null, null, null, null, null, null, T0))
                .isInstanceOf(TransicionInvalida.class)
                .extracting(e -> ((RequerimientoException) e).codigoError())
                .isEqualTo(CodigoError.CONFLICTO);
    }

    @Test
    @DisplayName("ASIGNACION sin profesional lanza REQ-422")
    void asignacionSinProfesional() {
        Requerimiento req = borradorMinimo();
        req.agregarNna(NnaAfectado.nuevo("nna-1", null, List.of()), T0);
        req.enviar(T0);
        req.abrirAdmisibilidad(T0);

        Admisibilidad sinProfesional = new Admisibilidad(null, AccionAdmisibilidad.ASIGNACION,
                "coord-1", false, null, null, T0);

        assertThatThrownBy(() -> req.decidir(sinProfesional, T0))
                .isInstanceOf(ReglaNegocioViolada.class);
    }
}
