package cl.smid.instituciones.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;

/**
 * Representación pública resumida de una institución para listados (sin puntos focales).
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record InstitucionResumenDTO(
        String altKey,
        String codigo,
        String nombre,
        String tipoAlt,
        String tipoNombre,
        String ambito,
        String rut,
        String regionCodigo,
        String comunaCodigo,
        boolean activa,
        Instant creadoEn,
        Instant actualizadoEn) {
}
