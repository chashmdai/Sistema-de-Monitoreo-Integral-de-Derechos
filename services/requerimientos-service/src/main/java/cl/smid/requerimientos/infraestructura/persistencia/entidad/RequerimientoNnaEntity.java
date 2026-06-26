package cl.smid.requerimientos.infraestructura.persistencia.entidad;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidad JPA de un NNA afectado (tabla {@code requerimiento_nna}). Guarda el alt_key del NNA en
 * personas-service y un snapshot JSON mínimo (nombre/RUT) de resiliencia.
 */
@Entity
@Table(name = "requerimiento_nna")
@Getter
@Setter
@NoArgsConstructor
public class RequerimientoNnaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_requerimiento", nullable = false)
    private RequerimientoEntity requerimiento;

    @Column(name = "id_persona_alt", nullable = false, columnDefinition = "CHAR(36)")
    private String idPersonaAlt;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "persona_snapshot", columnDefinition = "JSON")
    private String personaSnapshot;

    /** Derechos vulnerados imputados a este NNA. */
    @OneToMany(mappedBy = "nna", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<RequerimientoNnaDerechoEntity> derechos = new ArrayList<>();

    public void agregarDerecho(RequerimientoNnaDerechoEntity derecho) {
        derecho.setNna(this);
        derechos.add(derecho);
    }
}
