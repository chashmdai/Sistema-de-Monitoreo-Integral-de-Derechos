package cl.smid.requerimientos.infraestructura.persistencia.entidad;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad JPA del agregado {@code requerimiento} (tabla homónima). Vive en la capa de
 * infraestructura; Lombok está permitido aquí (no en el dominio). Los enumerados se almacenan
 * como texto y los snapshots como JSON; el mapeo dominio↔entidad es explícito.
 *
 * <p>Los {@code columnDefinition} se declaran para que {@code ddl-auto=validate} concuerde con
 * el esquema gobernado por Flyway (CHAR(36), DATETIME(6), JSON, TINYINT(1)).</p>
 */
@Entity
@Table(name = "requerimiento")
@Getter
@Setter
@NoArgsConstructor
public class RequerimientoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "alt_key", nullable = false, columnDefinition = "CHAR(36)")
    private String altKey;

    @Column(name = "folio", nullable = false, length = 24)
    private String folio;

    @Column(name = "id_sede", nullable = false, columnDefinition = "CHAR(36)")
    private String idSede;

    @Column(name = "id_unidad_destino", columnDefinition = "CHAR(36)")
    private String idUnidadDestino;

    @Column(name = "estado", nullable = false, length = 20)
    private String estado;

    @Column(name = "canal", length = 20)
    private String canal;

    @Column(name = "complejidad", length = 20)
    private String complejidad;

    @Column(name = "urgencia", length = 20)
    private String urgencia;

    @Column(name = "requiere_ficha_reservada", nullable = false, columnDefinition = "TINYINT(1)")
    private boolean requiereFichaReservada;

    @Column(name = "id_requirente_alt", columnDefinition = "CHAR(36)")
    private String idRequirenteAlt;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "requirente_snapshot", columnDefinition = "JSON")
    private String requirenteSnapshot;

    @Column(name = "resumen", length = 2000)
    private String resumen;

    @Column(name = "fecha_ingreso", columnDefinition = "DATETIME(6)")
    private Instant fechaIngreso;

    @Column(name = "es_beta", nullable = false, columnDefinition = "TINYINT(1)")
    private boolean esBeta;

    @Column(name = "vigente", nullable = false, columnDefinition = "TINYINT(1)")
    private boolean vigente;

    @Column(name = "creado_en", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant creadoEn;

    @Column(name = "actualizado_en", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant actualizadoEn;

    @Column(name = "creado_por", columnDefinition = "CHAR(36)")
    private String creadoPor;

    /** NNA afectados. Cascada total y orphanRemoval: el agregado gobierna su ciclo. */
    @OneToMany(mappedBy = "requerimiento", cascade = CascadeType.ALL, orphanRemoval = true,
            fetch = FetchType.LAZY)
    private List<RequerimientoNnaEntity> nnas = new ArrayList<>();

    /** Anexos (solo metadatos). */
    @OneToMany(mappedBy = "requerimiento", cascade = CascadeType.ALL, orphanRemoval = true,
            fetch = FetchType.LAZY)
    private List<RequerimientoAnexoEntity> anexos = new ArrayList<>();

    /** Historial de decisiones de admisibilidad. */
    @OneToMany(mappedBy = "requerimiento", cascade = CascadeType.ALL, orphanRemoval = true,
            fetch = FetchType.LAZY)
    private List<AdmisibilidadEntity> admisibilidades = new ArrayList<>();

    public void agregarNna(RequerimientoNnaEntity nna) {
        nna.setRequerimiento(this);
        nnas.add(nna);
    }

    public void agregarAnexo(RequerimientoAnexoEntity anexo) {
        anexo.setRequerimiento(this);
        anexos.add(anexo);
    }

    public void agregarAdmisibilidad(AdmisibilidadEntity admisibilidad) {
        admisibilidad.setRequerimiento(this);
        admisibilidades.add(admisibilidad);
    }
}
