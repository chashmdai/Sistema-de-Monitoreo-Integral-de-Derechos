package cl.smid.sgs.entity;

import cl.smid.sgs.enums.EvaluacionCumplimiento;
import cl.smid.sgs.enums.TipoAnalisis;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/** Registro inmutable de auditoría. Una entrada por análisis persistido (al guardar). Espejo EsnnaAnalisisAudit. */
@Entity
@Table(name = "sgs_analisis_audit", indexes = {
        @Index(name = "ix_audit_oficio", columnList = "oficio_id"),
        @Index(name = "ix_audit_usuario", columnList = "usuario_rut")
})
@Getter
@NoArgsConstructor
public class SgsAnalisisAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "oficio_id")
    private Long oficioId;

    @Column(name = "recomendacion_id")
    private Long recomendacionId;

    @Column(name = "usuario_rut", length = 50, nullable = false)
    private String usuarioRut;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_analisis", length = 20, nullable = false)
    private TipoAnalisis tipoAnalisis;

    @Column(name = "modelo", length = 80)
    private String modelo;

    @Column(name = "modelo_snapshot", length = 120)
    private String modeloSnapshot;

    @Column(name = "version_rubrica", length = 80)
    private String versionRubrica;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "sgs_audit_documento", joinColumns = @JoinColumn(name = "audit_id"))
    private List<DocumentoHash> documentos = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "evaluacion_ia", length = 60)
    private EvaluacionCumplimiento evaluacionIa;

    @Column(name = "confianza_ia")
    private Double confianzaIa;

    @Column(name = "tokens_prompt")
    private Integer tokensPrompt;

    @Column(name = "tokens_completion")
    private Integer tokensCompletion;

    @Column(name = "tokens_reasoning")
    private Integer tokensReasoning;

    @Column(name = "costo_estimado", precision = 12, scale = 6)
    private BigDecimal costoEstimado;

    @Builder
    public SgsAnalisisAudit(Long oficioId, Long recomendacionId, String usuarioRut, LocalDateTime timestamp,
                            TipoAnalisis tipoAnalisis, String modelo, String modeloSnapshot, String versionRubrica,
                            List<DocumentoHash> documentos, EvaluacionCumplimiento evaluacionIa, Double confianzaIa,
                            Integer tokensPrompt, Integer tokensCompletion, Integer tokensReasoning, BigDecimal costoEstimado) {
        this.oficioId = oficioId;
        this.recomendacionId = recomendacionId;
        this.usuarioRut = usuarioRut;
        this.timestamp = timestamp != null ? timestamp : LocalDateTime.now();
        this.tipoAnalisis = tipoAnalisis;
        this.modelo = modelo;
        this.modeloSnapshot = modeloSnapshot;
        this.versionRubrica = versionRubrica;
        this.documentos = documentos != null ? documentos : new ArrayList<>();
        this.evaluacionIa = evaluacionIa;
        this.confianzaIa = confianzaIa;
        this.tokensPrompt = tokensPrompt;
        this.tokensCompletion = tokensCompletion;
        this.tokensReasoning = tokensReasoning;
        this.costoEstimado = costoEstimado;
    }
}
