package cl.smid.smidservice.entity;

import java.time.LocalDate;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ley")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ley {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombre_ley", nullable = false, length = 200)
    private String nombreLey;

    @Column(name = "numero_boletin", length = 50)
    private String numeroBoletin;

    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;
}
