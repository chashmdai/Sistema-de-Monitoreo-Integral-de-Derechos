package cl.smid.esnna.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Estima el costo en USD de una corrida a partir de los tokens. Precios por 1M
 * tokens configurables (cambian seguido). No suma reasoning aparte: ya viene
 * dentro de completion_tokens.
 */
@Component
public class CostoEstimador {

    private static final BigDecimal POR_MILLON = new BigDecimal("1000000");

    @Value("${esnna.openai.precio.extraccion-input:0.15}")
    private BigDecimal precioExtraccionInput;
    @Value("${esnna.openai.precio.extraccion-output:0.60}")
    private BigDecimal precioExtraccionOutput;
    @Value("${esnna.openai.precio.consolidacion-input:1.25}")
    private BigDecimal precioConsolidacionInput;
    @Value("${esnna.openai.precio.consolidacion-output:10.00}")
    private BigDecimal precioConsolidacionOutput;

    public BigDecimal estimarUsd(boolean esConsolidacion, int tokensPrompt, int tokensCompletion) {
        BigDecimal in = esConsolidacion ? precioConsolidacionInput : precioExtraccionInput;
        BigDecimal out = esConsolidacion ? precioConsolidacionOutput : precioExtraccionOutput;
        BigDecimal costoIn = in.multiply(BigDecimal.valueOf(tokensPrompt)).divide(POR_MILLON, 6, RoundingMode.HALF_UP);
        BigDecimal costoOut = out.multiply(BigDecimal.valueOf(tokensCompletion)).divide(POR_MILLON, 6, RoundingMode.HALF_UP);
        return costoIn.add(costoOut);
    }
}
