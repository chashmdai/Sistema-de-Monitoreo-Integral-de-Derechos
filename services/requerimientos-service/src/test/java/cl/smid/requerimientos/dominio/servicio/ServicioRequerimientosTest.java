package cl.smid.requerimientos.dominio.servicio;

import cl.smid.requerimientos.dominio.excepcion.ReglaNegocioViolada;
import cl.smid.requerimientos.dominio.excepcion.SolicitudInvalida;
import cl.smid.requerimientos.dominio.modelo.AccionAdmisibilidad;
import cl.smid.requerimientos.dominio.modelo.Alcance;
import cl.smid.requerimientos.dominio.modelo.CanalIngreso;
import cl.smid.requerimientos.dominio.modelo.EstadoRequerimiento;
import cl.smid.requerimientos.dominio.modelo.Requerimiento;
import cl.smid.requerimientos.dominio.puerto.entrada.GestionRequerimientosUseCase.ComandoAdmisibilidad;
import cl.smid.requerimientos.dominio.puerto.entrada.GestionRequerimientosUseCase.ComandoAgregarNna;
import cl.smid.requerimientos.dominio.puerto.entrada.GestionRequerimientosUseCase.ComandoCrear;
import cl.smid.requerimientos.dominio.puerto.entrada.GestionRequerimientosUseCase.ContextoUsuario;
import cl.smid.requerimientos.dominio.puerto.entrada.GestionRequerimientosUseCase.DerechoSolicitado;
import cl.smid.requerimientos.soporte.CatalogoDerechosFalso;
import cl.smid.requerimientos.soporte.CorrelativoEnMemoria;
import cl.smid.requerimientos.soporte.DirectorioIdentidadFalso;
import cl.smid.requerimientos.soporte.DirectorioPersonasFalso;
import cl.smid.requerimientos.soporte.DirectorioSedesFalso;
import cl.smid.requerimientos.soporte.EventoPublicadorEnMemoria;
import cl.smid.requerimientos.soporte.GeneradorAltKeySecuencial;
import cl.smid.requerimientos.soporte.RelojFijo;
import cl.smid.requerimientos.soporte.RepositorioRequerimientosEnMemoria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("ServicioRequerimientos — orquestación del ingreso y la admisibilidad")
class ServicioRequerimientosTest {

    private static final Instant T0 = Instant.parse("2027-04-10T09:00:00Z");
    private static final String SEDE = "sede-a";
    private static final String UNIDAD = "unidad-1";

    private RepositorioRequerimientosEnMemoria repositorio;
    private DirectorioPersonasFalso personas;
    private CatalogoDerechosFalso catalogo;
    private DirectorioIdentidadFalso identidad;
    private EventoPublicadorEnMemoria eventos;
    private ServicioRequerimientos servicio;

    private ContextoUsuario coordinadorNacional() {
        return new ContextoUsuario("coord-1", SEDE, UNIDAD, Alcance.NACIONAL, Set.of("COORDINADOR"));
    }

    @BeforeEach
    void preparar() {
        repositorio = new RepositorioRequerimientosEnMemoria();
        personas = new DirectorioPersonasFalso().registrar("nna-1", "NNA Uno", "11.111.111-1");
        catalogo = new CatalogoDerechosFalso().registrarDerecho("der-1");
        identidad = new DirectorioIdentidadFalso();
        eventos = new EventoPublicadorEnMemoria();

        GeneradorFolio generadorFolio = new GeneradorFolio(
                new DirectorioSedesFalso().registrar(SEDE, "RM"),
                new CorrelativoEnMemoria(),
                LocalDate.parse("2027-01-01"));

        servicio = new ServicioRequerimientos(repositorio, generadorFolio, personas, catalogo,
                identidad, eventos, new RelojFijo(T0), new GeneradorAltKeySecuencial(), false);
    }

    /** Crea un requerimiento y lo lleva a INGRESADO (canal, sede y un NNA). */
    private Requerimiento ingresado() {
        ContextoUsuario ctx = coordinadorNacional();
        Requerimiento req = servicio.crear(new ComandoCrear(CanalIngreso.WEB, null, null, UNIDAD,
                "Caso de prueba", null, null), ctx);
        servicio.agregarNna(req.altKey(),
                new ComandoAgregarNna("nna-1", List.of(new DerechoSolicitado("der-1", null))), ctx);
        return servicio.enviar(req.altKey(), ctx);
    }

