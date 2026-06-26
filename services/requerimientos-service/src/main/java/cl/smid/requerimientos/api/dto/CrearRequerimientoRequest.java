package cl.smid.requerimientos.api.dto;

import cl.smid.requerimientos.dominio.modelo.CanalIngreso;
import cl.smid.requerimientos.dominio.modelo.Complejidad;
import cl.smid.requerimientos.dominio.modelo.Urgencia;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

/**
 * Petición de creación de un requerimiento en BORRADOR (USR.01). Todos los campos son opcionales
 * (flexibilidad de ingreso): la sede y la autoría se estampan desde el token, no del cliente.
 *
 * @param canal              canal de ingreso (nulable)
 * @param complejidad        complejidad inicial (nulable)
 * @param urgencia           urgencia inicial (nulable)
 * @param idUnidadDestinoAlt alt_key de la unidad de destino (nulable)
 * @param resumen            resumen (nulable; máx. 2000)
 * @param idRequirenteAlt    alt_key del requirente en personas-service (nulable)
 * @param esBeta             override de serie de folio: {@code true}=BETA, {@code false}=OFICIAL,
 *                           {@code null}=política por fecha
 */
@Schema(description = "Peticion de creacion de requerimiento en BORRADOR. Los campos son opcionales por flexibilidad de ingreso.")
public record CrearRequerimientoRequest(
        @Schema(allowableValues = {"WEB", "PRESENCIAL"}, example = "WEB", nullable = true)
        CanalIngreso canal,
        @Schema(allowableValues = {"BAJA", "MEDIANA", "ALTA", "FAST_TRACK"}, example = "MEDIANA", nullable = true)
        Complejidad complejidad,
        @Schema(allowableValues = {"VERDE", "AMARILLO", "ROJO"}, example = "AMARILLO", nullable = true)
        Urgencia urgencia,
        @Schema(description = "altKey de la unidad destino.", example = "5f1d2c8e-1d4a-4a1e-9b2c-0e7a1b2c3d4e", nullable = true)
        String idUnidadDestinoAlt,
        @Schema(example = "Posible vulneracion de derecho a la educacion.", nullable = true)
        @Size(max = 2000, message = "El resumen no puede superar los 2000 caracteres") String resumen,
        @Schema(description = "altKey del requirente en personas-service.", example = "9a8b7c6d-5e4f-3a2b-1c0d-9e8f7a6b5c4d", nullable = true)
        String idRequirenteAlt,
        @Schema(description = "Override de serie de folio; null aplica politica por fecha.", example = "false", nullable = true)
        Boolean esBeta) {
}
