package cl.smid.sgs.service;

import cl.smid.sgs.dto.out.JobEstadoDTO;
import cl.smid.sgs.entity.JobEstado;
import cl.smid.sgs.enums.JobStatus;
import cl.smid.sgs.enums.TipoAnalisis;
import cl.smid.sgs.exception.SgsNotFoundException;
import cl.smid.sgs.repository.JobRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/** Ciclo de vida del job asíncrono (decisión #5). */
@Service
public class JobService {

    private static final Logger log = LoggerFactory.getLogger(JobService.class);

    private final JobRepository repo;
    private final ObjectMapper om;

    public JobService(JobRepository repo, ObjectMapper om) {
        this.repo = repo;
        this.om = om;
    }

    @Transactional
    public JobEstado crear(TipoAnalisis tipo) {
        JobEstado job = new JobEstado();
        job.setJobId(UUID.randomUUID().toString());
        job.setTipo(tipo);
        job.setStatus(JobStatus.ENCOLADO);
        return repo.save(job);
    }

    @Transactional
    public void marcarProcesando(String jobId) {
        repo.findById(jobId).ifPresent(j -> { j.setStatus(JobStatus.PROCESANDO); repo.save(j); });
    }

    @Transactional
    public void completar(String jobId, Object payload) {
        JobEstado j = repo.findById(jobId).orElseThrow(() -> new SgsNotFoundException("Job inexistente: " + jobId));
        try {
            j.setPayloadResultado(om.writeValueAsString(payload));
        } catch (Exception e) {
            log.error("No se pudo serializar el payload del job {}", jobId, e);
            j.setStatus(JobStatus.FALLIDO);
            j.setError("Error serializando el resultado.");
            repo.save(j);
            return;
        }
        j.setStatus(JobStatus.COMPLETADO);
        repo.save(j);
    }

    @Transactional
    public void fallar(String jobId, String error) {
        repo.findById(jobId).ifPresent(j -> { j.setStatus(JobStatus.FALLIDO); j.setError(error); repo.save(j); });
    }

    @Transactional(readOnly = true)
    public JobEstadoDTO get(String jobId) {
        JobEstado j = repo.findById(jobId).orElseThrow(() -> new SgsNotFoundException("Job inexistente: " + jobId));
        JsonNode payload = null;
        if (j.getPayloadResultado() != null) {
            try { payload = om.readTree(j.getPayloadResultado()); } catch (Exception ignored) {}
        }
        return new JobEstadoDTO(j.getJobId(), j.getTipo(), j.getStatus(), payload, j.getError(),
                j.getCreatedAt(), j.getUpdatedAt());
    }
}
