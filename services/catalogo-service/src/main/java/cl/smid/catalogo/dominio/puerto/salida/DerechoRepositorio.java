package cl.smid.catalogo.dominio.puerto.salida;

import cl.smid.catalogo.dominio.modelo.Derecho;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida hacia la persistencia de <b>derechos</b>.
 *
 * <p>El dominio define este contrato en términos de su propio modelo ({@link Derecho});
 * el adaptador JPA de infraestructura lo implementa. Aquí viven las operaciones que el
 * servicio necesita, incluida la recuperación del árbol mediante CTE recursiva (que el
 * adaptador resuelve con {@code WITH RECURSIVE} de MySQL 8).</p>
 */
public interface DerechoRepositorio {

    /**
     * Recupera, en una sola llamada, <b>todos los derechos vigentes alcanzables desde las
     * raíces vigentes</b>, en orden estable ({@code nivel, orden, codigo}). El ensamblado
     * del árbol anidado lo hace el dominio en memoria. Implementado con CTE recursiva.
     */
    List<Derecho> cargarArbolVigente();

    /** Todos los derechos vigentes en orden estable (para la vista {@code ?formato=plano}). */
    List<Derecho> cargarTodosVigentes();

    /**
     * Descendientes <b>vigentes</b> de un nodo (excluyendo el propio nodo), recorridos con
     * CTE recursiva. Lo usa la baja en cascada lógica.
     */
    List<Derecho> descendientesVigentes(Long idRaiz);

    /**
     * Identificadores internos de toda la descendencia del nodo (cualquier vigencia,
     * excluyendo el propio nodo). Lo usa la detección de ciclos al reubicar un derecho:
     * un nodo no puede moverse bajo sí mismo ni bajo ninguno de sus descendientes.
     */
    List<Long> idsDescendientes(Long idRaiz);

    /** Hijos directos vigentes de un nodo, en orden estable ({@code orden, codigo}). */
    List<Derecho> hijosDirectosVigentes(Long idPadre);

    /** Busca un derecho por su alt_key (cualquier vigencia). */
    Optional<Derecho> buscarPorAltKey(String altKey);

    /** Busca un derecho por su código (cualquier vigencia). */
    Optional<Derecho> buscarPorCodigo(String codigo);

    /** {@code true} si ya existe un derecho con ese código (en cualquier estado). */
    boolean existeCodigo(String codigo);

    /**
     * Búsqueda <b>acento-insensible y case-insensible</b> por nombre o código entre los
     * derechos vigentes (resuelta por la colación {@code utf8mb4_0900_ai_ci}: "educacion"
     * encuentra "educación"). Devuelve en orden estable.
     */
    List<Derecho> buscarPorTexto(String texto);

    /** Inserta un derecho nuevo y devuelve la instancia con su {@code id} interno asignado. */
    Derecho guardarNuevo(Derecho derecho);

    /** Persiste los cambios de un derecho existente (nombre, descripción, orden, nivel, vigencia). */
    void actualizar(Derecho derecho);

    /** Persiste en bloque varios derechos existentes (baja en cascada / recálculo de nivel). */
    void actualizarTodos(List<Derecho> derechos);
}
