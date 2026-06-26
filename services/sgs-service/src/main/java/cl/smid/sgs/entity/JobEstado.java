package cl.smid.sgs.entity;

import cl.smid.sgs.enums.JobStatus;
import cl.smid.sgs.enums.TipoAnalisis;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/** Estado de un job de procesamiento asíncrono. payloadResultado = JSON del DTO de resultado (decisión #5). */
@Entity
@Table(name = "sgs_job")
@Getter
@Setter
@NoArgsConstructor
public class JobEstado {

    @Id
    @Column(name = "job_id", length = 36)
    private String jobId;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", length = 20, nullable = false)
    private TipoAnalisis tipo;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    private JobStatus status = JobStatus.ENCOLADO;

    @Lob
    @Column(name = "payload_resultado", columnDefinition = "LONGTEXT")
    private String payloadResultado;

    @Column(name = "error", columnDefinition = "TEXT")
    private String error;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
