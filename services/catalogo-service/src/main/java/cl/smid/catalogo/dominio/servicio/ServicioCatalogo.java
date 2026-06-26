package cl.smid.catalogo.dominio.servicio;

import cl.smid.catalogo.dominio.excepcion.CatalogoException;
import cl.smid.catalogo.dominio.modelo.Causa;
import cl.smid.catalogo.dominio.modelo.Derecho;
import cl.smid.catalogo.dominio.modelo.NodoArbol;
import cl.smid.catalogo.dominio.puerto.entrada.ActorEvento;
import cl.smid.catalogo.dominio.puerto.entrada.ActualizarDerechoCmd;
import cl.smid.catalogo.dominio.puerto.entrada.CatalogoUseCase;
import cl.smid.catalogo.dominio.puerto.entrada.CrearCausaCmd;
import cl.smid.catalogo.dominio.puerto.entrada.CrearDerechoCmd;
import cl.smid.catalogo.dominio.puerto.salida.CausaRepositorio;
import cl.smid.catalogo.dominio.puerto.salida.DerechoRepositorio;
import cl.smid.catalogo.dominio.puerto.salida.EventoDominio;
import cl.smid.catalogo.dominio.puerto.salida.EventoPublicador;
import cl.smid.catalogo.dominio.puerto.salida.GeneradorAltKey;
import cl.smid.catalogo.dominio.puerto.salida.RelojDominio;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Implementación del caso de uso del catálogo: orquesta el modelo de dominio y los puertos
 * de salida (persistencia, reloj, generación de identificadores y publicación de eventos).
 *
 * <p>Es un <b>POJO puro</b>: no lleva anotaciones de Spring. Se instancia y cablea como bean
 * en {@code config.DominioConfig}, lo que permite verificar toda la lógica con dobles en
 * memoria, sin levantar contexto de aplicación ni base de datos.</p>
 *
 * <h2>Transaccionalidad</h2>
 * <p>La demarcación transaccional se aplica en el adaptador de entrada (el controlador, o un
 * decorador transaccional declarado en la configuración): si una operación de escritura
 * lanza {@link CatalogoException}, la transacción se revierte y no se persiste nada a medias.
 * Los eventos se publican al final de la operación, una vez aplicadas las mutaciones.</p>
 */
public class ServicioCatalogo implements CatalogoUseCase {

    // Nombres estables de los eventos de dominio (Núcleo 2.8).
    private static final String EV_DERECHO_CREADO = "catalogo.derecho.creado";
    private static final String EV_DERECHO_ACTUALIZADO = "catalogo.derecho.actualizado";
    private static final String EV_DERECHO_BAJA = "catalogo.derecho.baja";
    private static final String EV_CAUSA_CREADA = "catalogo.causa.creada";

    private final DerechoRepositorio derechos;
    private final CausaRepositorio causas;
    private final RelojDominio reloj;
    private final EventoPublicador eventos;
    private final EnsambladorArbol ensamblador;
    private final GeneradorAltKey generadorAltKey;

    public ServicioCatalogo(DerechoRepositorio derechos,
                            CausaRepositorio causas,
                            RelojDominio reloj,
                            EventoPublicador eventos,
                            EnsambladorArbol ensamblador,
                            GeneradorAltKey generadorAltKey) {
        this.derechos = Objects.requireNonNull(derechos);
        this.causas = Objects.requireNonNull(causas);
        this.reloj = Objects.requireNonNull(reloj);
        this.eventos = Objects.requireNonNull(eventos);
        this.ensamblador = Objects.requireNonNull(ensamblador);
        this.generadorAltKey = Objects.requireNonNull(generadorAltKey);
    }

    // ============================== Lecturas ==============================

    @Override
    public List<NodoArbol> obtenerArbol() {
        // La CTE recursiva entrega el conjunto vigente en orden estable; aquí solo se anida.
        return ensamblador.ensamblar(derechos.cargarArbolVigente());
    }

    @Override
    public List<Derecho> obtenerPlano() {
        return derechos.cargarTodosVigentes();
    }

    @Override
    public NodoArbol obtenerDetalle(String altKey) {
        Derecho derecho = buscarDerechoObligatorio(altKey);
        // Se devuelve aunque esté de baja (consulta histórica): los enlaces no se rompen tras una baja.
        NodoArbol nodo = new NodoArbol(derecho);
        for (Derecho hijo : derechos.hijosDirectosVigentes(derecho.getId())) {
            nodo.agregarHijo(new NodoArbol(hijo));
        }
        return nodo;
    }

    @Override
    public List<Causa> obtenerCausas(String derechoAltKey) {
        Derecho derecho = buscarDerechoObligatorio(derechoAltKey);
        return causas.causasVigentesDe(derecho.getId());
    }

