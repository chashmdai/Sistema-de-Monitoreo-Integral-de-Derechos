package cl.smid.casos.infraestructura.persistencia;

import cl.smid.casos.dominio.modelo.Complejidad;
import cl.smid.casos.dominio.modelo.EstadoCaso;
import cl.smid.casos.dominio.modelo.SerieExpediente;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

/**
 * Entidad JPA del Caso. Vive en la capa de infraestructura: la PK numérica {@code id} es privada y
 * jamás se expone fuera del servicio. El mapeo dominio↔entidad es EXPLÍCITO
 * ({@code MapeadorCasoPersistencia}); esta clase no se serializa a la API.
 *
 * <p>Lombok se permite SOLO en entidades JPA y adaptadores (nunca en el dominio).</p>
 */
@Entity
@Table(name = "caso")
@Getter
@Setter
@NoArgsConstructor
public class CasoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "alt_key", nullable = false, unique = true, length = 36)
    private String altKey;

    @Column(name = "numero_expediente", nullable = false, unique = true, length = 32)
    private String numeroExpediente;

    // Componentes del número, persistidos para reconstruir el VO sin parsear la cadena.
    @Column(name = "codigo_sede", nullable = false, length = 8)
    private String codigoSede;

    @Enumerated(EnumType.STRING)
    @Column(name = "serie", nullable = false, length = 8)
    private SerieExpediente serie;

    @Column(name = "correlativo", nullable = false)
    private long correlativo;

    @Column(name = "anio", nullable = false)
    private int anio;

    /** Clave única de idempotencia: un único caso por requerimiento de origen. */
    @Column(name = "id_requerimiento_origen_alt", nullable = false, unique = true, length = 36)
    private String idRequerimientoOrigenAlt;

    @Column(name = "folio_requerimiento", length = 24)
    private String folioRequerimiento;

    @Column(name = "id_sede_alt", nullable = false, length = 36)
    private String idSedeAlt;

    @Column(name = "id_unidad_alt", length = 36)
    private String idUnidadAlt;

    @Column(name = "id_profesional_responsable_alt", length = 36)
    private String idProfesionalResponsableAlt;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoCaso estado;

    @Enumerated(EnumType.STRING)
    @Column(name = "complejidad", length = 12)
    private Complejidad complejidad;

    @Column(name = "requiere_ficha_reservada", nullable = false)
    private boolean requiereFichaReservada;

    @Column(name = "es_beta", nullable = false)
    private boolean esBeta;

    @Column(name = "abierto_en", nullable = false)
    private Instant abiertoEn;

    @Column(name = "cerrado_en")
    private Instant cerradoEn;

    @Column(name = "creado_en", nullable = false)
    private Instant creadoEn;

    @Column(name = "actualizado_en", nullable = false)
    private Instant actualizadoEn;

    @Column(name = "creado_por", length = 36)
    private String creadoPor;

    @Column(name = "vigente", nullable = false)
    private boolean vigente = true;
}
