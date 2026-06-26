package cl.smid.smidservice.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "tramite_legislativo")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class TramiteLegislativo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ley_id", nullable = false)
    private Ley ley;

    @Column(name = "nombre_tramite", nullable = false, length = 100)
    private String nombreTramite;

    @Column(name = "fecha_tramite")
    private LocalDate fechaTramite;

    @Lob
    @Column(name = "analisis_interes_superior", columnDefinition = "TEXT")
    private String analisisInteresSuperior;

    @Lob
    @Column(name = "analisis_autonomia", columnDefinition = "TEXT")
    private String analisisAutonomia;

    @Lob
    @Column(name = "analisis_participacion", columnDefinition = "TEXT")
    private String analisisParticipacion;

    @Min(1) @Max(5) private Integer scoreInteresSup;
    @Min(1) @Max(5) private Integer scoreParticipacion;
    @Min(1) @Max(5) private Integer scoreAutonomia;
    @Min(1) @Max(5) private Integer scoreInterseccion;
    @Min(1) @Max(5) private Integer scoreProporcion;
    @Min(1) @Max(5) private Integer scoreRolGarante;
    @Min(1) @Max(5) private Integer scoreOperatividad;

    @Column(name = "indice_irn", precision = 4, scale = 2)
    private BigDecimal indiceIrn;

    @Column(name = "indice_irri", precision = 4, scale = 2)
    private BigDecimal indiceIrri;

    @Column(name = "indice_compuesto", precision = 4, scale = 2)
    private BigDecimal indiceCompuesto;
}
