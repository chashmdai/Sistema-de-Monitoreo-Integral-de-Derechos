package cl.smid.sgs.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** Raíz del expediente: el documento físico de origen. Sus datos son únicos por oficio (evita anomalía de actualización). */
@Entity
@Table(name = "sgs_oficio", indexes = {
        @Index(name = "ix_oficio_nro", columnList = "nro_oficio"),
        @Index(name = "ix_oficio_region", columnList = "region"),
        @Index(name = "ix_oficio_institucion", columnList = "institucion"),
        @Index(name = "ix_oficio_fecha", columnList = "fecha_ingreso")
})
@Getter
@Setter
@NoArgsConstructor
public class Oficio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nro_oficio", length = 100)
    private String nroOficio;

    @Column(name = "region", length = 120)
    private String region;

    @Column(name = "institucion", length = 255)
    private String institucion;

    @Column(name = "residencia_centro", length = 255)
    private String residenciaCentro;

    @Column(name = "nivel", length = 100)
    private String nivel;

    /** sha256 del PDF fuente; el contenido vive en MinIO bajo esta key (PDF-3). */
    @Column(name = "pdf_hash", length = 64)
    private String pdfHash;

    @CreationTimestamp
    @Column(name = "fecha_ingreso", updatable = false)
    private LocalDateTime fechaIngreso;

    @UpdateTimestamp
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @OneToMany(mappedBy = "oficio", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Recomendacion> recomendaciones = new ArrayList<>();

    public void addRecomendacion(Recomendacion r) {
        recomendaciones.add(r);
        r.setOficio(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Oficio that)) return false;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() { return getClass().hashCode(); }
}
