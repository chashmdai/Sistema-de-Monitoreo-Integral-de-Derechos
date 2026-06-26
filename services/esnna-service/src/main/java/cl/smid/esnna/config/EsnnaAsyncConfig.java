package cl.smid.esnna.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * Executors del flujo asíncrono de /procesar (ERR-6 / RES-6).
 *
 *  - esnnaJobExecutor: corre el pipeline completo de un lote. Concurrencia
 *    deliberadamente baja (gpt-5.5 high es caro) y cola corta con AbortPolicy:
 *    cola llena => RejectedExecutionException => 429 CAPACIDAD_EXCEDIDA, en vez
 *    de acumular trabajo invisible.
 *  - esnnaMapExecutor: paraleliza la Fase 1 dentro de un job. Con 30 documentos
 *    secuenciales la fase MAP sola tomaba minutos; con paralelismo 3 el tiempo
 *    se divide sin presionar el rate limit del tier institucional. Pool
 *    compartido entre jobs (jobs y MAP corren en pools distintos: no hay
 *    espera circular posible).
 *
 * Shutdown graceful: se da tiempo a los jobs en vuelo antes de matar el proceso
 * (NSSM envía stop en cada redeploy); un job interrumpido no deja draft y el
 * lote se re-paga.
 *
 * @EnableScheduling habilita la limpieza periódica de ProcesoJobStore.
 */
@Configuration
@EnableScheduling
public class EsnnaAsyncConfig {

    public static final String JOB_EXECUTOR = "esnnaJobExecutor";
    public static final String MAP_EXECUTOR = "esnnaMapExecutor";

    @Bean(JOB_EXECUTOR)
    public ThreadPoolTaskExecutor esnnaJobExecutor(
            @Value("${esnna.jobs.concurrencia:2}") int concurrencia,
            @Value("${esnna.jobs.cola:10}") int cola) {
        ThreadPoolTaskExecutor ex = new ThreadPoolTaskExecutor();
        ex.setCorePoolSize(concurrencia);
        ex.setMaxPoolSize(concurrencia);
        ex.setQueueCapacity(cola);
        ex.setThreadNamePrefix("esnna-job-");
        ex.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        ex.setWaitForTasksToCompleteOnShutdown(true);
        ex.setAwaitTerminationSeconds(60);
        ex.initialize();
        return ex;
    }

    @Bean(MAP_EXECUTOR)
    public ThreadPoolTaskExecutor esnnaMapExecutor(
            @Value("${esnna.map.paralelismo:3}") int paralelismo) {
        ThreadPoolTaskExecutor ex = new ThreadPoolTaskExecutor();
        ex.setCorePoolSize(paralelismo);
        ex.setMaxPoolSize(paralelismo);
        ex.setQueueCapacity(500);
        ex.setThreadNamePrefix("esnna-map-");
        ex.setWaitForTasksToCompleteOnShutdown(true);
        ex.setAwaitTerminationSeconds(60);
        ex.initialize();
        return ex;
    }
}