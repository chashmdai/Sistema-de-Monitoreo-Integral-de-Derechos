package cl.smid.requerimientos.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import cl.smid.requerimientos.dominio.modelo.AccionAdmisibilidad;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

/**
 * Decisión de admisibilidad expuesta en la API.
 *
 * @param accion                   acción ejecutada
 * @param idCoordinador            alt_key del coordinador que decidió
 * @param escaladoADefensora       solo INADMISIBLE
 * @param idProfesionalAsignado    alt_key del profesional (solo ASIGNACION; nulable)
 * @param observacion              texto libre (nulable)
 * @param decididoEn               instante UTC de la decisión
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Decision de admisibilidad registrada.")
public record AdmisibilidadDTO(
        @Schema(allowableValues = {"INADMISIBLE", "RESPUESTA_INMEDIATA", "ASIGNACION"}, example = "ASIGNACION")
        AccionAdmisibilidad accion,
        @Schema(description = "altKey del coordinador.", example = "c3d4e5f6-1111-2222-3333-444455556666")
        String idCoordinador,
        @Schema(description = "Solo aplica a INADMISIBLE.", example = "false")
        boolean escaladoADefensora,
        @Schema(description = "altKey del profesional asignado, solo ASIGNACION.", example = "a7f8e9d0-1111-2222-3333-444455556666", nullable = true)
        String idProfesionalAsignado,
        @Schema(example = "Deriva a UPRJ", nullable = true)
        String observacion,
        @Schema(example = "2027-04-10T09:00:00Z")
        Instant decididoEn) {
}
