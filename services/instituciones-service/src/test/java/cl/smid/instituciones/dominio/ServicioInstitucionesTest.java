package cl.smid.instituciones.dominio;

import cl.smid.instituciones.dominio.excepcion.ConflictoException;
import cl.smid.instituciones.dominio.excepcion.ExcepcionValidacion;
import cl.smid.instituciones.dominio.excepcion.NoAutorizadoException;
import cl.smid.instituciones.dominio.excepcion.RecursoNoEncontradoException;
import cl.smid.instituciones.dominio.excepcion.ReglaNegocioException;
import cl.smid.instituciones.dominio.modelo.ContextoSesion;
import cl.smid.instituciones.dominio.modelo.DetalleInstitucion;
import cl.smid.instituciones.dominio.modelo.PuntoFocal;
import cl.smid.instituciones.dominio.modelo.TipoInstitucion;
import cl.smid.instituciones.dominio.puerto.entrada.comando.DatosInstitucion;
import cl.smid.instituciones.dominio.puerto.entrada.comando.DatosPuntoFocal;
import cl.smid.instituciones.dominio.puerto.entrada.comando.DatosTipo;
import cl.smid.instituciones.dominio.servicio.ServicioInstituciones;
import cl.smid.instituciones.dominio.soporte.GeneradorSecuencial;
import cl.smid.instituciones.dominio.soporte.PublicadorEventosCaptura;
import cl.smid.instituciones.dominio.soporte.RelojFijo;
import cl.smid.instituciones.dominio.soporte.RepositorioInstitucionesMemoria;
import cl.smid.instituciones.dominio.soporte.RepositorioPuntosFocalesMemoria;
import cl.smid.instituciones.dominio.soporte.RepositorioTiposMemoria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Pruebas del orquestador {@link ServicioInstituciones}: autorización de escritura por
 * rol, reglas de tipo vigente, unicidad, baja lógica e invariante de un único punto
 * focal principal activo por institución.
 */
class ServicioInstitucionesTest {

    private RepositorioTiposMemoria tipos;
    private RepositorioInstitucionesMemoria instituciones;
    private RepositorioPuntosFocalesMemoria puntosFocales;
    private PublicadorEventosCaptura eventos;
    private ServicioInstituciones servicio;

    private static final ContextoSesion ADMIN = new ContextoSesion(
            "u-admin", Set.of("ADMIN_INSTITUCIONES"), "sede-1", "unidad-1", "NACIONAL", "Admin");
    private static final ContextoSesion SIN_ROL = new ContextoSesion(
            "u-lector", Set.of("PROFESIONAL_UPRJ"), "sede-1", "unidad-1", "UNIDAD", "Lector");

    @BeforeEach
    void preparar() {
        tipos = new RepositorioTiposMemoria();
        instituciones = new RepositorioInstitucionesMemoria(tipos);
        puntosFocales = new RepositorioPuntosFocalesMemoria();
        eventos = new PublicadorEventosCaptura();
        servicio = new ServicioInstituciones(
                tipos, instituciones, puntosFocales, eventos,
                new RelojFijo(Instant.parse("2027-01-01T00:00:00Z")),
                new GeneradorSecuencial(),
                Set.of("ADMIN_INSTITUCIONES", "COORDINACION_NACIONAL"));
    }

    private TipoInstitucion crearTipoBase() {
        return servicio.crearTipo(ADMIN, new DatosTipo("Tribunal de Familia", "JUDICIAL", null));
    }

    // ---------- Autorización ----------

    @Test
    void rechazaCrearTipoSinRolAdmin() {
        assertThatThrownBy(() -> servicio.crearTipo(SIN_ROL, new DatosTipo("X", "SALUD", null)))
                .isInstanceOf(NoAutorizadoException.class);
    }

    @Test
    void rechazaCrearTipoSinContexto() {
        assertThatThrownBy(() -> servicio.crearTipo(null, new DatosTipo("X", "SALUD", null)))
                .isInstanceOf(NoAutorizadoException.class);
    }

    @Test
    void lecturaDeTiposNoRequiereContexto() {
        crearTipoBase();
        assertThat(servicio.listarTipos(null, cl.smid.instituciones.dominio.modelo.Paginado.de(0, 20))
                .totalElementos()).isEqualTo(1);
    }

    // ---------- Tipos ----------

    @Test
    void creaTipoYEmiteEvento() {
        TipoInstitucion tipo = crearTipoBase();
        assertThat(tipo.altKey()).isEqualTo("alt-1");
        assertThat(tipo.vigente()).isTrue();
        assertThat(eventos.contar("tipo.creado")).isEqualTo(1);
    }

    @Test
    void rechazaNombreDuplicadoDeTipo() {
        crearTipoBase();
        assertThatThrownBy(() -> servicio.crearTipo(ADMIN,
                new DatosTipo("tribunal de familia", "JUDICIAL", null)))
                .isInstanceOf(ConflictoException.class);
    }

    @Test
    void rechazaAmbitoInvalido() {
        assertThatThrownBy(() -> servicio.crearTipo(ADMIN, new DatosTipo("Otro", "INEXISTENTE", null)))
                .isInstanceOf(ExcepcionValidacion.class);
    }

    @Test
    void rechazaNombreDeTipoEnBlanco() {
        assertThatThrownBy(() -> servicio.crearTipo(ADMIN, new DatosTipo("   ", "SALUD", null)))
                .isInstanceOf(ExcepcionValidacion.class);
    }

