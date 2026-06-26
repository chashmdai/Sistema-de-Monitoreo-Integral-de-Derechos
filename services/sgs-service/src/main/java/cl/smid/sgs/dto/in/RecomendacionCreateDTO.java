package cl.smid.sgs.dto.in;

import cl.smid.sgs.enums.PlazoRecomendacion;
import cl.smid.sgs.enums.RespuestaSiNo;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

/** Recomendación revisada por el analista antes de persistir. materia/categoria resueltas a FK de catálogo. */
public record RecomendacionCreateDTO(
        String correlativo,
        String dimension,
        @NotBlank String nudoCritico,
        String tipoRecomendacion,
        String verbo,
        String descripcion,
        PlazoRecomendacion plazo,
        String plazoRaw,
        boolean gv,
        RespuestaSiNo acoge,
        Long materiaId,
        Long categoriaId,
        String profesionalResponsable,
        String responsableSeguimiento,
        List<AccionDTO> acciones
) {}
