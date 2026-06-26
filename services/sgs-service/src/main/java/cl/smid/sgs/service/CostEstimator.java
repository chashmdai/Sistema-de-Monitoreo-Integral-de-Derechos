package cl.smid.sgs.service;

import cl.smid.sgs.config.OpenAiProperties;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

/** Estima costo a partir de tokens y tarifas configurables. Devuelve null si no hay tarifas. */
@Component
public class CostEstimator {

    private final OpenAiProperties props;
    public CostEstimator(OpenAiProperties props) { this.props = props; }

    public BigDecimal estimar(Integer tokensPrompt, Integer tokensCompletion) {
        BigDecimal in = props.getCostPromptPer1k();
        BigDecimal out = props.getCostCompletionPer1k();
        if ((in == null || in.signum() == 0) && (out == null || out.signum() == 0)) return null;
        BigDecimal p = BigDecimal.valueOf(tokensPrompt == null ? 0 : tokensPrompt);
        BigDecimal c = BigDecimal.valueOf(tokensCompletion == null ? 0 : tokensCompletion);
        BigDecimal mil = BigDecimal.valueOf(1000);
        BigDecimal costo = p.divide(mil, 6, RoundingMode.HALF_UP).multiply(in == null ? BigDecimal.ZERO : in)
                .add(c.divide(mil, 6, RoundingMode.HALF_UP).multiply(out == null ? BigDecimal.ZERO : out));
        return costo.setScale(6, RoundingMode.HALF_UP);
    }
}
