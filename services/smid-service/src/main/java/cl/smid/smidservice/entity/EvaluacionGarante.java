package cl.smid.smidservice.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "evaluacion_garante")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class EvaluacionGarante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requerimiento_codigo", nullable = false)
    @ToString.Exclude
    private Requerimiento requerimiento;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "institucion_id", nullable = false)
    private Institucion institucion;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "derecho_id", nullable = false)
    private Derecho derecho;

    @Min(0) @Max(4)
    private int scoreEfectividad;

    @Min(0) @Max(4)
    private int scorePriorizacion;

    @Min(0) @Max(4)
    private int scoreParticipacion;

    @Min(0) @Max(4)
    private int scoreIntersector;

    @Min(0) @Max(2)
    private int danioFisico;

    @Min(0) @Max(2)
    private int danioPsicologico;

    @Min(0) @Max(2)
    private int danioEstructural;

    @Min(0) @Max(2)
    private int danioUrgencia;

    @Min(0) @Max(2)
    private int estadoOmision;

    @Min(0) @Max(2)
    private int estadoRevictimiza;

    @Min(0) @Max(2)
    private int estadoContradiccion;

    @Column(name = "indice_alineacion", precision = 4, scale = 2)
    private BigDecimal indiceAlineacion;

    @OneToMany(mappedBy = "evaluacion", fetch = FetchType.LAZY)
    private List<OficioSeguimiento> oficios;
}
