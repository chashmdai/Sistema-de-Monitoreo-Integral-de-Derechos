package cl.smid.antecedentes.infraestructura.persistencia;

import cl.smid.antecedentes.dominio.modelo.EstadoHallazgo;
import cl.smid.antecedentes.dominio.modelo.Temporalidad;
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

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Entidad JPA del agregado raiz {@link cl.smid.antecedentes.dominio.modelo.Hallazgo}.
 * {@code instrumento_id} es FK BIGINT (nullable a nivel de esquema; el dominio exige
 * instrumento al proponer). El mapeador traduce instrumento alt_key&lt;-&gt;id. Las listas de
 * unidades e instituciones son {@code Set} (evita MultipleBagFetch) en colecciones hijas.
 */
@Entity
@Table(name = "hallazgo")
@Getter
@Setter
@NoArgsConstructor
public class HallazgoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "alt_key", nullable = false, updatable = false, length = 36)
    private String altKey;

    @Column(name = "folio", nullable = false, updatable = false, length = 40)
    private String folio;

    @Column(name = "titulo", nullable = false, length = 255)
    private String titulo;

    @Column(name = "descripcion", nullable = false, length = 4000)
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoHallazgo estado;

    @Column(name = "instrumento_id")
    private Long instrumentoId;

    @Enumerated(EnumType.STRING)
    @Column(name = "temporalidad", nullable = false, length = 20)
    private Temporalidad temporalidad;

    @Column(name = "origen_ficha_alt", length = 36)
    private String origenFichaAlt;

    @Column(name = "creado_en", nullable = false, updatable = false)
    private LocalDateTime creadoEn;

    @Column(name = "actualizado_en", nullable = false)
    private LocalDateTime actualizadoEn;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "hallazgo_unidad", joinColumns = @JoinColumn(name = "hallazgo_id"))
    @Column(name = "unidad_alt", nullable = false, length = 36)
    private Set<String> unidadesInvolucradas = new LinkedHashSet<>();

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "hallazgo_institucion", joinColumns = @JoinColumn(name = "hallazgo_id"))
    @Column(name = "institucion_alt", nullable = false, length = 36)
    private Set<String> institucionesExternas = new LinkedHashSet<>();
}
