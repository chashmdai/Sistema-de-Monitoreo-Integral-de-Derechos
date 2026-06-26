package cl.smid.smidservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "factor_riesgo")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FactorRiesgo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 50)
    private String nombre;
}
