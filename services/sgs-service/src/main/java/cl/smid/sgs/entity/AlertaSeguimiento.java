package cl.smid.sgs.entity;

import cl.smid.sgs.enums.TipoAlerta;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/** Alerta generada por el barrido diario del scheduler (decisión #6). Dos relojes vía 'tipo'. */
@Entity
@Table(name = "sgs_alerta", indexes = {
        @Index(name = "ix_alerta_rec", columnList = "recomendacion_id"),
        @Index(name = "ix_alerta_atendida", columnList = "atendida")
})
@Getter
@Setter
@NoArgsConstructor
public class AlertaSeguimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "recomendacion_id", nullable = false)
    private Long recomendacionId;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", length = 30, nullable = false)
    private TipoAlerta tipo;

    @Column(name = "fecha_limite", nullable = false)
    private LocalDate fechaLimite;

    @Column(name = "fecha_generada", nullable = false)
    private LocalDateTime fechaGenerada;

    @Column(name = "atendida", nullable = false)
    private boolean atendida = false;

    @Column(name = "notificada_telegram", nullable = false)
    private boolean notificadaTelegram = false;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AlertaSeguimiento that)) return false;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() { return getClass().hashCode(); }
}
