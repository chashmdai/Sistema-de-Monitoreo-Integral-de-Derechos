package cl.smid.sgs.dto.out;

import cl.smid.sgs.enums.TipoAlerta;

import java.time.LocalDate;

public record AlertaDTO(
        Long id,
        Long recomendacionId,
        String nroOficio,
        String correlativo,
        TipoAlerta tipo,
        LocalDate fechaLimite,
        long diasRestantes,
        boolean vencida,
        boolean gv
) {}
