package cl.smid.instituciones.infraestructura.persistencia.entidad;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Entidad JPA de la tabla {@code institucion}. La relación con el tipo se modela por la
 * <strong>PK interna escalar</strong> {@code tipo_id} (override de diseño 6.10: sin
 * relación JPA bidireccional), que el mapeador/adaptador resuelve contra el alt_key del
 * tipo. El RUT se persiste descompuesto en {@code rut} (cuerpo) y {@code dv}.
 */
@Entity
@Table(name = "institucion")
@Getter
@Setter
@NoArgsConstructor
public class InstitucionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "alt_key", nullable = false, length = 36)
    private String altKey;

    @Column(name = "codigo", length = 60)
    private String codigo;

    @Column(name = "nombre", nullable = false, length = 200)
    private String nombre;

    @Column(name = "tipo_id", nullable = false)
    private Long tipoId;

    @Column(name = "rut", length = 12)
    private String rut;

    @Column(name = "dv", length = 1)
    private String dv;

    @Column(name = "region_codigo", length = 10)
    private String regionCodigo;

    @Column(name = "comuna_codigo", length = 10)
    private String comunaCodigo;

    @Column(name = "direccion", length = 240)
    private String direccion;

    @Column(name = "telefono", length = 40)
    private String telefono;

    @Column(name = "email", length = 160)
    private String email;

    @Column(name = "sitio_web", length = 200)
    private String sitioWeb;

    @Column(name = "activa", nullable = false)
    private boolean activa;

    @Column(name = "creado_en", nullable = false)
    private LocalDateTime creadoEn;

    @Column(name = "actualizado_en", nullable = false)
    private LocalDateTime actualizadoEn;
}
