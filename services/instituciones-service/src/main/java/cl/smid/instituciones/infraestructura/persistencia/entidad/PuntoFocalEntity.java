package cl.smid.instituciones.infraestructura.persistencia.entidad;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Entidad JPA de la tabla {@code punto_focal}. Pertenece a una institución mediante la
 * PK interna escalar {@code institucion_id} (sin relación JPA bidireccional). La
 * invariante "a lo sumo un principal activo por institución" la sostiene el dominio
 * dentro de la transacción del controlador.
 */
@Entity
@Table(name = "punto_focal")
@Getter
@Setter
@NoArgsConstructor
public class PuntoFocalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "alt_key", nullable = false, length = 36)
    private String altKey;

    @Column(name = "institucion_id", nullable = false)
    private Long institucionId;

    @Column(name = "nombre", nullable = false, length = 160)
    private String nombre;

    @Column(name = "cargo", length = 120)
    private String cargo;

    @Column(name = "email", length = 160)
    private String email;

    @Column(name = "telefono", length = 40)
    private String telefono;

    @Column(name = "principal", nullable = false)
    private boolean principal;

    @Column(name = "activo", nullable = false)
    private boolean activo;

    @Column(name = "creado_en", nullable = false)
    private LocalDateTime creadoEn;

    @Column(name = "actualizado_en", nullable = false)
    private LocalDateTime actualizadoEn;
}
