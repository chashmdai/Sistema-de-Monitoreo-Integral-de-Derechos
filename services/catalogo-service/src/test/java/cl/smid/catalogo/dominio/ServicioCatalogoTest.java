package cl.smid.catalogo.dominio;

import cl.smid.catalogo.dominio.excepcion.CatalogoException;
import cl.smid.catalogo.dominio.excepcion.CodigoError;
import cl.smid.catalogo.dominio.modelo.Causa;
import cl.smid.catalogo.dominio.modelo.Derecho;
import cl.smid.catalogo.dominio.modelo.NodoArbol;
import cl.smid.catalogo.dominio.puerto.entrada.ActorEvento;
import cl.smid.catalogo.dominio.puerto.entrada.ActualizarDerechoCmd;
import cl.smid.catalogo.dominio.puerto.entrada.CrearCausaCmd;
import cl.smid.catalogo.dominio.puerto.entrada.CrearDerechoCmd;
import cl.smid.catalogo.dominio.puerto.salida.EventoDominio;
import cl.smid.catalogo.dominio.servicio.EnsambladorArbol;
import cl.smid.catalogo.dominio.servicio.ServicioCatalogo;
import cl.smid.catalogo.soporte.DoblesDeterministas.GeneradorAltKeySecuencial;
import cl.smid.catalogo.soporte.DoblesDeterministas.RelojFijo;
import cl.smid.catalogo.soporte.PublicadorEventosEnMemoria;
import cl.smid.catalogo.soporte.RepositorioCausaEnMemoria;
import cl.smid.catalogo.soporte.RepositorioDerechoEnMemoria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Pruebas de la lógica de negocio de {@link ServicioCatalogo} con dobles en memoria.
 */
class ServicioCatalogoTest {

    private static final ActorEvento ACTOR = new ActorEvento("usuario-admin", "sede-1", "unidad-1");

    private RepositorioDerechoEnMemoria derechos;
    private RepositorioCausaEnMemoria causas;
    private PublicadorEventosEnMemoria eventos;
    private ServicioCatalogo servicio;

    @BeforeEach
    void preparar() {
        derechos = new RepositorioDerechoEnMemoria();
        causas = new RepositorioCausaEnMemoria();
        eventos = new PublicadorEventosEnMemoria();
        servicio = new ServicioCatalogo(
                derechos, causas,
                new RelojFijo(LocalDate.of(2026, 6, 13)),
                eventos,
                new EnsambladorArbol(),
                new GeneradorAltKeySecuencial());
    }

    private Derecho crearRaiz(String codigo, String nombre) {
        return servicio.crearDerecho(new CrearDerechoCmd(null, codigo, nombre, null, (short) 1), ACTOR);
    }

    private Derecho crearHijo(Derecho padre, String codigo, String nombre) {
        return servicio.crearDerecho(
                new CrearDerechoCmd(padre.getAltKey(), codigo, nombre, null, (short) 1), ACTOR);
    }

    @Test
    @DisplayName("Crear raíz: nivel 0, vigente, con evento y alt_key generado")
    void crearRaiz() {
        Derecho raiz = crearRaiz("EDU", "Educación");

        assertThat(raiz.getNivel()).isZero();
        assertThat(raiz.esRaiz()).isTrue();
        assertThat(raiz.estaVigente()).isTrue();
        assertThat(raiz.getAltKey()).isEqualTo("ak-1");
        assertThat(eventos.ultimoConNombre("catalogo.derecho.creado")).isPresent();
    }

    @Test
    @DisplayName("Crear hijo: el nivel se deriva del padre y la FK apunta al padre")
    void crearHijoDerivaNivel() {
        Derecho raiz = crearRaiz("EDU", "Educación");
        Derecho hijo = crearHijo(raiz, "EDU.ACCESO", "Acceso");

        assertThat(hijo.getNivel()).isEqualTo((short) 1);
        assertThat(hijo.getIdPadre()).isEqualTo(raiz.getId());
    }

