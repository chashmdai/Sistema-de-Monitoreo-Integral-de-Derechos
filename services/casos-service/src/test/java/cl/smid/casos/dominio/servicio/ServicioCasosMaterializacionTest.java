package cl.smid.casos.dominio.servicio;

import cl.smid.casos.dominio.excepcion.AccesoDenegadoException;
import cl.smid.casos.dominio.excepcion.CasoNoEncontradoException;
import cl.smid.casos.dominio.excepcion.TransicionInvalidaException;
import cl.smid.casos.dominio.modelo.AccionCaso;
import cl.smid.casos.dominio.modelo.Alcance;
import cl.smid.casos.dominio.modelo.Caso;
import cl.smid.casos.dominio.modelo.Complejidad;
import cl.smid.casos.dominio.modelo.ContextoTerritorial;
import cl.smid.casos.dominio.modelo.EstadoCaso;
import cl.smid.casos.dominio.modelo.EventoRequerimientoAsignado;
import cl.smid.casos.dominio.puerto.entrada.TransicionarCaso.ComandoTransicion;
import cl.smid.casos.soporte.DoblesEnMemoria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/** Ejercita la materialización idempotente y el flujo de transiciones del servicio de Casos. */
class ServicioCasosMaterializacionTest {

    private static final String SEDE = "sede-RM";
    private static final String UNIDAD = "unidad-1";
    private static final Instant AHORA = Instant.parse("2027-02-01T10:00:00Z");

    private DoblesEnMemoria.RepositorioEnMemoria repositorio;
    private DoblesEnMemoria.PublicadorEnMemoria publicador;
    private ServicioCasos servicio;

    @BeforeEach
    void preparar() {
        repositorio = new DoblesEnMemoria.RepositorioEnMemoria();
        publicador = new DoblesEnMemoria.PublicadorEnMemoria();
        servicio = new ServicioCasos(
                repositorio,
                new DoblesEnMemoria.CorrelativoEnMemoria(),
                new DoblesEnMemoria.DirectorioSedesFijo("RM"),
                publicador,
                new DoblesEnMemoria.ClienteRequerimientosFijo(),
                new DoblesEnMemoria.RelojFijo(AHORA),
                new DoblesEnMemoria.GeneradorSecuencial(),
                new MaquinaEstadosCaso(),
                new GeneradorNumeroExpediente(),
                new EvaluadorAlcance(),
                Set.of("COORDINADOR"),
                LocalDate.of(2027, 1, 1));
    }

    private EventoRequerimientoAsignado evento(String origenAlt, Boolean esBeta) {
        return new EventoRequerimientoAsignado(origenAlt, AHORA, "F-100", SEDE, UNIDAD,
                Complejidad.MEDIANA, false, esBeta, "prof-9");
    }

    private ContextoTerritorial ctxNacional(String... roles) {
        return new ContextoTerritorial("user-1", Set.of(roles), SEDE, UNIDAD, Alcance.NACIONAL, "Ana");
    }

    @Test
    void materializaCasoAbiertoConAsientoYEvento() {
        Caso caso = servicio.materializar(evento("req-1", null));

        assertThat(caso.estado()).isEqualTo(EstadoCaso.ABIERTO);
        assertThat(caso.numeroExpediente().valor()).isEqualTo("EXP-RM-1/2027");
        assertThat(caso.historialCompleto()).hasSize(1);
        assertThat(caso.historialCompleto().get(0).accion()).isEqualTo("MATERIALIZACION");
        assertThat(repositorio.cantidad()).isEqualTo(1);
        assertThat(publicador.tipos()).containsExactly("caso.abierto");
    }

    @Test
    void materializacionEsIdempotente() {
        Caso primero = servicio.materializar(evento("req-1", null));
        Caso segundo = servicio.materializar(evento("req-1", null)); // reentrega

        assertThat(segundo.altKey()).isEqualTo(primero.altKey());
        assertThat(repositorio.cantidad()).isEqualTo(1);
        // El no-op NO vuelve a publicar caso.abierto.
        assertThat(publicador.tipos()).containsExactly("caso.abierto");
    }

    @Test
    void eventoBetaGeneraExpedienteEnSerieBeta() {
        Caso caso = servicio.materializar(evento("req-beta", Boolean.TRUE));
        assertThat(caso.numeroExpediente().valor()).isEqualTo("EXP-RM-B1/2027");
        assertThat(caso.esBeta()).isTrue();
    }

    @Test
    void transicionValidaActualizaEstadoYEmiteEvento() {
        Caso caso = servicio.materializar(evento("req-1", null));
        Caso actualizado = servicio.transicionar(
                new ComandoTransicion(caso.altKey(), AccionCaso.INICIAR_INVESTIGACION, "Inicio"),
                ctxNacional("PROFESIONAL"));

        assertThat(actualizado.estado()).isEqualTo(EstadoCaso.EN_INVESTIGACION);
        assertThat(publicador.tipos()).containsExactly("caso.abierto", "caso.estado_cambiado");
    }

    @Test
    void transicionFueraDeAlcanceResponde404() {
        Caso caso = servicio.materializar(evento("req-1", null));
        ContextoTerritorial otraUnidad = new ContextoTerritorial(
                "user-2", Set.of("PROFESIONAL"), SEDE, "unidad-distinta", Alcance.UNIDAD, "Beto");

        assertThatThrownBy(() -> servicio.transicionar(
                new ComandoTransicion(caso.altKey(), AccionCaso.INICIAR_INVESTIGACION, null), otraUnidad))
                .isInstanceOf(CasoNoEncontradoException.class);
    }

    @Test
    void accionAdministrativaSinRolResponde403() {
        Caso caso = servicio.materializar(evento("req-1", null));

        assertThatThrownBy(() -> servicio.transicionar(
                new ComandoTransicion(caso.altKey(), AccionCaso.CERRAR, "cierre"),
                ctxNacional("PROFESIONAL")))
                .isInstanceOf(AccesoDenegadoException.class);
    }

    @Test
    void accionAdministrativaConRolCierraElCaso() {
        Caso caso = servicio.materializar(evento("req-1", null));
        Caso cerrado = servicio.transicionar(
                new ComandoTransicion(caso.altKey(), AccionCaso.CERRAR, "cierre"),
                ctxNacional("COORDINADOR"));

        assertThat(cerrado.estado()).isEqualTo(EstadoCaso.CERRADO);
        assertThat(cerrado.cerradoEn()).isNotNull();
        assertThat(publicador.tipos()).containsExactly("caso.abierto", "caso.cerrado");
    }

    @Test
    void transicionInvalidaResponde409() {
        Caso caso = servicio.materializar(evento("req-1", null));

        assertThatThrownBy(() -> servicio.transicionar(
                new ComandoTransicion(caso.altKey(), AccionCaso.DERIVAR_A_SEGUIMIENTO, null),
                ctxNacional("PROFESIONAL")))
                .isInstanceOf(TransicionInvalidaException.class);
    }
}
