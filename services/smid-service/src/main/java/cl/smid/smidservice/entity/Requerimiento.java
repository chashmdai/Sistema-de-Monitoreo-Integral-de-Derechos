package cl.smid.smidservice.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "requerimiento")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "codigo")
public class Requerimiento {

    @Id
    @Column(length = 20)
    private String codigo;

    private Integer anio;
    private LocalDate fechaIngreso;
    private String region;
    private String comuna;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "req_interseccionalidad",
        joinColumns = @JoinColumn(name = "requerimiento_codigo"),
        inverseJoinColumns = @JoinColumn(name = "factor_id")
    )
    private Set<FactorRiesgo> factoresRiesgo;

    @OneToMany(mappedBy = "requerimiento", cascade = CascadeType.ALL)
    private List<EvaluacionGarante> evaluaciones;

    public boolean isPrioridadReforzada() {
        return this.factoresRiesgo != null && !this.factoresRiesgo.isEmpty();
    }

    public boolean esPrioridadReforzada() {
        return isPrioridadReforzada();
    }
}
