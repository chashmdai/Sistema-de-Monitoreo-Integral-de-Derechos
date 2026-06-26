package cl.smid.esnna.dto;

/**
 * Resultado de una llamada a la IA: el dato parseado + el consumo de tokens del
 * modelo, para observabilidad de costo (FEAT). tokensReasoning es subconjunto de
 * tokensCompletion (los razonadores lo facturan como output), por eso el costo se
 * calcula con prompt+completion, sin doble conteo.
 */
public record ResultadoIa<T>(
        T dato,
        String modelo,
        int tokensPrompt,
        int tokensCompletion,
        int tokensReasoning
) {}
