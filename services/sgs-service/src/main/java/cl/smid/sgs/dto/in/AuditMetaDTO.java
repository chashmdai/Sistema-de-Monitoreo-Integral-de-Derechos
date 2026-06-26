package cl.smid.sgs.dto.in;

import java.math.BigDecimal;
import java.util.List;

/**
 * Metadatos de auditoría que el frontend devuelve junto a la confirmación humana,
 * tomados del resultado del job (decisión #7: auditoría al guardar).
 */
public record AuditMetaDTO(
        String modelo,
        String modeloSnapshot,
        List<DocumentoHashDTO> documentos,
        Integer tokensPrompt,
        Integer tokensCompletion,
        Integer tokensReasoning,
        BigDecimal costoEstimado
) {}
