package cl.smid.sgs.dto.out;

import cl.smid.sgs.enums.EstadoGestion;
import cl.smid.sgs.enums.PlazoRecomendacion;
import cl.smid.sgs.enums.RespuestaSiNo;

import java.util.List;

public record RecomendacionDetalleDTO(
        Long id,
        String correlativo,
        String dimension,
        String nudoCritico,
        String tipoRecomendacion,
        String verbo,
        String descripcion,
        PlazoRecomendacion plazo,
        String plazoRaw,
        boolean gv,
        RespuestaSiNo acoge,
        EstadoGestion estado,
        String materia,
        String categoria,
        String profesionalResponsable,
        String responsableSeguimiento,
        boolean anulado,
        List<AccionOutDTO> acciones,
        List<SeguimientoDetalleDTO> seguimientos
) {}
