package cl.smid.antecedentes.api.dto;

import cl.smid.antecedentes.dominio.modelo.EstadoHallazgo;
import cl.smid.antecedentes.dominio.modelo.Temporalidad;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;
import java.util.List;

/**
 * Detalle/elemento de listado de un hallazgo. {@code instrumentoId} transporta el alt_key del
 * instrumento.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record HallazgoResponse(
        String altKey,
        String folio,
        String titulo,
        String descripcion,
        EstadoHallazgo estado,
        String instrumentoId,
        Temporalidad temporalidad,
        List<String> unidadesInvolucradas,
        List<String> institucionesExternas,
        String origenFichaAlt,
        Instant creadoEn,
        Instant actualizadoEn) {
}
