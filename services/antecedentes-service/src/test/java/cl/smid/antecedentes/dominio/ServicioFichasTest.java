package cl.smid.antecedentes.dominio;

import cl.smid.antecedentes.dominio.excepcion.AccesoDenegadoException;
import cl.smid.antecedentes.dominio.excepcion.RecursoNoEncontradoException;
import cl.smid.antecedentes.dominio.excepcion.ReglaNegocioException;
import cl.smid.antecedentes.dominio.modelo.AccionRevision;
import cl.smid.antecedentes.dominio.modelo.Alcance;
import cl.smid.antecedentes.dominio.modelo.Calificacion;
import cl.smid.antecedentes.dominio.modelo.ContextoSesion;
import cl.smid.antecedentes.dominio.modelo.Criterio;
import cl.smid.antecedentes.dominio.modelo.EstadoFicha;
import cl.smid.antecedentes.dominio.modelo.EstadoHallazgo;
import cl.smid.antecedentes.dominio.modelo.FichaAntecedente;
import cl.smid.antecedentes.dominio.modelo.Hallazgo;
import cl.smid.antecedentes.dominio.modelo.PercepcionHallazgo;
import cl.smid.antecedentes.dominio.modelo.PoliticaRoles;
import cl.smid.antecedentes.dominio.modelo.Temporalidad;
import cl.smid.antecedentes.dominio.puerto.entrada.GestionFichasUseCase;
import cl.smid.antecedentes.dominio.puerto.entrada.GestionFichasUseCase.ComandoAccionRevision;
import cl.smid.antecedentes.dominio.puerto.entrada.GestionFichasUseCase.ComandoCrear;
import cl.smid.antecedentes.dominio.puerto.entrada.GestionFichasUseCase.DatosPropuestaHallazgo;
import cl.smid.antecedentes.dominio.servicio.EvaluadorAlcance;
import cl.smid.antecedentes.dominio.servicio.GeneradorFolio;
import cl.smid.antecedentes.dominio.servicio.MaquinaEstadosFicha;
import cl.smid.antecedentes.dominio.servicio.ServicioFichas;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ServicioFichasTest {

    private static final Instant T2027 = LocalDateTime.of(2027, 5, 1, 10, 0).toInstant(ZoneOffset.UTC);
    private static final String ROL_REVISOR = "JEFATURA_UNIDAD";

    private DoblesEnMemoria.RepositorioFichasFake repoFichas;
    private DoblesEnMemoria.RepositorioHallazgosFake repoHallazgos;
    private DoblesEnMemoria.RepositorioReferenciasFake repoReferencias;
    private DoblesEnMemoria.PublicadorFake publicador;
    private ServicioFichas servicio;

    @BeforeEach
    void preparar() {
        repoFichas = new DoblesEnMemoria.RepositorioFichasFake();
        repoHallazgos = new DoblesEnMemoria.RepositorioHallazgosFake();
        repoReferencias = new DoblesEnMemoria.RepositorioReferenciasFake();
        publicador = new DoblesEnMemoria.PublicadorFake();
        repoReferencias.registrarVigente(cl.smid.antecedentes.dominio.modelo.TipoReferencia.PROCESO, "proc-1");
        repoReferencias.registrarVigente(cl.smid.antecedentes.dominio.modelo.TipoReferencia.CATEGORIA, "cat-1");
        repoReferencias.registrarVigente(cl.smid.antecedentes.dominio.modelo.TipoReferencia.CATEGORIA, "cat-2");
        repoReferencias.registrarVigente(cl.smid.antecedentes.dominio.modelo.TipoReferencia.CATEGORIA, "cat-3");
        repoReferencias.registrarVigente(cl.smid.antecedentes.dominio.modelo.TipoReferencia.INSTRUMENTO, "inst-1");

        DoblesEnMemoria.RelojFijo reloj = new DoblesEnMemoria.RelojFijo(T2027);
        GeneradorFolio generadorFolio = new GeneradorFolio(new DoblesEnMemoria.CorrelativoFake(), reloj);
        PoliticaRoles politica = new PoliticaRoles(Set.of(ROL_REVISOR), Set.of("ADMINISTRADOR"));

        servicio = new ServicioFichas(repoFichas, repoHallazgos, repoReferencias, new MaquinaEstadosFicha(),
                new EvaluadorAlcance(), generadorFolio, reloj, new DoblesEnMemoria.GeneradorIdSecuencial("alt"),
                publicador, politica);
    }

    private ContextoSesion autor() {
        return new ContextoSesion("prof-1", "Autor", Set.of(), "sA", "uA", Alcance.UNIDAD);
    }

    private ContextoSesion revisorNacional() {
        return new ContextoSesion("rev-1", "Revisor", Set.of(ROL_REVISOR), "sA", "uA", Alcance.NACIONAL);
    }

    private ComandoCrear comando(PercepcionHallazgo percepcion, String hallazgoAlt, DatosPropuestaHallazgo prop,
                                 List<String> secundarias, List<Integer> derechos) {
        return new ComandoCrear("proc-1", null, "cat-1", secundarias, derechos, "Descripcion", "Relato sensible",
                Calificacion.BUENA_PRACTICA, Set.of(Criterio.GRAVEDAD), percepcion, null, hallazgoAlt, prop);
    }

    private ComandoCrear comandoSimple() {
        return comando(PercepcionHallazgo.NO_ES_HALLAZGO, null, null, List.of("cat-2"), List.of(1, 2, 3));
    }

    @Test
    void crearGeneraBorradorFolioYEvento() {
        FichaAntecedente ficha = servicio.crear(comandoSimple(), autor());
        assertEquals(EstadoFicha.BORRADOR, ficha.estado());
        assertEquals("FA-1/2027", ficha.folio());
        assertEquals("uA", ficha.unidadAlt());
        assertEquals("prof-1", ficha.profesionalAlt());
        assertTrue(publicador.tipos().contains("ficha.creada"));
    }

    @Test
    void seProponeHallazgoCreaHallazgoYEmiteEventos() {
        DatosPropuestaHallazgo prop = new DatosPropuestaHallazgo("Titulo", "Desc", "inst-1",
                Temporalidad.CORTO_PLAZO, List.of("uA"), List.of());
        FichaAntecedente ficha = servicio.crear(
                comando(PercepcionHallazgo.SE_PROPONE_HALLAZGO, null, prop, List.of(), List.of()), autor());

        assertNotNull(ficha.hallazgoAlt());
        Hallazgo hallazgo = repoHallazgos.almacen.get(ficha.hallazgoAlt());
        assertNotNull(hallazgo);
        assertEquals(EstadoHallazgo.PROPUESTO, hallazgo.estado());
        assertEquals(ficha.altKey(), hallazgo.origenFichaAlt());
        assertTrue(publicador.tipos().containsAll(
                List.of("ficha.creada", "ficha.propuesta_hallazgo", "hallazgo.creado")));
    }

    @Test
    void noEsHallazgoConVinculoFalla() {
        assertThrows(ReglaNegocioException.class, () -> servicio.crear(
                comando(PercepcionHallazgo.NO_ES_HALLAZGO, "hz-x", null, List.of(), List.of()), autor()));
    }

    @Test
    void antecedenteDeHallazgoInexistenteFalla() {
        assertThrows(ReglaNegocioException.class, () -> servicio.crear(
                comando(PercepcionHallazgo.ANTECEDENTE_DE_HALLAZGO, "hz-inexistente", null, List.of(), List.of()),
                autor()));
    }

    @Test
    void maximoDosCategoriasSecundarias() {
        assertThrows(ReglaNegocioException.class, () -> servicio.crear(
                comando(PercepcionHallazgo.NO_ES_HALLAZGO, null, null, List.of("cat-2", "cat-3", "cat-1"),
                        List.of(1)), autor()));
    }

    @Test
    void articuloCdnFueraDeRangoFalla() {
        assertThrows(ReglaNegocioException.class, () -> servicio.crear(
                comando(PercepcionHallazgo.NO_ES_HALLAZGO, null, null, List.of(), List.of(55)), autor()));
    }

    @Test
    void articuloCdnDuplicadoFalla() {
        assertThrows(ReglaNegocioException.class, () -> servicio.crear(
                comando(PercepcionHallazgo.NO_ES_HALLAZGO, null, null, List.of(), List.of(3, 3)), autor()));
    }

    @Test
    void referenciaNoVigenteFalla() {
        ComandoCrear conProcesoMalo = new ComandoCrear("proc-inexistente", null, "cat-1", List.of(), List.of(),
                "d", "r", Calificacion.NUDO_CRITICO, Set.of(), PercepcionHallazgo.NO_ES_HALLAZGO, null, null, null);
        assertThrows(ReglaNegocioException.class, () -> servicio.crear(conProcesoMalo, autor()));
    }

    @Test
    void obtenerFueraDeAlcanceDevuelve404() {
        FichaAntecedente ficha = servicio.crear(comandoSimple(), autor());
        ContextoSesion otraUnidad = new ContextoSesion("x", "X", Set.of(), "sB", "uB", Alcance.UNIDAD);
        assertThrows(RecursoNoEncontradoException.class, () -> servicio.obtener(ficha.altKey(), otraUnidad));
    }

    @Test
    void edicionSoloEnBorrador() {
        FichaAntecedente ficha = servicio.crear(comandoSimple(), autor());
        servicio.transicionar(ficha.altKey(), new ComandoAccionRevision(AccionRevision.ENVIAR_REVISION, null), autor());
        GestionFichasUseCase.ComandoEditar editar = new GestionFichasUseCase.ComandoEditar("proc-1", null, "cat-1",
                List.of(), List.of(1), "d2", "r2", Calificacion.NUDO_CRITICO, Set.of(),
                PercepcionHallazgo.NO_ES_HALLAZGO, null, null, null);
        assertThrows(ReglaNegocioException.class, () -> servicio.editar(ficha.altKey(), editar, autor()));
    }

    @Test
    void eliminarSoloEnBorrador() {
        FichaAntecedente ficha = servicio.crear(comandoSimple(), autor());
        servicio.transicionar(ficha.altKey(), new ComandoAccionRevision(AccionRevision.ENVIAR_REVISION, null), autor());
        assertThrows(ReglaNegocioException.class, () -> servicio.eliminar(ficha.altKey(), autor()));
    }

    @Test
    void aprobarSinRolRevisorEs403() {
        FichaAntecedente ficha = servicio.crear(comandoSimple(), autor());
        servicio.transicionar(ficha.altKey(), new ComandoAccionRevision(AccionRevision.ENVIAR_REVISION, null), autor());
        // El autor no tiene rol revisor.
        assertThrows(AccesoDenegadoException.class, () -> servicio.transicionar(ficha.altKey(),
                new ComandoAccionRevision(AccionRevision.APROBAR, null), autor()));
    }

    @Test
    void flujoCompletoHastaAprobada() {
        FichaAntecedente ficha = servicio.crear(comandoSimple(), autor());
        servicio.transicionar(ficha.altKey(), new ComandoAccionRevision(AccionRevision.ENVIAR_REVISION, null), autor());
        FichaAntecedente aprobada = servicio.transicionar(ficha.altKey(),
                new ComandoAccionRevision(AccionRevision.APROBAR, "Ok"), revisorNacional());
        assertEquals(EstadoFicha.APROBADA, aprobada.estado());
        assertTrue(publicador.tipos().contains("ficha.aprobada"));
    }

    @Test
    void aprobarUnBorradorEsTransicionInvalida() {
        FichaAntecedente ficha = servicio.crear(comandoSimple(), autor());
        assertThrows(ReglaNegocioException.class, () -> servicio.transicionar(ficha.altKey(),
                new ComandoAccionRevision(AccionRevision.APROBAR, null), revisorNacional()));
    }
}
