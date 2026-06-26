package cl.smid.antecedentes.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Documento asociado a la ficha (metadato).
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record DocumentoResponse(String altKey, String nombre, String referenciaExterna) {
}
