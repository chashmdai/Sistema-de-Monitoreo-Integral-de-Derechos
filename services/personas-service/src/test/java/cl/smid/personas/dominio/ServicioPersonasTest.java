package cl.smid.personas.dominio;

import cl.smid.personas.dominio.excepcion.PersonaNoEncontradaException;
import cl.smid.personas.dominio.excepcion.RutDuplicadoException;
import cl.smid.personas.dominio.excepcion.RutInvalidoException;
import cl.smid.personas.dominio.modelo.Alcance;
import cl.smid.personas.dominio.modelo.ContextoTerritorial;
import cl.smid.personas.dominio.modelo.DatosPersona;
import cl.smid.personas.dominio.modelo.EventoDominio;
import cl.smid.personas.dominio.modelo.Persona;
import cl.smid.personas.dominio.modelo.TipoPersona;
import cl.smid.personas.dominio.servicio.BuscadorDuplicadosBaseLocal;
import cl.smid.personas.dominio.servicio.ServicioPersonas;
import cl.smid.personas.soporte.EventoPublicadorEnMemoria;
import cl.smid.personas.soporte.GeneradorAltKeySecuencial;
import cl.smid.personas.soporte.PersonaRepositorioEnMemoria;
import cl.smid.personas.soporte.RelojFijo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Prueba el caso de uso de gestión de personas con dobles en memoria, cubriendo los criterios de
 * aceptación del Núcleo (§5.5): alta parcial sin RUT, unicidad de RUT, validación de RUT, filtro
 * territorial y emisión de eventos con metadatos no sensibles.
 */
class ServicioPersonasTest {

    private static final Instant T0 = Instant.parse("2026-01-15T12:00:00Z");

    private PersonaRepositorioEnMemoria repositorio;
    private EventoPublicadorEnMemoria publicador;
    private RelojFijo reloj;
    private ServicioPersonas servicio;

    private final ContextoTerritorial ctxSedeA =
            new ContextoTerritorial("func-1", Alcance.SEDE, "sede-a", "unidad-1");

    @BeforeEach
    void preparar() {
        repositorio = new PersonaRepositorioEnMemoria();
        publicador = new EventoPublicadorEnMemoria();
        reloj = new RelojFijo(T0);
        servicio = new ServicioPersonas(
                repositorio,
                new BuscadorDuplicadosBaseLocal(repositorio),
                publicador,
                reloj,
                new GeneradorAltKeySecuencial());
    }

    @Test
    @DisplayName("Crea un NNA sólo con nombre, sin RUT, y estampa el contexto")
    void creaNnaSinRut() {
        DatosPersona datos = new DatosPersona(
                TipoPersona.NNA, null, "Pedro", "Soto", "Vega", null, null, null, null, null);

        Persona creada = servicio.crear(datos, ctxSedeA);

        assertThat(creada.altKey()).isNotBlank();
        assertThat(creada.tipo()).isEqualTo(TipoPersona.NNA);
        assertThat(creada.rut()).isNull();
        assertThat(creada.idSede()).isEqualTo("sede-a");
        assertThat(creada.idUnidad()).isEqualTo("unidad-1");
        assertThat(creada.creadoPor()).isEqualTo("func-1");
        assertThat(creada.vigente()).isTrue();
        assertThat(creada.creadoEn()).isEqualTo(T0);
        assertThat(creada.actualizadoEn()).isEqualTo(T0);

        // Se publicó 'persona.creada' con metadatos NO sensibles (sólo tipo y conRut).
        EventoDominio evento = publicador.ultimo();
        assertThat(evento.nombre()).isEqualTo("persona.creada");
        assertThat(evento.recurso()).isEqualTo(creada.altKey());
        assertThat(evento.datos()).containsOnlyKeys("tipo", "conRut");
        assertThat(evento.datos()).containsEntry("tipo", "NNA");
        assertThat(evento.datos()).containsEntry("conRut", false);
    }

    @Test
    @DisplayName("Un RUT inválido (módulo 11) impide el alta")
    void rechazaRutInvalido() {
        DatosPersona datos = new DatosPersona(
                TipoPersona.ADULTO, "12345678-9", "Ana", "Soto", null, null, null, null, null, null);

        assertThatThrownBy(() -> servicio.crear(datos, ctxSedeA))
                .isInstanceOf(RutInvalidoException.class);
    }

    @Test
    @DisplayName("Dos personas no pueden compartir el mismo RUT vigente")
    void rechazaRutDuplicado() {
        DatosPersona primera = new DatosPersona(
                TipoPersona.ADULTO, "12345678-5", "Ana", "Soto", null, null, null, null, null, null);
        servicio.crear(primera, ctxSedeA);

        // Mismo RUT en otro formato de captura → mismo canónico → conflicto.
        DatosPersona segunda = new DatosPersona(
                TipoPersona.ADULTO, "12.345.678-5", "Otra", "Persona", null, null, null, null, null, null);

        assertThatThrownBy(() -> servicio.crear(segunda, ctxSedeA))
                .isInstanceOf(RutDuplicadoException.class);
    }

    @Test
    @DisplayName("Un registro de otra sede es invisible (404) para un usuario con alcance SEDE")
    void filtroTerritorialOcultaOtraSede() {
        Persona creada = servicio.crear(
                new DatosPersona(TipoPersona.NNA, null, "Pedro", "Soto", null, null, null, null, null, null),
                ctxSedeA);

        ContextoTerritorial ctxSedeB =
                new ContextoTerritorial("func-2", Alcance.SEDE, "sede-b", "unidad-9");

        assertThatThrownBy(() -> servicio.obtener(creada.altKey(), ctxSedeB))
                .isInstanceOf(PersonaNoEncontradaException.class);
    }

    @Test
    @DisplayName("La actualización es un merge parcial: sólo cambian los campos informados")
    void actualizacionParcial() {
        Persona creada = servicio.crear(
                new DatosPersona(TipoPersona.NNA, null, "Pedro", "Soto", "Vega", null, null, null, null, null),
                ctxSedeA);

        // Avanza el reloj para verificar que actualizado_en cambia.
        reloj.fijar(T0.plusSeconds(3600));

        // Sólo se informa 'nombres'; los apellidos deben conservarse.
        DatosPersona cambios = new DatosPersona(
                null, null, "Pedrito", null, null, null, null, null, null, null);

        Persona actualizada = servicio.actualizar(creada.altKey(), cambios, ctxSedeA);

        assertThat(actualizada.nombres()).isEqualTo("Pedrito");
        assertThat(actualizada.apellidoPaterno()).isEqualTo("Soto");
        assertThat(actualizada.apellidoMaterno()).isEqualTo("Vega");
        assertThat(actualizada.actualizadoEn()).isEqualTo(T0.plusSeconds(3600));
        assertThat(actualizada.creadoEn()).isEqualTo(T0);

        // El último evento es 'persona.actualizada'.
        List<EventoDominio> eventos = publicador.eventos();
        assertThat(eventos).extracting(EventoDominio::nombre)
                .containsExactly("persona.creada", "persona.actualizada");
    }
}
