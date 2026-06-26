package cl.smid.sgs.dto.out;

import cl.smid.sgs.enums.JobStatus;
import cl.smid.sgs.enums.TipoAnalisis;

import java.time.LocalDateTime;

/** Cuerpo del 202 Accepted (decisión #5). */
public record JobAceptadoDTO(String jobId, TipoAnalisis tipo, JobStatus status, LocalDateTime createdAt) {}
