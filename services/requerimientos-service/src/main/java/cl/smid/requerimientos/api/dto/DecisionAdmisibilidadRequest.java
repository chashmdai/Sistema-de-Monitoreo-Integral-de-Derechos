package cl.smid.requerimientos.api.dto;

import cl.smid.requerimientos.dominio.modelo.AccionAdmisibilidad;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Petición de decisión de admisibilidad (USR.02): exactamente una de tres acciones disjuntas. La
 * obligatoriedad del profesional para ASIGNACION la valida el dominio (REQ-422).
 *
 * @param accion                   acción a ejecutar (obligatoria)
 * @param escaladoADefensora       solo aplica a INADMISIBLE
 * @param idProfesionalAsignadoAlt alt_key del profesional (obligatorio en ASIGNACION)
 * @param observacion              texto libre opcional (máx. 2000)
 */
@Schema(description = "Peticion de decision de admisibilidad. RESPUESTA_INMEDIATA registra, pero no envia comunicacion saliente.")
public record DecisionAdmisibilidadRequest(
        @Schema(allowableValues = {"INADMISIBLE", "RESPUESTA_INMEDIATA", "ASIGNACION"}, example = "ASIGNACION", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "La acción de admisibilidad es obligatoria") AccionAdmisibilidad accion,
        @Schema(description = "Solo aplica a INADMISIBLE.", example = "false")
        boolean escaladoADefensora,
        @Schema(description = "altKey del profesional, obligatorio para ASIGNACION.", example = "c3d4e5f6-1111-2222-3333-444455556666", nullable = true)
        String idProfesionalAsignadoAlt,
        @Schema(example = "Deriva a UPRJ", nullable = true)
        @Size(max = 2000, message = "La observación no puede superar los 2000 caracteres") String observacion) {
}
