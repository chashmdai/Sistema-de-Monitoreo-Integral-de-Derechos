package cl.smid.sgs.dto.out;

import java.time.LocalDateTime;
import java.util.List;

public record OficioDetalleDTO(
        Long id,
        String nroOficio,
        String region,
        String institucion,
        String residenciaCentro,
        String nivel,
        String pdfHash,
        LocalDateTime fechaIngreso,
        LocalDateTime fechaActualizacion,
        List<RecomendacionDetalleDTO> recomendaciones
) {}
