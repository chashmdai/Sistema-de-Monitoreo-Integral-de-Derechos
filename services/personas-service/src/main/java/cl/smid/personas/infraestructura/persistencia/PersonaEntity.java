package cl.smid.personas.infraestructura.persistencia;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad JPA de la tabla {@code persona}. Vive en la infraestructura: usa Lombok y anotaciones
 * de persistencia, a diferencia del agregado de dominio {@code Persona}, que es puro.
 *
 * <p>El mapeo respeta {@code ddl-auto=validate}: los {@code columnDefinition} se alinean al
 * esquema creado por Flyway (CHAR(36) para claves alternas, DATETIME(6) para marcas temporales,
 * VARCHAR para los enums modelados como texto), idéntico al criterio de auth/catálogo.</p>
 */
@Entity
@Table(name = "persona")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonaEntity {

    /** PK interna (BIGINT). Nunca cruza la frontera de la API. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Identificador público opaco (UUID). */
    @Column(name = "alt_key", nullable = false, columnDefinition = "CHAR(36)")
    private String altKey;

    /** Tipo de persona (NNA | ADULTO | JURIDICA | TESTIGO) como texto. */
    @Column(name = "tipo", nullable = false, length = 20)
    private String tipo;

    /** RUT canónico "cuerpo-DV" o nulo. */
    @Column(name = "rut", length = 12)
    private String rut;

    /** Dígito verificador (mayúscula) o nulo. */
    @Column(name = "dv", columnDefinition = "CHAR(1)")
    private String dv;

    @Column(name = "nombres", length = 160)
    private String nombres;

    @Column(name = "apellido_paterno", length = 120)
    private String apellidoPaterno;

    @Column(name = "apellido_materno", length = 120)
    private String apellidoMaterno;

    @Column(name = "razon_social", length = 200)
    private String razonSocial;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    /** Sexo (F | M | OTRO | NO_INFORMA) como texto, o nulo. */
    @Column(name = "sexo", length = 20)
    private String sexo;

    @Column(name = "nacionalidad", length = 60)
    private String nacionalidad;

    /** SHA-256 hex (64 chars) de la clave de deduplicación difusa. */
    @Column(name = "hash_dedup", length = 64)
    private String hashDedup;

    /** alt_key de la sede de origen (del token). */
    @Column(name = "id_sede", columnDefinition = "CHAR(36)")
    private String idSede;

    /** alt_key de la unidad de origen (del token). */
    @Column(name = "id_unidad", columnDefinition = "CHAR(36)")
    private String idUnidad;

    /** Borrado lógico: 1 vigente, 0 dado de baja (nunca borrado físico). */
    @Column(name = "vigente", nullable = false)
    private boolean vigente;

    @Column(name = "creado_en", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant creadoEn;

    @Column(name = "actualizado_en", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant actualizadoEn;

    /** alt_key del usuario creador (claim sub). */
    @Column(name = "creado_por", columnDefinition = "CHAR(36)")
    private String creadoPor;

    /** Contactos del agregado; cascada total y borrado de huérfanos. */
    @OneToMany(mappedBy = "persona", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Builder.Default
    private List<PersonaContactoEntity> contactos = new ArrayList<>();

    /** Reemplaza el conjunto de contactos manteniendo la coherencia de la relación bidireccional. */
    public void reemplazarContactos(List<PersonaContactoEntity> nuevos) {
        this.contactos.clear();
        if (nuevos != null) {
            for (PersonaContactoEntity c : nuevos) {
                c.setPersona(this);
                this.contactos.add(c);
            }
        }
    }
}
