package cl.smid.sgs.exception;

/** Fallo del motor GPT -> 502. Mensaje solo con metadato, nunca eco de contenido (PRIV-2). */
public class OpenAiException extends RuntimeException {
    public OpenAiException(String message) { super(message); }
    public OpenAiException(String message, Throwable cause) { super(message, cause); }
}
