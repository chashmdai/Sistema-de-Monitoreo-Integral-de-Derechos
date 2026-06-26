package cl.smid.esnna.dto;

import java.math.BigDecimal;

/**
 * Consumo de tokens y costo estimado de una corrida (observabilidad de gasto).
 * reasoning importa con gpt-5.5 en 'high': son tokens invisibles que sí se pagan.
 */
public record UsageDTO(
        Integer tokensPrompt,
        Integer tokensCompletion,
        Integer tokensReasoning,
        BigDecimal costoEstimado
) {}
