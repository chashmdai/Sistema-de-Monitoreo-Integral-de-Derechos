package cl.smid.casos.infraestructura.persistencia;

import cl.smid.casos.dominio.modelo.EstadoCaso;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

/**
 * Entidad JPA del asiento de transición del historial del Caso. La relación con el caso se modela por
 * la columna {@code id_caso} (FK), sin navegación bidireccional para mantener el adaptador simple y
 * el control de inserciones explícito.
 */
@Entity
@Table(name = "caso_transicion", indexes = @Index(name = "idx_transicion_caso", columnList = "id_caso"))
@Getter
@Setter
@NoArgsConstructor
public class TransicionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "alt_key", nullable = false, unique = true, length = 36)
    private String altKey;

    @Column(name = "id_caso", nullable = false)
    private Long idCaso;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_origen", length = 20)
    private EstadoCaso estadoOrigen;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_destino", nullable = false, length = 20)
    private EstadoCaso estadoDestino;

    @Column(name = "accion", nullable = false, length = 32)
    private String accion;

    @Column(name = "observacion", length = 2000)
    private String observacion;

    @Column(name = "actor_alt", length = 36)
    private String actorAlt;

    @Column(name = "ocurrido_en", nullable = false)
    private Instant ocurridoEn;
}
