package cl.smid.antecedentes.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;

/**
 * Elemento de una tabla de referencia (categoria/proceso/instrumento).
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ReferenciaResponse(
        String altKey,
        String codigo,
        String nombre,
        boolean vigente,
        Instant creadoEn,
        Instant actualizadoEn) {
}
