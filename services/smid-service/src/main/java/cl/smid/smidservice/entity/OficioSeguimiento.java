package cl.smid.smidservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "oficio_seguimiento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OficioSeguimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Corta el bucle de Jackson al serializar a JSON
    @JsonIgnore 
    // Evita el bucle de Hibernate/Lombok si alguna vez se hace un log del objeto
    @ToString.Exclude 
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evaluacion_id", nullable = false)
    private EvaluacionGarante evaluacion;

    @Column(name = "nro_oficio", length = 50)
    private String nroOficio;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_documento", nullable = false)
    private TipoDocumento tipoDocumento;

    @Column(name = "fecha_envio", nullable = false)
    private LocalDate fechaEnvio;

    @Column(name = "plazo_dias", nullable = false)
    private Integer plazoDias;

    @Column(name = "fecha_vencimiento")
    private LocalDate fechaVencimiento;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_actual")
    @Builder.Default
    private EstadoOficio estadoActual = EstadoOficio.PENDIENTE;
}