    // ---------- Instituciones ----------

    @Test
    void creaInstitucionConTipoVigente() {
        TipoInstitucion tipo = crearTipoBase();
        DetalleInstitucion detalle = servicio.crearInstitucion(ADMIN, new DatosInstitucion(
                "T-001", "1º Juzgado de Familia", tipo.altKey(), "12.345.678-5",
                "13", "13101", "Av. Siempre Viva 100", "+56222222222", "contacto@jf.cl", "https://jf.cl"));
        assertThat(detalle.institucion().altKey()).isEqualTo("alt-2");
        assertThat(detalle.tipoNombre()).isEqualTo("Tribunal de Familia");
        assertThat(detalle.institucion().rut().canonico()).isEqualTo("12345678-5");
        assertThat(eventos.contar("institucion.creada")).isEqualTo(1);
    }

    @Test
    void rechazaInstitucionConTipoInexistente() {
        assertThatThrownBy(() -> servicio.crearInstitucion(ADMIN, new DatosInstitucion(
                null, "Sin tipo", "tipo-fantasma", null, null, null, null, null, null, null)))
                .isInstanceOf(ReglaNegocioException.class);
    }

    @Test
    void rechazaInstitucionConTipoNoVigente() {
        TipoInstitucion tipo = crearTipoBase();
        servicio.cambiarVigenciaTipo(ADMIN, tipo.altKey(), false);
        assertThatThrownBy(() -> servicio.crearInstitucion(ADMIN, new DatosInstitucion(
                null, "Inst", tipo.altKey(), null, null, null, null, null, null, null)))
                .isInstanceOf(ReglaNegocioException.class);
    }

    @Test
    void rechazaRutInvalidoAlCrearInstitucion() {
        TipoInstitucion tipo = crearTipoBase();
        assertThatThrownBy(() -> servicio.crearInstitucion(ADMIN, new DatosInstitucion(
                null, "Inst", tipo.altKey(), "12345678-9", null, null, null, null, null, null)))
                .isInstanceOf(ReglaNegocioException.class);
    }

    @Test
    void rechazaCodigoDuplicado() {
        TipoInstitucion tipo = crearTipoBase();
        servicio.crearInstitucion(ADMIN, new DatosInstitucion(
                "DUP", "A", tipo.altKey(), null, null, null, null, null, null, null));
        assertThatThrownBy(() -> servicio.crearInstitucion(ADMIN, new DatosInstitucion(
                "DUP", "B", tipo.altKey(), null, null, null, null, null, null, null)))
                .isInstanceOf(ConflictoException.class);
    }

    @Test
    void bajaLogicaDeInstitucion() {
        TipoInstitucion tipo = crearTipoBase();
        DetalleInstitucion detalle = servicio.crearInstitucion(ADMIN, new DatosInstitucion(
                null, "A", tipo.altKey(), null, null, null, null, null, null, null));
        DetalleInstitucion baja = servicio.cambiarActivacionInstitucion(
                ADMIN, detalle.institucion().altKey(), false);
        assertThat(baja.institucion().activa()).isFalse();
        assertThat(eventos.contar("institucion.activacion")).isEqualTo(1);
    }

    @Test
    void obtenerInstitucionInexistenteLanza404() {
        assertThatThrownBy(() -> servicio.obtenerInstitucion("no-existe"))
                .isInstanceOf(RecursoNoEncontradoException.class);
    }

    // ---------- Puntos focales: invariante de principal ----------

    @Test
    void soloUnPuntoFocalPrincipalActivoPorInstitucion() {
        TipoInstitucion tipo = crearTipoBase();
        String inst = servicio.crearInstitucion(ADMIN, new DatosInstitucion(
                null, "A", tipo.altKey(), null, null, null, null, null, null, null))
                .institucion().altKey();

        PuntoFocal primero = servicio.crearPuntoFocal(ADMIN, inst,
                new DatosPuntoFocal("Ana", "Jefa", "ana@a.cl", null, true));
        PuntoFocal segundo = servicio.crearPuntoFocal(ADMIN, inst,
                new DatosPuntoFocal("Beto", "Sub", "beto@a.cl", null, true));

        // Tras designar a 'Beto' como principal, 'Ana' deja de serlo.
        assertThat(servicio.obtenerInstitucion(inst).puntosFocales())
                .filteredOn(PuntoFocal::principal)
                .extracting(PuntoFocal::altKey)
                .containsExactly(segundo.altKey());
        assertThat(primero.altKey()).isNotEqualTo(segundo.altKey());
        assertThat(eventos.contar("puntofocal.creado")).isEqualTo(2);
    }

    @Test
    void rechazaCrearPuntoFocalEnInstitucionInexistente() {
        assertThatThrownBy(() -> servicio.crearPuntoFocal(ADMIN, "no-existe",
                new DatosPuntoFocal("Ana", null, null, null, false)))
                .isInstanceOf(RecursoNoEncontradoException.class);
    }

    @Test
    void rechazaPuntoFocalSinRolAdmin() {
        TipoInstitucion tipo = crearTipoBase();
        String inst = servicio.crearInstitucion(ADMIN, new DatosInstitucion(
                null, "A", tipo.altKey(), null, null, null, null, null, null, null))
                .institucion().altKey();
        assertThatThrownBy(() -> servicio.crearPuntoFocal(SIN_ROL, inst,
                new DatosPuntoFocal("Ana", null, null, null, false)))
                .isInstanceOf(NoAutorizadoException.class);
    }
}
