package cl.smid.catalogo.dominio.puerto.entrada;

import cl.smid.catalogo.dominio.modelo.Causa;
import cl.smid.catalogo.dominio.modelo.Derecho;
import cl.smid.catalogo.dominio.modelo.NodoArbol;

import java.util.List;

/**
 * Puerto de entrada (driving port) del servicio de Catálogo: la frontera de aplicación que
 * la capa api (controladores) invoca. Expone las operaciones del contrato §4 en términos
 * del <b>modelo de dominio</b>; los DTO de transporte y su validación viven en la capa api.
 *
 * <p>Las operaciones de lectura solo requieren autenticación; las de escritura requieren rol
 * de Administración (lo hace cumplir la capa de seguridad antes de llegar aquí). Las
 * escrituras reciben además un {@link ActorEvento} para enriquecer los eventos de dominio.</p>
 */
public interface CatalogoUseCase {

    // ----------------------------- Lecturas -----------------------------

    /**
     * Árbol taxonómico completo de derechos vigentes, ya ensamblado en memoria y en orden
     * estable. Devuelve la lista de nodos raíz; cada uno expone sus hijos anidados.
     */
    List<NodoArbol> obtenerArbol();

    /**
     * Vista plana de todos los derechos vigentes, en orden estable. La capa api la proyecta
     * resolviendo el alt_key del padre de cada nodo (formato {@code ?formato=plano}).
     */
    List<Derecho> obtenerPlano();

    /**
     * Detalle de un derecho junto con sus <b>hijos directos vigentes</b>. El nodo devuelto
     * envuelve el derecho solicitado y, como hijos, sus descendientes inmediatos.
     *
     * @throws cl.smid.catalogo.dominio.excepcion.CatalogoException CAT-001 si no existe
     */
    NodoArbol obtenerDetalle(String altKey);

    /**
     * Causas vigentes de un derecho.
     *
     * @throws cl.smid.catalogo.dominio.excepcion.CatalogoException CAT-001 si el derecho no existe
     */
    List<Causa> obtenerCausas(String derechoAltKey);

    /**
     * Búsqueda acento- e insensible a mayúsculas por nombre o código entre derechos vigentes.
     * Un texto vacío o en blanco devuelve lista vacía.
     */
    List<Derecho> buscar(String texto);

    // ----------------------------- Escrituras -----------------------------

    /**
     * Crea un derecho (raíz o hijo) y publica {@code catalogo.derecho.creado}.
     *
     * @throws cl.smid.catalogo.dominio.excepcion.CatalogoException CAT-002 código duplicado;
     *         CAT-004 padre inexistente
     */
    Derecho crearDerecho(CrearDerechoCmd cmd, ActorEvento actor);

    /**
     * Actualiza un derecho existente (nombre, descripción, orden y opcional reubicación) y
     * publica {@code catalogo.derecho.actualizado}.
     *
     * @throws cl.smid.catalogo.dominio.excepcion.CatalogoException CAT-001 no encontrado;
     *         CAT-006 intento de cambiar el código; CAT-004 ciclo o padre inexistente
     */
    Derecho actualizarDerecho(String altKey, ActualizarDerechoCmd cmd, ActorEvento actor);

    /**
     * Da de baja lógica un derecho y, en cascada, sus descendientes vigentes; publica
     * {@code catalogo.derecho.baja} con la cantidad de descendientes afectados. Es idempotente.
     *
     * @throws cl.smid.catalogo.dominio.excepcion.CatalogoException CAT-001 si no existe
     */
    void darDeBajaDerecho(String altKey, ActorEvento actor);

    /**
     * Crea una causa para un derecho y publica {@code catalogo.causa.creada}.
     *
     * @throws cl.smid.catalogo.dominio.excepcion.CatalogoException CAT-001 derecho inexistente;
     *         CAT-003 código de causa duplicado dentro del derecho
     */
    Causa crearCausa(String derechoAltKey, CrearCausaCmd cmd, ActorEvento actor);
}
