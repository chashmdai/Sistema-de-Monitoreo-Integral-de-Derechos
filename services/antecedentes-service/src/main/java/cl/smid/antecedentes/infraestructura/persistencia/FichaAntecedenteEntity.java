package cl.smid.antecedentes.infraestructura.persistencia;

import cl.smid.antecedentes.dominio.modelo.Calificacion;
import cl.smid.antecedentes.dominio.modelo.Criterio;
import cl.smid.antecedentes.dominio.modelo.EstadoFicha;
import cl.smid.antecedentes.dominio.modelo.PercepcionHallazgo;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Entidad JPA del agregado raiz {@link cl.smid.antecedentes.dominio.modelo.FichaAntecedente}.
 *
 * <ul>
 *   <li>PK BIGINT interna nunca expuesta; identificador publico en {@code alt_key}.</li>
 *   <li>Referencias por PK escalar: {@code proceso_id}, {@code categoria_principal_id} y las
 *       secundarias ({@code categoria_id}) son FKs BIGINT a las tablas de referencia; el
 *       mapeador traduce alt_key&lt;-&gt;id. Sin asociaciones JPA bidireccionales.</li>
 *   <li>Enums como {@code VARCHAR} via {@code @Enumerated(STRING)} (con CHECK en Flyway).</li>
 *   <li>{@code relato_cifrado} guarda el relato cifrado AES-256-GCM (lo aplica el mapeador).</li>
 *   <li>Colecciones hijas como {@code @ElementCollection} (Hibernate gestiona su ciclo).</li>
 *   <li>Marcas {@code DATETIME(6)} UTC como {@link LocalDateTime}.</li>
 * </ul>
 */
@Entity
@Table(name = "ficha_antecedente")
@Getter
@Setter
@NoArgsConstructor
public class FichaAntecedenteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "alt_key", nullable = false, updatable = false, length = 36)
    private String altKey;

    @Column(name = "folio", nullable = false, updatable = false, length = 40)
    private String folio;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoFicha estado;

    @Column(name = "unidad_alt", nullable = false, updatable = false, length = 36)
    private String unidadAlt;

    @Column(name = "sede_alt", nullable = false, updatable = false, length = 36)
    private String sedeAlt;

    @Column(name = "profesional_alt", nullable = false, updatable = false, length = 36)
    private String profesionalAlt;

    @Column(name = "jefatura_alt", length = 36)
    private String jefaturaAlt;

    @Column(name = "proceso_id", nullable = false)
    private Long procesoId;

    @Column(name = "caso_alt", length = 36)
    private String casoAlt;

    @Column(name = "categoria_principal_id", nullable = false)
    private Long categoriaPrincipalId;

    @Column(name = "descripcion", nullable = false, length = 4000)
    private String descripcion;

    @JdbcTypeCode(SqlTypes.LONGVARCHAR)
    @Column(name = "relato_cifrado", nullable = false)
    private String relatoCifrado;

    @Enumerated(EnumType.STRING)
    @Column(name = "calificacion", nullable = false, length = 30)
    private Calificacion calificacion;

    @Enumerated(EnumType.STRING)
    @Column(name = "percepcion_hallazgo", nullable = false, length = 30)
    private PercepcionHallazgo percepcionHallazgo;

    @Column(name = "hallazgo_alt", length = 36)
    private String hallazgoAlt;

    @Column(name = "creado_en", nullable = false, updatable = false)
    private LocalDateTime creadoEn;

    @Column(name = "actualizado_en", nullable = false)
    private LocalDateTime actualizadoEn;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "ficha_categoria_secundaria", joinColumns = @JoinColumn(name = "ficha_id"))
    @Column(name = "categoria_id", nullable = false)
    private Set<Long> categoriasSecundariasIds = new LinkedHashSet<>();

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "ficha_derecho_cdn", joinColumns = @JoinColumn(name = "ficha_id"))
    @Column(name = "numero_articulo", nullable = false)
    private Set<Short> derechosCdn = new LinkedHashSet<>();

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "ficha_criterio", joinColumns = @JoinColumn(name = "ficha_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "criterio", nullable = false, length = 40)
    private Set<Criterio> criterios = new LinkedHashSet<>();

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "ficha_documento", joinColumns = @JoinColumn(name = "ficha_id"))
    private List<EmbebidosFicha.DocumentoEmbebido> documentos = new ArrayList<>();

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "ficha_historial", joinColumns = @JoinColumn(name = "ficha_id"))
    private List<EmbebidosFicha.HistorialEmbebido> historial = new ArrayList<>();
}
