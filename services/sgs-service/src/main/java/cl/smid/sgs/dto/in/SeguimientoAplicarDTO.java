package cl.smid.sgs.dto.in;

import cl.smid.sgs.enums.EvaluacionCumplimiento;
import cl.smid.sgs.enums.FaseSeguimiento;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

/**
 * Confirmación humana de una evaluación -> crea un Seguimiento (hito histórico).
 * Persiste la propuesta IA INMUTABLE + el override humano (MOD-2). Las sugerencias de catálogo se resuelven a FK.
 */
public record SeguimientoAplicarDTO(
        @NotNull Long recomendacionId,
        FaseSeguimiento fase,
        LocalDate fechaSeguimiento,
        Long tipoSeguimientoId,
        Long tipoRespuestaId,
        LocalDate fechaRespuesta,
        String otroSeguimientoInstitucional,
        // propuesta IA (se persiste inmutable como sustento)
        EvaluacionCumplimiento evaluacionIA,
        String valoracionRubrica,
        Double confianza,
        String razonamiento,
        // override humano
        @NotNull EvaluacionCumplimiento evaluacionFinal,
        String responsableSeguimiento
) {}
