package cl.smid.esnna.dto;

import cl.smid.esnna.domain.EstadoGestion;
import cl.smid.esnna.domain.Semaforo;
import cl.smid.esnna.domain.SexoNna;

import java.time.LocalDateTime;

/**
 * Proyección para el tablero (listado). Reemplaza la exposición de EsnnaEntity
 * (CTRL-1) y minimiza la PII en respuestas masivas: NO incluye nombre ni cédula
 * del NNA (eso vive en el detalle). Lo necesario para priorizar y ubicar el caso.
 */
public record EsnnaResumenDTO(
        Long id,
        String nroOficio,
        Semaforo semaforoFinal,
        Semaforo semaforoIa,
        String region,
        String delitoConcreto,
        Integer edad,
        SexoNna sexoNna,
        EstadoGestion estadoGestion,
        LocalDateTime fechaIngreso,
        LocalDateTime fechaActualizacion
) {}