    @Test
    @DisplayName("Código de derecho duplicado lanza CAT-002")
    void codigoDuplicado() {
        crearRaiz("EDU", "Educación");

        CatalogoException ex = assertThrows(CatalogoException.class,
                () -> crearRaiz("EDU", "Otra"));

        assertThat(ex.getCodigoError()).isEqualTo(CodigoError.CODIGO_DERECHO_DUPLICADO);
    }

    @Test
    @DisplayName("Crear bajo un padre inexistente lanza CAT-004")
    void padreInexistente() {
        CatalogoException ex = assertThrows(CatalogoException.class,
                () -> servicio.crearDerecho(
                        new CrearDerechoCmd("no-existe", "X", "X", null, (short) 1), ACTOR));

        assertThat(ex.getCodigoError()).isEqualTo(CodigoError.ARBOL_INVALIDO);
    }

    @Test
    @DisplayName("Baja en cascada: marca descendientes y publica descendientesAfectados")
    void bajaEnCascada() {
        Derecho raiz = crearRaiz("EDU", "Educación");
        Derecho hijo = crearHijo(raiz, "EDU.ACCESO", "Acceso");
        Derecho nieto = crearHijo(hijo, "EDU.ACCESO.RURAL", "Rural");
        eventos.limpiar();

        servicio.darDeBajaDerecho(raiz.getAltKey(), ACTOR);

        assertThat(derechos.buscarPorAltKey(raiz.getAltKey()).orElseThrow().estaVigente()).isFalse();
        assertThat(derechos.buscarPorAltKey(hijo.getAltKey()).orElseThrow().estaVigente()).isFalse();
        assertThat(derechos.buscarPorAltKey(nieto.getAltKey()).orElseThrow().estaVigente()).isFalse();

        EventoDominio ev = eventos.ultimoConNombre("catalogo.derecho.baja").orElseThrow();
        assertThat(ev.recurso()).isEqualTo(raiz.getAltKey());
        assertThat(ev.datos()).containsEntry("descendientesAfectados", 2);
    }

    @Test
    @DisplayName("La baja es idempotente: repetir no cambia estado ni publica de nuevo")
    void bajaIdempotente() {
        Derecho raiz = crearRaiz("EDU", "Educación");
        servicio.darDeBajaDerecho(raiz.getAltKey(), ACTOR);
        eventos.limpiar();

        servicio.darDeBajaDerecho(raiz.getAltKey(), ACTOR); // segunda vez

        assertThat(eventos.ultimoConNombre("catalogo.derecho.baja")).isEmpty();
    }

    @Test
    @DisplayName("El código es inmutable: cambiarlo lanza CAT-006; mantenerlo permite renombrar")
    void codigoInmutable() {
        Derecho d = crearRaiz("EDU", "Educación");

        CatalogoException ex = assertThrows(CatalogoException.class,
                () -> servicio.actualizarDerecho(d.getAltKey(),
                        new ActualizarDerechoCmd("OTRO", "Educación", null, null, null), ACTOR));
        assertThat(ex.getCodigoError()).isEqualTo(CodigoError.CODIGO_INMUTABLE);

        // Con el mismo código (o sin enviarlo), el renombrado funciona.
        Derecho actualizado = servicio.actualizarDerecho(d.getAltKey(),
                new ActualizarDerechoCmd("EDU", "Educación integral", null, null, null), ACTOR);
        assertThat(actualizado.getNombre()).isEqualTo("Educación integral");
    }

    @Test
    @DisplayName("Reubicar bajo un descendiente propio lanza CAT-004 (ciclo)")
    void reubicarCicloProhibido() {
        Derecho raiz = crearRaiz("A", "A");
        Derecho hijo = crearHijo(raiz, "A.1", "A.1");

        CatalogoException ex = assertThrows(CatalogoException.class,
                () -> servicio.actualizarDerecho(raiz.getAltKey(),
                        new ActualizarDerechoCmd(null, "A", null, null, hijo.getAltKey()), ACTOR));

        assertThat(ex.getCodigoError()).isEqualTo(CodigoError.ARBOL_INVALIDO);
    }

