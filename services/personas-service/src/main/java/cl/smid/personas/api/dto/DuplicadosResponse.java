package cl.smid.personas.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

/**
 * Respuesta de la prevalidación de duplicados ({@code POST /personas/buscar-duplicados}).
 * Combina la coincidencia exacta por RUT (si la hay) con la lista —ya ordenada por score
 * descendente— de coincidencias probables por similitud de nombre/fecha.
 *
 * <p>{@code coincidenciaExacta} es nula cuando no existe match por RUT; el cliente debe
 * tratarla como opcional.</p>
 *
 * @param coincidenciaExacta     match por RUT, o {@code null}
 * @param coincidenciasProbables candidatas difusas (lista posiblemente vacía)
 */
@Schema(description = "Resultado de prevalidacion de duplicados.")
public record DuplicadosResponse(
        @Schema(description = "Coincidencia exacta por RUT, o null si no existe.", nullable = true)
        CoincidenciaExactaDTO coincidenciaExacta,
        @Schema(description = "Coincidencias probables ordenadas por score descendente.")
        List<CoincidenciaProbableDTO> coincidenciasProbables
) {
}
