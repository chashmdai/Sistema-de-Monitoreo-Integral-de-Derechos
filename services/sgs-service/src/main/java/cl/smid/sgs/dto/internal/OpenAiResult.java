package cl.smid.sgs.dto.internal;

/** Resultado crudo del adapter OpenAI: contenido JSON + consumo. */
public record OpenAiResult(String content, OpenAiUsage usage) {}
