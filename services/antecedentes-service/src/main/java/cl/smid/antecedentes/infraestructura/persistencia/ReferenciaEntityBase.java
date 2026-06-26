package cl.smid.antecedentes.infraestructura.persistencia;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Superclase mapeada con los campos comunes de las tres tablas de referencia (categoria_ddn,
 * proceso_ddn, instrumento). PK BIGINT interna nunca expuesta; identificador publico opaco en
 * {@code alt_key}; {@code codigo} unico; marcas {@code DATETIME(6)} UTC. Lombok solo en
 * entidades JPA (prohibido en el dominio).
 */
@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
public abstract class ReferenciaEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "alt_key", nullable = false, updatable = false, length = 36)
    private String altKey;

    @Column(name = "codigo", nullable = false, updatable = false, length = 80)
    private String codigo;

    @Column(name = "nombre", nullable = false, length = 200)
    private String nombre;

    @Column(name = "vigente", nullable = false)
    private boolean vigente;

    @Column(name = "creado_en", nullable = false, updatable = false)
    private LocalDateTime creadoEn;

    @Column(name = "actualizado_en", nullable = false)
    private LocalDateTime actualizadoEn;
}
