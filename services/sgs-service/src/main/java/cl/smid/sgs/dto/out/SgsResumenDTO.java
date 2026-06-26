package cl.smid.sgs.dto.out;

import cl.smid.sgs.enums.EstadoGestion;
import cl.smid.sgs.enums.EvaluacionCumplimiento;
import cl.smid.sgs.enums.PlazoRecomendacion;

import java.time.LocalDateTime;

/** Fila del tablero (proyección, NO la entity) — CTRL-2/DTO-1. */
public record SgsResumenDTO(
        Long oficioId,
        Long recomendacionId,
        String nroOficio,
        String region,
        String institucion,
        String dimension,
        String correlativo,
        PlazoRecomendacion plazo,
        boolean gv,
        EstadoGestion estado,
        EvaluacionCumplimiento ultimaEvaluacion,
        boolean requiereRevisionHumana,
        LocalDateTime fechaIngreso
) {}
