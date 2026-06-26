package cl.smid.instituciones.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;

/**
 * Representación pública de un tipo de institución.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record TipoInstitucionDTO(
        String altKey,
        String nombre,
        String ambito,
        String descripcion,
        boolean vigente,
        Instant creadoEn,
        Instant actualizadoEn) {
}
