package cl.smid.requerimientos.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Anexo expuesto en la API (solo metadatos). La {@code referenciaExterna} permanece nula hasta que
 * exista el servicio de Documentos (6.9).
 *
 * @param nombreArchivo     nombre original
 * @param tipoMime          tipo MIME (nulable)
 * @param tamanoBytes       tamaño en bytes (nulable)
 * @param referenciaExterna referencia al binario en Documentos (nulable)
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Anexo expuesto como metadatos, sin binario.")
public record AnexoDTO(
        @Schema(example = "antecedente.pdf")
        String nombreArchivo,
        @Schema(example = "application/pdf", nullable = true)
        String tipoMime,
        @Schema(example = "245760", nullable = true)
        Long tamanoBytes,
        @Schema(description = "Referencia externa al binario, nula hasta servicio Documentos.", nullable = true)
        String referenciaExterna) {
}
