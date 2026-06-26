package cl.smid.sgs.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

/** Acción requerida para subsanar el nudo crítico. N variable por recomendación (ENT-7). */
@Entity
@Table(name = "sgs_accion", indexes = @Index(name = "ix_accion_rec", columnList = "recomendacion_id"))
@Getter
@Setter
@NoArgsConstructor
public class Accion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "recomendacion_id", nullable = false)
    private Recomendacion recomendacion;

    @Column(name = "orden", nullable = false)
    private int orden;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Accion that)) return false;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() { return getClass().hashCode(); }
}
