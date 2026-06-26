package cl.smid.requerimientos.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import cl.smid.requerimientos.dominio.modelo.Complejidad;
import cl.smid.requerimientos.dominio.modelo.EstadoRequerimiento;
import cl.smid.requerimientos.dominio.modelo.Urgencia;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

/**
 * Proyección liviana de un requerimiento para los listados. Evita exponer NNA, snapshots y
 * relatos en vistas de colección.
 *
 * @param altKey                 identificador público
 * @param folio                  folio oficial
 * @param estado                 estado actual
 * @param idUnidadDestino        alt_key de la unidad de destino (nulable)
 * @param urgencia               urgencia (nulable)
 * @param complejidad            complejidad (nulable)
 * @param requiereFichaReservada bandera FIR
 * @param creadoEn               instante de creación
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Resumen publico de requerimiento para listados.")
public record ResumenRequerimientoDTO(
        @Schema(description = "Identificador publico opaco.", example = "1c2d3e4f-5a6b-7c8d-9e0f-1a2b3c4d5e6f")
        String altKey,
        @Schema(example = "RM-1/2027")
        String folio,
        @Schema(allowableValues = {"BORRADOR", "INGRESADO", "EN_ADMISIBILIDAD", "INADMISIBLE", "RESPONDIDO", "ASIGNADO"}, example = "INGRESADO")
        EstadoRequerimiento estado,
        @Schema(description = "altKey de unidad destino.", example = "5f1d2c8e-1d4a-4a1e-9b2c-0e7a1b2c3d4e", nullable = true)
        String idUnidadDestino,
        @Schema(example = "AMARILLO", nullable = true)
        Urgencia urgencia,
        @Schema(example = "MEDIANA", nullable = true)
        Complejidad complejidad,
        @Schema(example = "true")
        boolean requiereFichaReservada,
        @Schema(example = "2027-04-10T09:00:00Z")
        Instant creadoEn) {
}
