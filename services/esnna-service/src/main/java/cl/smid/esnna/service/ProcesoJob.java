package cl.smid.esnna.service;

import cl.smid.esnna.domain.EstadoJob;
import cl.smid.esnna.dto.EsnnaProcesoResultado;
import cl.smid.esnna.dto.ProcesoJobStatusDTO;

import java.time.LocalDateTime;

/**
 * Estado vivo de un job de /procesar. Escrito por el hilo del executor (y los
 * hilos MAP vía listener), leído por los hilos HTTP del polling: campos
 * volatile + snapshot inmutable. No requiere más coordinación porque cada
 * campo se publica de forma independiente y el DTO tolera lecturas
 * ligeramente desfasadas entre sí.
 */
public class ProcesoJob {

    private final String jobId;
    private final String loteId;
    private final String ownerSub;
    private final LocalDateTime creadoEn = LocalDateTime.now();

    private volatile EstadoJob estado = EstadoJob.EN_COLA;
    private volatile int procesados;
    private volatile int totalDocumentos;
    private volatile EsnnaProcesoResultado resultado;
    private volatile String errorCodigo;
    private volatile String errorMensaje;
    private volatile LocalDateTime actualizadoEn = LocalDateTime.now();

    public ProcesoJob(String jobId, String loteId, String ownerSub, int totalDocumentos) {
        this.jobId = jobId;
        this.loteId = loteId;
        this.ownerSub = ownerSub;
        this.totalDocumentos = totalDocumentos;
    }

    public void fase(EstadoJob nueva) {
        this.estado = nueva;
        touch();
    }

    public void avanceMap(int procesados, int total) {
        this.procesados = procesados;
        this.totalDocumentos = total;
        touch();
    }

    public void completar(EsnnaProcesoResultado resultado) {
        this.resultado = resultado;
        this.estado = EstadoJob.COMPLETADO;
        touch();
    }

    public void fallar(String codigo, String mensaje) {
        this.errorCodigo = codigo;
        this.errorMensaje = mensaje;
        this.estado = EstadoJob.FALLIDO;
        touch();
    }

    private void touch() {
        this.actualizadoEn = LocalDateTime.now();
    }

    public ProcesoJobStatusDTO snapshot() {
        EstadoJob est = this.estado;
        ProcesoJobStatusDTO.ProgresoDTO prog = est == EstadoJob.ANALIZANDO
                ? new ProcesoJobStatusDTO.ProgresoDTO(procesados, totalDocumentos)
                : null;
        ProcesoJobStatusDTO.JobErrorDTO err = est == EstadoJob.FALLIDO
                ? new ProcesoJobStatusDTO.JobErrorDTO(errorCodigo, errorMensaje)
                : null;
        return new ProcesoJobStatusDTO(jobId, est, prog,
                est == EstadoJob.COMPLETADO ? resultado : null,
                err, creadoEn, actualizadoEn);
    }

    public String jobId() {
        return jobId;
    }

    public String loteId() {
        return loteId;
    }

    public String ownerSub() {
        return ownerSub;
    }

    public EstadoJob estado() {
        return estado;
    }

    public LocalDateTime actualizadoEn() {
        return actualizadoEn;
    }
}