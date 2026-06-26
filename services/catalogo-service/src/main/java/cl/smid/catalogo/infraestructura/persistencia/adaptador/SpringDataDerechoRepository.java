package cl.smid.catalogo.infraestructura.persistencia.adaptador;

import cl.smid.catalogo.infraestructura.persistencia.DerechoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio Spring Data JPA de {@link DerechoEntity}.
 *
 * <p>Combina derivación de consultas por nombre de método (para los casos simples) con
 * consultas nativas {@code WITH RECURSIVE} de MySQL 8 (para el recorrido del árbol, que es
 * donde una base relacional es claramente más eficiente que traer todo y recorrer en Java).
 * Las consultas nativas devuelven la propia entidad: el {@code SELECT *} incluye columnas no
 * mapeadas (como {@code creado_en}), que Hibernate simplemente ignora al materializar.</p>
 */
public interface SpringDataDerechoRepository extends JpaRepository<DerechoEntity, Long> {

    Optional<DerechoEntity> findByAltKey(String altKey);

    Optional<DerechoEntity> findByCodigo(String codigo);

    boolean existsByCodigo(String codigo);

    /** Hijos directos vigentes de un nodo, en orden estable. */
    List<DerechoEntity> findByIdPadreAndVigenteTrueOrderByOrdenAscCodigoAsc(Long idPadre);

    /** Todos los derechos vigentes, en orden estable (vista plana). */
    List<DerechoEntity> findByVigenteTrueOrderByNivelAscOrdenAscCodigoAsc();

    /**
     * Árbol completo de derechos <b>vigentes</b> alcanzables desde las raíces vigentes,
     * recorrido con CTE recursiva y devuelto en orden estable ({@code nivel, orden, codigo}).
     */
    @Query(value = """
            WITH RECURSIVE arbol AS (
                SELECT d.* FROM derecho d
                WHERE d.id_padre IS NULL AND d.vigente = 1
                UNION ALL
                SELECT h.* FROM derecho h
                JOIN arbol a ON h.id_padre = a.id
                WHERE h.vigente = 1
            )
            SELECT * FROM arbol
            ORDER BY nivel, orden, codigo
            """, nativeQuery = true)
    List<DerechoEntity> cargarArbolVigente();

    /**
     * Descendientes <b>vigentes</b> de un nodo (excluido el propio nodo), en orden estable.
     * Lo usa la baja en cascada lógica.
     */
    @Query(value = """
            WITH RECURSIVE sub AS (
                SELECT h.* FROM derecho h
                WHERE h.id_padre = :idRaiz AND h.vigente = 1
                UNION ALL
                SELECT n.* FROM derecho n
                JOIN sub s ON n.id_padre = s.id
                WHERE n.vigente = 1
            )
            SELECT * FROM sub
            ORDER BY nivel, orden, codigo
            """, nativeQuery = true)
    List<DerechoEntity> descendientesVigentes(@Param("idRaiz") Long idRaiz);

    /**
     * Identificadores de <b>toda</b> la descendencia de un nodo (cualquier vigencia, excluido
     * el propio nodo). Lo usa la detección de ciclos al reubicar un derecho.
     */
    @Query(value = """
            WITH RECURSIVE sub AS (
                SELECT h.id, h.id_padre FROM derecho h
                WHERE h.id_padre = :idRaiz
                UNION ALL
                SELECT n.id, n.id_padre FROM derecho n
                JOIN sub s ON n.id_padre = s.id
            )
            SELECT id FROM sub
            """, nativeQuery = true)
    List<Long> idsDescendientes(@Param("idRaiz") Long idRaiz);

    /**
     * Búsqueda por nombre o código entre derechos vigentes. La insensibilidad a acentos y
     * mayúsculas la aporta la colación {@code utf8mb4_0900_ai_ci} de las columnas (de modo que
     * "educacion" encuentra "educación"); el patrón se arma con comodines en el adaptador.
     */
    @Query(value = """
            SELECT d.* FROM derecho d
            WHERE d.vigente = 1
              AND (d.nombre LIKE :patron OR d.codigo LIKE :patron)
            ORDER BY d.nivel, d.orden, d.codigo
            """, nativeQuery = true)
    List<DerechoEntity> buscarPorTexto(@Param("patron") String patron);
}
