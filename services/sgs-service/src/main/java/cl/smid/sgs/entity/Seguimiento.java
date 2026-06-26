package cl.smid.sgs.entity;

import cl.smid.sgs.enums.EvaluacionCumplimiento;
import cl.smid.sgs.enums.FaseSeguimiento;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Historial temporal: un registro por hito/fase. Aquí vive el desdoble IA/humano (MOD-2).
 * Los campos evaluacionIA / valoracionRubrica / confianza / razonamiento son INMUTABLES tras /aplicar:
 * son el sustento auditable de la propuesta algorítmica. evaluacionFinal es el override humano.
 */
@Entity
@Table(name = "sgs_seguimiento", indexes = {
        @Index(name = "ix_seg_rec", columnList = "recomendacion_id"),
        @Index(name = "ix_seg_fecha", columnList = "fecha_seguimiento")
})
@Getter
@Setter
@NoArgsConstructor
public class Seguimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "recomendacion_id", nullable = false)
    private Recomendacion recomendacion;

    @Enumerated(EnumType.STRING)
    @Column(name = "fase", length = 60)
    private FaseSeguimiento fase;

    @Column(name = "fecha_seguimiento")
    private LocalDate fechaSeguimiento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_seguimiento_id")
    private Catalogo tipoSeguimiento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_respuesta_id")
    private Catalogo tipoRespuesta;

    @Column(name = "fecha_respuesta")
    private LocalDate fechaRespuesta;

    @Column(name = "otro_seguimiento_inst", length = 500)
    private String otroSeguimientoInstitucional;

    // ---- PROPUESTA IA (inmutable) ----
    @Enumerated(EnumType.STRING)
    @Column(name = "evaluacion_ia", length = 60, updatable = false)
    private EvaluacionCumplimiento evaluacionIA;

    @Column(name = "valoracion_rubrica", columnDefinition = "TEXT", updatable = false)
    private String valoracionRubrica;

    @Column(name = "confianza", updatable = false)
    private Double confianza;

    @Column(name = "razonamiento", columnDefinition = "TEXT", updatable = false)
    private String razonamiento;

    @Column(name = "requiere_revision_humana", nullable = false)
    private boolean requiereRevisionHumana = false;

    // ---- OVERRIDE HUMANO ----
    @Enumerated(EnumType.STRING)
    @Column(name = "evaluacion_final", length = 60)
    private EvaluacionCumplimiento evaluacionFinal;

    @Column(name = "evaluacion_final_autor", length = 50)
    private String evaluacionFinalAutor;

    @Column(name = "evaluacion_final_fecha")
    private LocalDateTime evaluacionFinalFecha;

    @Column(name = "responsable_seguimiento", length = 150)
    private String responsableSeguimiento;

    @Version
    @Column(name = "version")
    private Long version;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Seguimiento that)) return false;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() { return getClass().hashCode(); }
}
