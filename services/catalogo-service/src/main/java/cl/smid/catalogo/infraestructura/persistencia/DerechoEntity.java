package cl.smid.catalogo.infraestructura.persistencia;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;

/**
 * Entidad JPA de la tabla {@code derecho} (lista de adyacencia del árbol taxonómico).
 *
 * <p>Vive en infraestructura, separada del modelo de dominio {@code Derecho}: el
 * {@code MapeadorPersistencia} traduce entre ambos. Así el núcleo no depende de JPA.</p>
 *
 * <h2>Notas de compatibilidad con {@code ddl-auto=validate}</h2>
 * <ul>
 *   <li>{@code alt_key} se declara como <b>CHAR(36)</b> mediante
 *       {@link JdbcTypeCode}({@link SqlTypes#CHAR}) para que el tipo mapeado coincida
 *       exactamente con la columna creada por Flyway (evita el error «found char, expected
 *       varchar» del validador de esquema).</li>
 *   <li>{@code vigente} se mapea como {@code boolean}: con el conector MySQL
 *       ({@code tinyInt1isBit=true}, por defecto) una columna {@code TINYINT(1)} se reporta
 *       como BIT, de modo que la validación cuadra.</li>
 *   <li>La columna {@code creado_en} de la base <b>no se mapea</b> a propósito: el validador
 *       ignora columnas existentes no mapeadas, y así evitamos fricción entre {@code TIMESTAMP}
 *       y los tipos temporales de Java en un campo que el dominio no necesita.</li>
 * </ul>
 */
@Entity
@Table(name = "derecho")
@Getter
@Setter
@NoArgsConstructor
public class DerechoEntity {

    /** PK interna BIGINT autoincremental. Nunca se expone fuera de la frontera de API. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Identificador público opaco (UUID v4 en su forma canónica). */
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name = "alt_key", length = 36, nullable = false, unique = true)
    private String altKey;

    /** FK a {@code derecho(id)}: padre en la lista de adyacencia. {@code null} en las raíces. */
    @Column(name = "id_padre")
    private Long idPadre;

    /** Código estable e inmutable, único en todo el catálogo. */
    @Column(name = "codigo", length = 40, nullable = false, unique = true)
    private String codigo;

    @Column(name = "nombre", length = 200, nullable = false)
    private String nombre;

    @Column(name = "descripcion", length = 600)
    private String descripcion;

    /** Profundidad en el árbol (0 = raíz), derivada del padre. */
    @Column(name = "nivel", nullable = false)
    private short nivel;

    /** Orden de despliegue entre hermanos. */
    @Column(name = "orden", nullable = false)
    private short orden;

    /** Vigencia (borrado lógico). */
    @Column(name = "vigente", nullable = false)
    private boolean vigente;

    @Column(name = "vigente_desde", nullable = false)
    private LocalDate vigenteDesde;

    /** Fecha de baja lógica; {@code null} mientras el derecho está vigente. */
    @Column(name = "vigente_hasta")
    private LocalDate vigenteHasta;
}
