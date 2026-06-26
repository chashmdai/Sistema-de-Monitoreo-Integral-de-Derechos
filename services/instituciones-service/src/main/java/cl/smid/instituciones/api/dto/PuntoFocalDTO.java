package cl.smid.instituciones.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;

/**
 * Representación pública de un punto focal.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record PuntoFocalDTO(
        String altKey,
        String institucionAlt,
        String nombre,
        String cargo,
        String email,
        String telefono,
        boolean principal,
        boolean activo,
        Instant creadoEn,
        Instant actualizadoEn) {
}
