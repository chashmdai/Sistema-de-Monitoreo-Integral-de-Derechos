package cl.smid.esnna.entity;

import cl.smid.esnna.domain.Semaforo;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * BIZ-3: registro inmutable de cada corrida de análisis que se persiste (al
 * guardar, decisión #7). Órgano público + datos de NNA + decisión asistida por
 * IA exige trazabilidad: qué documentos (hash, no contenido), qué modelos y
 * snapshots, qué versión del protocolo, qué usuario y cuánto costó.
 *
 * Sin @Setter: una vez creado, no se modifica. Sin relación bidireccional con
 * el caso (basta el id) para no contaminar el agregado de negocio.
 */
@Entity
@Table(name = "esnna_analisis_audit", indexes = {
        @Index(name = "idx_audit_caso", columnList = "caso_id"),
        @Index(name = "idx_audit_usuario", columnList = "usuario_rut"),
        @Index(name = "idx_audit_timestamp", columnList = "timestamp")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class EsnnaAnalisisAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "caso_id", nullable = false, updatable = false)
    private Long casoId;

    /** RUT del profesional, leído del 'sub' del JWT (CTRL-9). */
    @Column(name = "usuario_rut", length = 20, updatable = false)
    private String usuarioRut;

    @CreationTimestamp
    @Column(name = "timestamp", updatable = false, nullable = false)
    private LocalDateTime timestamp;

    @Column(name = "modelo_extraccion", length = 60, updatable = false)
    private String modeloExtraccion;

    @Column(name = "modelo_consolidacion", length = 60, updatable = false)
    private String modeloConsolidacion;

    @Column(name = "version_protocolo", length = 60, updatable = false)
    private String versionProtocolo;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "esnna_analisis_documentos",
            joinColumns = @JoinColumn(name = "audit_id"))
    @Builder.Default
    private List<DocumentoAnalizado> documentos = new ArrayList<>();

    /** Semáforo que computó el backend en esta corrida (inmutable, para calibración). */
    @Enumerated(EnumType.STRING)
    @Column(name = "semaforo_ia", length = 10, updatable = false)
    private Semaforo semaforoIa;

    @Column(name = "confianza_ia", updatable = false)
    private Double confianzaIa;

    // Observabilidad de costo. reasoning_tokens importa con gpt-5.5 en 'high'.
    @Column(name = "tokens_prompt", updatable = false)
    private Integer tokensPrompt;

    @Column(name = "tokens_completion", updatable = false)
    private Integer tokensCompletion;

    @Column(name = "tokens_reasoning", updatable = false)
    private Integer tokensReasoning;

    @Column(name = "costo_estimado", precision = 12, scale = 6, updatable = false)
    private BigDecimal costoEstimado;
}