    @Override
    public List<Derecho> buscar(String texto) {
        if (texto == null) {
            return List.of();
        }
        String limpio = texto.trim();
        if (limpio.isEmpty()) {
            return List.of();
        }
        // La insensibilidad a acentos y mayúsculas la resuelve la colación utf8mb4_0900_ai_ci.
        return derechos.buscarPorTexto(limpio);
    }

    // ============================== Escrituras ==============================

    @Override
    public Derecho crearDerecho(CrearDerechoCmd cmd, ActorEvento actor) {
        Objects.requireNonNull(cmd, "el comando no puede ser nulo");

        String codigo = recortar(cmd.codigo());
        String nombre = recortar(cmd.nombre());
        String descripcion = limpiarOpcional(cmd.descripcion());
        short orden = cmd.orden() == null ? 0 : cmd.orden();

        // El código es único en todo el catálogo.
        if (codigo != null && derechos.existeCodigo(codigo)) {
            throw CatalogoException.codigoDerechoDuplicado(codigo);
        }

        String altKey = generadorAltKey.nuevo();
        LocalDate hoy = reloj.hoy();

        Derecho aGuardar;
        if (esVacio(cmd.idPadreAltKey())) {
            aGuardar = Derecho.nuevaRaiz(altKey, codigo, nombre, descripcion, orden, hoy);
        } else {
            Derecho padre = derechos.buscarPorAltKey(cmd.idPadreAltKey())
                    .orElseThrow(() -> CatalogoException.arbolInvalido(
                            "El derecho padre %s no existe.".formatted(cmd.idPadreAltKey())));
            if (!padre.estaVigente()) {
                throw CatalogoException.arbolInvalido(
                        "No se puede crear un derecho bajo un padre dado de baja.");
            }
            aGuardar = Derecho.nuevoHijo(altKey, codigo, nombre, descripcion, orden, padre, hoy);
        }

        Derecho persistido = derechos.guardarNuevo(aGuardar);
        publicar(EV_DERECHO_CREADO, persistido.getAltKey(), actor, Map.of(
                "codigo", persistido.getCodigo(),
                "nivel", (int) persistido.getNivel(),
                "esRaiz", persistido.esRaiz()));
        return persistido;
    }

    @Override
    public Derecho actualizarDerecho(String altKey, ActualizarDerechoCmd cmd, ActorEvento actor) {
        Objects.requireNonNull(cmd, "el comando no puede ser nulo");
        Derecho derecho = buscarDerechoObligatorio(altKey);

        // 1) Inmutabilidad del código: si llega un código y difiere del actual, se rechaza.
        if (!esVacio(cmd.codigo())) {
            String solicitado = cmd.codigo().trim();
            if (!solicitado.equals(derecho.getCodigo())) {
                throw CatalogoException.codigoInmutable();
            }
        }

        // 2) Nombre (obligatorio) y descripción (reemplazo).
        derecho.renombrar(recortar(cmd.nombre()));
        derecho.cambiarDescripcion(limpiarOpcional(cmd.descripcion()));

        // 3) Orden, solo si se especifica.
        if (cmd.orden() != null) {
            derecho.cambiarOrden(cmd.orden());
        }

        // 4) Reubicación opcional bajo otro padre (con verificación de ciclo y recálculo de nivel).
        List<Derecho> descendientesActualizados = new ArrayList<>();
        if (!esVacio(cmd.idPadreAltKey())) {
            descendientesActualizados = reubicar(derecho, cmd.idPadreAltKey().trim());
        }

        derechos.actualizar(derecho);
        if (!descendientesActualizados.isEmpty()) {
            derechos.actualizarTodos(descendientesActualizados);
        }

        publicar(EV_DERECHO_ACTUALIZADO, derecho.getAltKey(), actor, Map.of(
                "codigo", derecho.getCodigo(),
                "nivel", (int) derecho.getNivel(),
                "descendientesRecalculados", descendientesActualizados.size()));
        return derecho;
    }

    @Override
    public void darDeBajaDerecho(String altKey, ActorEvento actor) {
        Derecho derecho = buscarDerechoObligatorio(altKey);
        LocalDate hoy = reloj.hoy();

        // Idempotencia: si ya estaba de baja, no hay cambios, cascada ni evento.
        if (!derecho.darDeBaja(hoy)) {
            return;
        }

        // Baja en cascada lógica sobre los descendientes vigentes.
        List<Derecho> descendientes = derechos.descendientesVigentes(derecho.getId());
        List<Derecho> afectados = new ArrayList<>(descendientes.size() + 1);
        afectados.add(derecho);
        for (Derecho hijo : descendientes) {
            if (hijo.darDeBaja(hoy)) {
                afectados.add(hijo);
            }
        }

        derechos.actualizarTodos(afectados);
        publicar(EV_DERECHO_BAJA, derecho.getAltKey(), actor, Map.of(
                "codigo", derecho.getCodigo(),
                "descendientesAfectados", descendientes.size()));
    }

