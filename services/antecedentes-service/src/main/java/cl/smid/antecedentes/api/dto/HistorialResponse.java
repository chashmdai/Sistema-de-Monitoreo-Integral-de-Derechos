package cl.smid.antecedentes.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;

/**
 * Asiento del historial del ciclo de vida de la ficha.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record HistorialResponse(String tipoEvento, String actorAlt, Instant ocurridoEn, String observacion) {
}
