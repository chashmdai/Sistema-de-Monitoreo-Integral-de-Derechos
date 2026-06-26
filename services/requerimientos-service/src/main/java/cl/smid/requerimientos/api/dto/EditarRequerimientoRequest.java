package cl.smid.requerimientos.api.dto;

import cl.smid.requerimientos.dominio.modelo.CanalIngreso;
import cl.smid.requerimientos.dominio.modelo.Complejidad;
import cl.smid.requerimientos.dominio.modelo.Urgencia;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

/**
 * Petición de edición de un requerimiento (PUT, partial-merge): cada campo nulo significa
 * "no tocar". Solo surte efecto dentro de la ventana de mutabilidad.
 *
 * @param canal              nuevo canal (nulable)
 * @param complejidad        nueva complejidad (nulable)
 * @param urgencia           nueva urgencia (nulable)
 * @param idUnidadDestinoAlt nueva unidad de destino (nulable)
 * @param resumen            nuevo resumen (nulable; máx. 2000)
 * @param idRequirenteAlt    nuevo requirente (nulable)
 */
@Schema(description = "Peticion de edicion partial-merge. Campos nulos significan no tocar.")
public record EditarRequerimientoRequest(
        @Schema(allowableValues = {"WEB", "PRESENCIAL"}, example = "WEB", nullable = true)
        CanalIngreso canal,
        @Schema(allowableValues = {"BAJA", "MEDIANA", "ALTA", "FAST_TRACK"}, example = "MEDIANA", nullable = true)
        Complejidad complejidad,
        @Schema(allowableValues = {"VERDE", "AMARILLO", "ROJO"}, example = "AMARILLO", nullable = true)
        Urgencia urgencia,
        @Schema(description = "altKey de unidad destino.", example = "5f1d2c8e-1d4a-4a1e-9b2c-0e7a1b2c3d4e", nullable = true)
        String idUnidadDestinoAlt,
        @Schema(example = "Actualizacion de antecedentes del requerimiento.", nullable = true)
        @Size(max = 2000, message = "El resumen no puede superar los 2000 caracteres") String resumen,
        @Schema(description = "altKey del requirente.", example = "9a8b7c6d-5e4f-3a2b-1c0d-9e8f7a6b5c4d", nullable = true)
        String idRequirenteAlt) {
}
