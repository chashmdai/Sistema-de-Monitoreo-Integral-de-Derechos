package cl.smid.requerimientos.infraestructura.persistencia.entidad;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

/**
 * Entidad JPA de una decisión de admisibilidad (tabla {@code admisibilidad}). La acción se
 * almacena como texto; el profesional asignado solo se completa en la acción ASIGNACION.
 */
@Entity
@Table(name = "admisibilidad")
@Getter
@Setter
@NoArgsConstructor
public class AdmisibilidadEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_requerimiento", nullable = false)
    private RequerimientoEntity requerimiento;

    @Column(name = "accion", nullable = false, length = 20)
    private String accion;

    @Column(name = "id_coordinador_alt", nullable = false, columnDefinition = "CHAR(36)")
    private String idCoordinadorAlt;

    @Column(name = "escalado_a_defensora", nullable = false, columnDefinition = "TINYINT(1)")
    private boolean escaladoADefensora;

    @Column(name = "id_profesional_asignado_alt", columnDefinition = "CHAR(36)")
    private String idProfesionalAsignadoAlt;

    @Column(name = "observacion", length = 2000)
    private String observacion;

    @Column(name = "decidido_en", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant decididoEn;
}
