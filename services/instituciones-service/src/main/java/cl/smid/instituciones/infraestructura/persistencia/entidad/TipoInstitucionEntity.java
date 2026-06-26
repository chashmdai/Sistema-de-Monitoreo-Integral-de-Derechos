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
 * Entidad JPA de la tabla {@code tipo_institucion}. Es un detalle de infraestructura:
 * sus columnas calzan 1:1 con la migración Flyway (Hibernate solo valida). Lombok se
 * usa exclusivamente en esta capa de entidades.
 *
 * <p>Las marcas temporales se almacenan en {@link LocalDateTime} (UTC, con
 * {@code hibernate.jdbc.time_zone=UTC}); la conversión desde/hacia {@code Instant} del
 * dominio la realiza el mapeador.</p>
 */
@Entity
@Table(name = "tipo_institucion")
@Getter
@Setter
@NoArgsConstructor
public class TipoInstitucionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "alt_key", nullable = false, length = 36)
    private String altKey;

    @Column(name = "nombre", nullable = false, length = 160)
    private String nombre;

    @Column(name = "ambito", nullable = false, length = 20)
    private String ambito;

    @Column(name = "descripcion", length = 500)
    private String descripcion;

    @Column(name = "vigente", nullable = false)
    private boolean vigente;

    @Column(name = "creado_en", nullable = false)
    private LocalDateTime creadoEn;

    @Column(name = "actualizado_en", nullable = false)
    private LocalDateTime actualizadoEn;
}
