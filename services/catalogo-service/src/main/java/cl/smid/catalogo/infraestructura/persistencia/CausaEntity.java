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

/**
 * Entidad JPA de la tabla {@code causa} (motivos/forma de vulneración vinculados a un derecho).
 *
 * <p>La unicidad del {@code codigo} es por derecho (restricción {@code uk_causa(id_derecho, codigo)}
 * en la base). Aplican las mismas notas de compatibilidad con {@code validate} que en
 * {@link DerechoEntity} (alt_key como CHAR(36), vigente como boolean).</p>
 */
@Entity
@Table(name = "causa")
@Getter
@Setter
@NoArgsConstructor
public class CausaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name = "alt_key", length = 36, nullable = false, unique = true)
    private String altKey;

    /** FK al derecho dueño. */
    @Column(name = "id_derecho", nullable = false)
    private Long idDerecho;

    @Column(name = "codigo", length = 40, nullable = false)
    private String codigo;

    @Column(name = "nombre", length = 200, nullable = false)
    private String nombre;

    @Column(name = "vigente", nullable = false)
    private boolean vigente;
}
