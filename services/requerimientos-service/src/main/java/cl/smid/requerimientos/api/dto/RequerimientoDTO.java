package cl.smid.requerimientos.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import cl.smid.requerimientos.dominio.modelo.CanalIngreso;
import cl.smid.requerimientos.dominio.modelo.Complejidad;
import cl.smid.requerimientos.dominio.modelo.EstadoRequerimiento;
import cl.smid.requerimientos.dominio.modelo.Urgencia;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.List;

/**
 * Representación pública completa de un requerimiento. Expone exclusivamente identificadores opacos
 * (alt_key); la PK interna nunca cruza la frontera (cierre IDOR, Núcleo 2.2).
 *
 * @param altKey                 identificador público
 * @param folio                  folio oficial
 * @param idSede                 alt_key de la sede
 * @param idUnidadDestino        alt_key de la unidad de destino (nulable)
 * @param estado                 estado actual
 * @param canal                  canal de ingreso (nulable)
 * @param complejidad            complejidad (nulable)
 * @param urgencia               urgencia (nulable)
 * @param requiereFichaReservada bandera FIR (costura 6.5)
 * @param idRequirente           alt_key del requirente (nulable)
 * @param requirente             snapshot del requirente (nulable)
 * @param resumen                resumen (nulable)
 * @param fechaIngreso           instante del ingreso (nulable)
 * @param esBeta                 si pertenece a la serie BETA
 * @param vigente                bandera de vigencia
 * @param creadoEn               instante de creación
 * @param actualizadoEn          instante de última modificación
 * @param nnas                   NNA afectados
 * @param anexos                 anexos (solo metadatos)
 * @param admisibilidades        historial de decisiones de admisibilidad
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Representacion publica completa de un requerimiento.")
public record RequerimientoDTO(
        @Schema(description = "Identificador publico opaco.", example = "1c2d3e4f-5a6b-7c8d-9e0f-1a2b3c4d5e6f")
        String altKey,
        @Schema(example = "RM-1/2027")
        String folio,
        @Schema(description = "altKey de sede, no id interno.", example = "3b2a1c0d-1111-2222-3333-444455556666")
        String idSede,
        @Schema(description = "altKey de unidad destino.", example = "5f1d2c8e-1d4a-4a1e-9b2c-0e7a1b2c3d4e", nullable = true)
        String idUnidadDestino,
        @Schema(allowableValues = {"BORRADOR", "INGRESADO", "EN_ADMISIBILIDAD", "INADMISIBLE", "RESPONDIDO", "ASIGNADO"}, example = "BORRADOR")
        EstadoRequerimiento estado,
        @Schema(example = "WEB", nullable = true)
        CanalIngreso canal,
        @Schema(example = "MEDIANA", nullable = true)
        Complejidad complejidad,
        @Schema(example = "AMARILLO", nullable = true)
        Urgencia urgencia,
        @Schema(example = "true")
        boolean requiereFichaReservada,
        @Schema(description = "altKey del requirente.", example = "9a8b7c6d-5e4f-3a2b-1c0d-9e8f7a6b5c4d", nullable = true)
        String idRequirente,
        @Schema(nullable = true)
        SnapshotDTO requirente,
        @Schema(example = "Posible vulneracion de derecho a la educacion.", nullable = true)
        String resumen,
        @Schema(example = "2027-04-10T09:00:00Z", nullable = true)
        Instant fechaIngreso,
        @Schema(example = "false")
        boolean esBeta,
        @Schema(example = "true")
        boolean vigente,
        @Schema(example = "2027-04-10T09:00:00Z")
        Instant creadoEn,
        @Schema(example = "2027-04-10T09:00:00Z")
        Instant actualizadoEn,
        @Schema(description = "NNA afectados.")
        List<NnaAfectadoDTO> nnas,
        @Schema(description = "Anexos como metadatos.")
        List<AnexoDTO> anexos,
        @Schema(description = "Historial de admisibilidad.")
        List<AdmisibilidadDTO> admisibilidades) {
}
