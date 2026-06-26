package cl.smid.antecedentes.api.dto;

import cl.smid.antecedentes.dominio.modelo.Calificacion;
import cl.smid.antecedentes.dominio.modelo.EstadoFicha;
import cl.smid.antecedentes.dominio.modelo.PercepcionHallazgo;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;

/**
 * Resumen de ficha para la bandeja/listado. No incluye el relato.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record FichaResumenResponse(
        String altKey,
        String folio,
        EstadoFicha estado,
        Calificacion calificacion,
        PercepcionHallazgo percepcionHallazgo,
        String unidadAlt,
        String sedeAlt,
        String casoAlt,
        Instant creadoEn,
        Instant actualizadoEn) {
}
