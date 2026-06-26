package cl.smid.antecedentes.api.dto;

import cl.smid.antecedentes.dominio.modelo.Calificacion;
import cl.smid.antecedentes.dominio.modelo.Criterio;
import cl.smid.antecedentes.dominio.modelo.EstadoFicha;
import cl.smid.antecedentes.dominio.modelo.PercepcionHallazgo;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;
import java.util.List;
import java.util.Set;

/**
 * Detalle completo de una ficha. Incluye el {@code relato} en claro (solo en el detalle, a
 * usuarios con acceso territorial). Los identificadores de referencia son alt_key (UUID); la PK
 * interna nunca se expone.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record FichaDetalleResponse(
        String altKey,
        String folio,
        EstadoFicha estado,
        String unidadAlt,
        String sedeAlt,
        String profesionalAlt,
        String jefaturaAlt,
        String procesoId,
        String casoAlt,
        String categoriaPrincipalId,
        List<String> categoriasSecundariasIds,
        List<Integer> derechosCdn,
        String descripcion,
        String relato,
        Calificacion calificacion,
        Set<Criterio> criterios,
        PercepcionHallazgo percepcionHallazgo,
        String hallazgoAlt,
        List<DocumentoResponse> documentos,
        List<HistorialResponse> historial,
        Instant creadoEn,
        Instant actualizadoEn) {
}