    @Override
    public Causa crearCausa(String derechoAltKey, CrearCausaCmd cmd, ActorEvento actor) {
        Objects.requireNonNull(cmd, "el comando no puede ser nulo");
        Derecho derecho = buscarDerechoObligatorio(derechoAltKey);

        String codigo = recortar(cmd.codigo());
        String nombre = recortar(cmd.nombre());

        if (codigo != null && causas.existeCodigoEnDerecho(derecho.getId(), codigo)) {
            throw CatalogoException.codigoCausaDuplicado(codigo);
        }

        Causa causa = Causa.nueva(derecho.getId(), generadorAltKey.nuevo(), codigo, nombre);
        Causa persistida = causas.guardarNueva(causa);
        publicar(EV_CAUSA_CREADA, persistida.getAltKey(), actor, Map.of(
                "derecho", derecho.getAltKey(),
                "codigo", persistida.getCodigo()));
        return persistida;
    }

    // ============================== Apoyo interno ==============================

    /**
     * Reubica {@code derecho} bajo el padre indicado por su alt_key, validando que no se
     * produzca un ciclo, y recalcula el nivel del subárbol vigente afectado.
     *
     * @return lista de descendientes vigentes cuyo nivel cambió (para persistir en bloque);
     *         vacía si no hubo desplazamiento de nivel o si el padre no cambió
     */
    private List<Derecho> reubicar(Derecho derecho, String nuevoPadreAltKey) {
        Derecho nuevoPadre = derechos.buscarPorAltKey(nuevoPadreAltKey)
                .orElseThrow(() -> CatalogoException.arbolInvalido(
                        "El derecho padre %s no existe.".formatted(nuevoPadreAltKey)));

        // Sin cambios si ya cuelga de ese padre.
        if (Objects.equals(nuevoPadre.getId(), derecho.getIdPadre())) {
            return List.of();
        }
        // Un nodo no puede ser su propio padre.
        if (Objects.equals(nuevoPadre.getId(), derecho.getId())) {
            throw CatalogoException.arbolInvalido("Un derecho no puede ser su propio padre.");
        }
        if (!nuevoPadre.estaVigente()) {
            throw CatalogoException.arbolInvalido("No se puede reubicar un derecho bajo un padre dado de baja.");
        }
        // Detección de ciclo: el nuevo padre no puede ser descendiente del nodo movido.
        List<Long> descendientesIds = derechos.idsDescendientes(derecho.getId());
        if (descendientesIds.contains(nuevoPadre.getId())) {
            throw CatalogoException.arbolInvalido(
                    "No se puede mover un derecho bajo uno de sus propios descendientes (ciclo).");
        }

        short nivelAnterior = derecho.getNivel();
        derecho.reubicarBajo(nuevoPadre);          // ajusta idPadre y nivel propio
        short delta = (short) (derecho.getNivel() - nivelAnterior);
        if (delta == 0) {
            return List.of();
        }
        // Propagar el desplazamiento de nivel a todo el subárbol vigente.
        List<Derecho> descendientes = derechos.descendientesVigentes(derecho.getId());
        for (Derecho hijo : descendientes) {
            hijo.fijarNivel((short) (hijo.getNivel() + delta));
        }
        return descendientes;
    }

    private Derecho buscarDerechoObligatorio(String altKey) {
        return derechos.buscarPorAltKey(altKey)
                .orElseThrow(() -> CatalogoException.derechoNoEncontrado(altKey));
    }

    private void publicar(String evento, String recursoAltKey, ActorEvento actor, Map<String, Object> datos) {
        ActorEvento a = (actor == null) ? ActorEvento.sistema() : actor;
        eventos.publicar(new EventoDominio(
                evento, reloj.ahora(), a.actor(), recursoAltKey, a.idSede(), a.idUnidad(), datos));
    }

    /** Recorta espacios; devuelve {@code null} si la entrada es nula (el modelo valida lo obligatorio). */
    private static String recortar(String valor) {
        return valor == null ? null : valor.trim();
    }

    /** Recorta y convierte a {@code null} las cadenas vacías (campos opcionales). */
    private static String limpiarOpcional(String valor) {
        if (valor == null) {
            return null;
        }
        String t = valor.trim();
        return t.isEmpty() ? null : t;
    }

    private static boolean esVacio(String valor) {
        return valor == null || valor.isBlank();
    }
}
