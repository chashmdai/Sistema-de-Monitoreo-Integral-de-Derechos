package cl.smid.sgs.dto.in;

import cl.smid.sgs.enums.RespuestaSiNo;

/** Edición parcial de gestión (PATCH). NO toca la extracción IA (SRV-2). Campos null = sin cambio. */
public record RecomendacionPatchDTO(
        RespuestaSiNo acoge,
        Long materiaId,
        Long categoriaId,
        String profesionalResponsable,
        String responsableSeguimiento
) {}