    @Test
    @DisplayName("Reubicar recalcula el nivel del subárbol con el delta correspondiente")
    void reubicarRecalculaNivel() {
        Derecho a = crearRaiz("A", "A");
        Derecho b = crearRaiz("B", "B");
        Derecho b1 = crearHijo(b, "B.1", "B.1");      // nivel 1
        Derecho a1 = crearHijo(a, "A.1", "A.1");      // nivel 1
        Derecho a11 = crearHijo(a1, "A.1.1", "A.1.1"); // nivel 2

        // Mover A.1 (con su hijo) bajo B.1 ⇒ A.1 pasa a nivel 2 y A.1.1 a nivel 3.
        servicio.actualizarDerecho(a1.getAltKey(),
                new ActualizarDerechoCmd(null, "A.1", null, null, b1.getAltKey()), ACTOR);

        assertThat(derechos.buscarPorAltKey(a1.getAltKey()).orElseThrow().getNivel()).isEqualTo((short) 2);
        assertThat(derechos.buscarPorAltKey(a11.getAltKey()).orElseThrow().getNivel()).isEqualTo((short) 3);
        assertThat(derechos.buscarPorAltKey(a1.getAltKey()).orElseThrow().getIdPadre()).isEqualTo(b1.getId());
    }

    @Test
    @DisplayName("Búsqueda: acento- e insensible a mayúsculas; texto en blanco devuelve vacío")
    void busqueda() {
        crearRaiz("EDU", "Educación");

        assertThat(servicio.buscar("educacion")).hasSize(1);
        assertThat(servicio.buscar("EDUCACIÓN")).hasSize(1);
        assertThat(servicio.buscar("   ")).isEmpty();
        assertThat(servicio.buscar(null)).isEmpty();
    }

    @Test
    @DisplayName("Detalle incluye hijos directos vigentes; derecho inexistente lanza CAT-001")
    void detalle() {
        Derecho raiz = crearRaiz("EDU", "Educación");
        crearHijo(raiz, "EDU.ACCESO", "Acceso");
        crearHijo(raiz, "EDU.CALIDAD", "Calidad");

        NodoArbol detalle = servicio.obtenerDetalle(raiz.getAltKey());
        assertThat(detalle.getHijos()).extracting(n -> n.getDerecho().getCodigo())
                .containsExactly("EDU.ACCESO", "EDU.CALIDAD");

        CatalogoException ex = assertThrows(CatalogoException.class,
                () -> servicio.obtenerDetalle("no-existe"));
        assertThat(ex.getCodigoError()).isEqualTo(CodigoError.DERECHO_NO_ENCONTRADO);
    }

    @Test
    @DisplayName("Crear causa: éxito y duplicado por derecho lanza CAT-003")
    void crearCausa() {
        Derecho d = crearRaiz("EDU", "Educación");

        Causa causa = servicio.crearCausa(d.getAltKey(), new CrearCausaCmd("C1", "Causa 1"), ACTOR);
        assertThat(causa.getAltKey()).isNotBlank();
        assertThat(servicio.obtenerCausas(d.getAltKey())).hasSize(1);

        CatalogoException ex = assertThrows(CatalogoException.class,
                () -> servicio.crearCausa(d.getAltKey(), new CrearCausaCmd("C1", "Repetida"), ACTOR));
        assertThat(ex.getCodigoError()).isEqualTo(CodigoError.CODIGO_CAUSA_DUPLICADO);
    }

    @Test
    @DisplayName("Obtener causas de un derecho inexistente lanza CAT-001")
    void causasDerechoInexistente() {
        CatalogoException ex = assertThrows(CatalogoException.class,
                () -> servicio.obtenerCausas("no-existe"));
        assertThat(ex.getCodigoError()).isEqualTo(CodigoError.DERECHO_NO_ENCONTRADO);
    }
}
