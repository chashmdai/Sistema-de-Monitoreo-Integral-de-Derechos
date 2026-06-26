package cl.smid.sgs.entity;

import cl.smid.sgs.enums.EstadoGestion;
import cl.smid.sgs.enums.PlazoRecomendacion;
import cl.smid.sgs.enums.RespuestaSiNo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** Hija de Oficio. Una recomendación = un nudo crítico con su plazo, gestión e historial de seguimiento. */
@Entity
@Table(name = "sgs_recomendacion", indexes = {
        @Index(name = "ix_rec_oficio", columnList = "oficio_id"),
        @Index(name = "ix_rec_estado", columnList = "estado"),
        @Index(name = "ix_rec_gv", columnList = "gv"),
        @Index(name = "ix_rec_plazo", columnList = "plazo")
})
@Getter
@Setter
@NoArgsConstructor
public class Recomendacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "oficio_id", nullable = false)
    private Oficio oficio;

    /** Identidad de negocio = oficio.nroOficio + correlativo. */
    @Column(name = "correlativo", length = 50)
    private String correlativo;

    @Column(name = "dimension", length = 150)
    private String dimension;

    @Column(name = "nudo_critico", columnDefinition = "TEXT")
    private String nudoCritico;

    @Column(name = "tipo_recomendacion", length = 200)
    private String tipoRecomendacion;

    /** Verbo rector: texto libre, sugerido por IA (decisión #9). */
    @Column(name = "verbo", length = 200)
    private String verbo;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(name = "plazo", length = 30)
    private PlazoRecomendacion plazo;

    /** Plazo textual original extraído del oficio (auditoría del mapeo). */
    @Column(name = "plazo_raw", length = 120)
    private String plazoRaw;

    /** Graves Violaciones: clasificación binaria del caso (decisión #7). NO se extrae de la respuesta. */
    @Column(name = "gv", nullable = false)
    private boolean gv = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "acoge", length = 5)
    private RespuestaSiNo acoge;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", length = 30, nullable = false)
    private EstadoGestion estado = EstadoGestion.PENDIENTE_REGISTRO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "materia_id")
    private Catalogo materia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id")
    private Catalogo categoria;

    @Column(name = "profesional_responsable", length = 150)
    private String profesionalResponsable;

    @Column(name = "responsable_seguimiento", length = 150)
    private String responsableSeguimiento;

    // Borrado lógico (ENT-10)
    @Column(name = "anulado", nullable = false)
    private boolean anulado = false;

    @Column(name = "motivo_anulacion", columnDefinition = "TEXT")
    private String motivoAnulacion;

    @Column(name = "anulado_por", length = 50)
    private String anuladoPor;

    @Version
    @Column(name = "version")
    private Long version;

    @OneToMany(mappedBy = "recomendacion", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("orden ASC")
    private List<Accion> acciones = new ArrayList<>();

    @OneToMany(mappedBy = "recomendacion", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("fechaSeguimiento DESC")
    private List<Seguimiento> seguimientos = new ArrayList<>();

    public void addAccion(Accion a) {
        acciones.add(a);
        a.setRecomendacion(this);
    }

    public void addSeguimiento(Seguimiento s) {
        seguimientos.add(s);
        s.setRecomendacion(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Recomendacion that)) return false;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() { return getClass().hashCode(); }
}
