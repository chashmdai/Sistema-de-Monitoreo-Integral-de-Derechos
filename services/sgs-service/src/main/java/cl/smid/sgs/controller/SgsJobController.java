package cl.smid.sgs.controller;

import cl.smid.sgs.dto.out.JobEstadoDTO;
import cl.smid.sgs.service.JobService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Polling del estado de un job asíncrono (decisión #5). */
@RestController
@RequestMapping("/api/sgs")
public class SgsJobController {

    private final JobService jobService;
    public SgsJobController(JobService jobService) { this.jobService = jobService; }

    @GetMapping("/jobs/{jobId}")
    public JobEstadoDTO estado(@PathVariable String jobId) {
        return jobService.get(jobId);
    }
}
