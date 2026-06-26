package cl.smid.sgs.entity;

import cl.smid.sgs.enums.TipoCatalogo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

/** Catálogo parametrizable (materia/categoría/tipo de seguimiento/tipo de respuesta). Editable por admin (decisión #9). */
@Entity
@Table(name = "sgs_catalogo", uniqueConstraints = @UniqueConstraint(name = "uk_catalogo_tipo_codigo", columnNames = {"tipo", "codigo"}))
@Getter
@Setter
@NoArgsConstructor
public class Catalogo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", length = 30, nullable = false)
    private TipoCatalogo tipo;

    @Column(name = "codigo", length = 80, nullable = false)
    private String codigo;

    @Column(name = "etiqueta", length = 255, nullable = false)
    private String etiqueta;

    @Column(name = "activo", nullable = false)
    private boolean activo = true;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Catalogo that)) return false;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() { return getClass().hashCode(); }
}
