package cl.smid.sgs.dto.internal;

import cl.smid.sgs.enums.EstadoGestion;
import cl.smid.sgs.enums.EvaluacionCumplimiento;
import cl.smid.sgs.enums.FaseSeguimiento;
import cl.smid.sgs.enums.PlazoRecomendacion;

import java.time.LocalDate;
import java.time.LocalDateTime;

/** Proyección plana para el Excel: una fila por Seguimiento (decisión #16). JOIN Oficio▸Recomendacion▸Seguimiento. */
public record ExportRowDTO(
        String nroOficio,
        String region,
        String institucion,
        String residenciaCentro,
        String nivel,
        String correlativo,
        String dimension,
        String materia,
        String categoria,
        String nudoCritico,
        String tipoRecomendacion,
        String verbo,
        String descripcion,
        PlazoRecomendacion plazo,
        boolean gv,
        EstadoGestion estado,
        FaseSeguimiento fase,
        LocalDate fechaSeguimiento,
        String tipoSeguimiento,
        LocalDate fechaRespuesta,
        String tipoRespuesta,
        EvaluacionCumplimiento evaluacionIA,
        EvaluacionCumplimiento evaluacionFinal,
        String evaluacionFinalAutor,
        Double confianza,
        boolean requiereRevisionHumana,
        String valoracionRubrica,
        String responsableSeguimiento,
        LocalDateTime fechaIngreso
) {}
