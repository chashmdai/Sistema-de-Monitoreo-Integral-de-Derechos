package cl.smid.esnna.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Registro en memoria de jobs de /procesar.
 *
 * Decisión deliberada: NO es una tabla. El servicio corre en una instancia
 * (Windows Server + NSSM); el estado del job es transitorio por naturaleza y
 * el resultado durable de valor ya lo cubre el DraftStore por loteId. Si el
 * servicio se reinicia a mitad de un job, el polling recibe 404 y el frontend
 * re-somete: el costo es re-pagar un lote en vuelo durante un redeploy, que el
 * shutdown graceful del executor minimiza. Persistir estado de progreso en DB
 * para ese único escenario es la sobreingeniería que el diseño ya descartó.
 *
 * Índice activosPorClave (owner|loteId): dos submits del mismo lote por el
 * mismo usuario mientras el primero corre devuelven EL MISMO jobId — mata el
 * doble pago por doble click, que el draft (escrito recién al final) no cubre.
 */
@Component
public class ProcesoJobStore {

    private static final Logger log = LoggerFactory.getLogger(ProcesoJobStore.class);

    private final ConcurrentMap<String, ProcesoJob> porId = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, ProcesoJob> activosPorClave = new ConcurrentHashMap<>();

    @Value("${esnna.jobs.ttl-minutos:30}")
    private long ttlTerminadosMinutos;

    @Value("${esnna.jobs.ttl-activos-minutos:120}")
    private long ttlActivosMinutos;

    public Optional<ProcesoJob> porId(String jobId) {
        return Optional.ofNullable(porId.get(jobId));
    }

    /**
     * Registra el job; si ya hay uno activo para la misma clave, devuelve el
     * existente y descarta el nuevo (resolución atómica de la carrera de
     * submits concurrentes).
     */
    public ProcesoJob registrar(ProcesoJob job, String claveIdempotencia) {
        ProcesoJob efectivo = activosPorClave.compute(claveIdempotencia, (k, actual) ->
                (actual != null && !actual.estado().terminal()) ? actual : job);
        if (efectivo == job) {
            porId.put(job.jobId(), job);
        }
        return efectivo;
    }

    /** Registra un job ya terminal (lote cacheado en draft) solo para polling consistente. */
    public void registrarTerminal(ProcesoJob job) {
        porId.put(job.jobId(), job);
    }

    /** Libera la clave de idempotencia al terminar; el resultado queda en el draft. */
    public void liberarClave(String claveIdempotencia, ProcesoJob job) {
        activosPorClave.remove(claveIdempotencia, job);
    }

    public static String clave(String ownerSub, String loteId) {
        return (ownerSub != null ? ownerSub : "anon") + "|" + loteId;
    }

    @Scheduled(fixedDelayString = "${esnna.jobs.limpieza-ms:300000}")
    void limpiar() {
        LocalDateTime ahora = LocalDateTime.now();
        porId.values().removeIf(job -> {
            boolean terminal = job.estado().terminal();
            long edadMin = java.time.Duration.between(job.actualizadoEn(), ahora).toMinutes();
            if (terminal && edadMin >= ttlTerminadosMinutos) {
                return true;
            }
            if (!terminal && edadMin >= ttlActivosMinutos) {
                // Defensa ante un hilo colgado más allá de todo timeout interno:
                // el job se marca fallido y cae en el TTL normal del próximo ciclo.
                log.error("Job {} excedió {} min sin avance; se marca FALLIDO.", job.jobId(), ttlActivosMinutos);
                job.fallar("TIMEOUT_INTERNO", "El análisis excedió el tiempo máximo interno.");
                liberarClave(clave(job.ownerSub(), job.loteId()), job);
            }
            return false;
        });
    }
}