package cl.smid.sgs.dto.out;

import cl.smid.sgs.enums.EvaluacionCumplimiento;
import cl.smid.sgs.enums.FaseSeguimiento;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record SeguimientoDetalleDTO(
        Long id,
        FaseSeguimiento fase,
        LocalDate fechaSeguimiento,
        String tipoSeguimiento,
        String tipoRespuesta,
        LocalDate fechaRespuesta,
        String otroSeguimientoInstitucional,
        EvaluacionCumplimiento evaluacionIA,
        String valoracionRubrica,
        Double confianza,
        String razonamiento,
        boolean requiereRevisionHumana,
        EvaluacionCumplimiento evaluacionFinal,
        String evaluacionFinalAutor,
        LocalDateTime evaluacionFinalFecha,
        String responsableSeguimiento
) {}