    @Test
    @DisplayName("Crear asigna folio oficial y deja el requerimiento en BORRADOR")
    void crearAsignaFolio() {
        Requerimiento req = servicio.crear(new ComandoCrear(CanalIngreso.WEB, null, null, UNIDAD,
                null, null, null), coordinadorNacional());
        assertThat(req.estado()).isEqualTo(EstadoRequerimiento.BORRADOR);
        assertThat(req.folio().valor()).isEqualTo("RM-1/2027");
    }

    @Test
    @DisplayName("Agregar NNA con un derecho inexistente en el Catálogo lanza REQ-001")
    void nnaConDerechoInexistente() {
        Requerimiento req = servicio.crear(new ComandoCrear(CanalIngreso.WEB, null, null, UNIDAD,
                null, null, null), coordinadorNacional());
        ComandoAgregarNna cmd = new ComandoAgregarNna("nna-1",
                List.of(new DerechoSolicitado("der-INEXISTENTE", null)));
        assertThatThrownBy(() -> servicio.agregarNna(req.altKey(), cmd, coordinadorNacional()))
                .isInstanceOf(SolicitudInvalida.class);
    }

    @Test
    @DisplayName("INADMISIBLE deja el requerimiento INADMISIBLE y emite requerimiento.inadmisible")
    void admisibilidadInadmisible() {
        Requerimiento ingresado = ingresado();
        Requerimiento decidido = servicio.decidirAdmisibilidad(ingresado.altKey(),
                new ComandoAdmisibilidad(AccionAdmisibilidad.INADMISIBLE, true, null, "No corresponde"),
                coordinadorNacional());

        assertThat(decidido.estado()).isEqualTo(EstadoRequerimiento.INADMISIBLE);
        assertThat(eventos.contieneTipo("requerimiento.inadmisible")).isTrue();
    }

    @Test
    @DisplayName("RESPUESTA_INMEDIATA registra y NO envía comunicación; emite requerimiento.respondido")
    void admisibilidadRespuestaInmediata() {
        Requerimiento ingresado = ingresado();
        Requerimiento decidido = servicio.decidirAdmisibilidad(ingresado.altKey(),
                new ComandoAdmisibilidad(AccionAdmisibilidad.RESPUESTA_INMEDIATA, false, null, "Orientación dada"),
                coordinadorNacional());

        assertThat(decidido.estado()).isEqualTo(EstadoRequerimiento.RESPONDIDO);
        assertThat(eventos.contieneTipo("requerimiento.respondido")).isTrue();
        // No existe canal de comunicación saliente: el único efecto observable es el evento de dominio.
        assertThat(eventos.conteoTipo("requerimiento.respondido")).isEqualTo(1);
    }

    @Test
    @DisplayName("ASIGNACION con profesional de la unidad de destino cierra en ASIGNADO y emite el evento")
    void admisibilidadAsignacionUnidadCorrecta() {
        identidad.registrar("prof-1", UNIDAD);
        Requerimiento ingresado = ingresado();
        Requerimiento decidido = servicio.decidirAdmisibilidad(ingresado.altKey(),
                new ComandoAdmisibilidad(AccionAdmisibilidad.ASIGNACION, false, "prof-1", null),
                coordinadorNacional());

        assertThat(decidido.estado()).isEqualTo(EstadoRequerimiento.ASIGNADO);
        assertThat(eventos.contieneTipo("requerimiento.asignado")).isTrue();
    }

    @Test
    @DisplayName("ASIGNACION a un profesional de otra unidad se rechaza con REQ-422")
    void admisibilidadAsignacionUnidadDistinta() {
        identidad.registrar("prof-2", "unidad-OTRA");
        Requerimiento ingresado = ingresado();

        ComandoAdmisibilidad cmd = new ComandoAdmisibilidad(
                AccionAdmisibilidad.ASIGNACION, false, "prof-2", null);
        assertThatThrownBy(() -> servicio.decidirAdmisibilidad(ingresado.altKey(), cmd, coordinadorNacional()))
                .isInstanceOf(ReglaNegocioViolada.class);
    }
}
