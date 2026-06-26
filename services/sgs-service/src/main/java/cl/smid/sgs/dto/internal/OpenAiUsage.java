package cl.smid.sgs.dto.internal;

/** Consumo de tokens leído del 'usage' de OpenAI (GPT-4) para auditoría de costo. */
public record OpenAiUsage(
        Integer tokensPrompt,
        Integer tokensCompletion,
        Integer tokensReasoning
) {
    public static OpenAiUsage empty() { return new OpenAiUsage(0, 0, 0); }

    public OpenAiUsage plus(OpenAiUsage o) {
        if (o == null) return this;
        return new OpenAiUsage(
                nz(tokensPrompt) + nz(o.tokensPrompt),
                nz(tokensCompletion) + nz(o.tokensCompletion),
                nz(tokensReasoning) + nz(o.tokensReasoning));
    }
    private static int nz(Integer v) { return v == null ? 0 : v; }
}
