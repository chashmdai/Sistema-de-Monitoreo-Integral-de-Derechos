package cl.smid.sgs.exception;

/** Error de lectura/extracción de PDF. Mensaje solo con nombre de archivo, nunca contenido (PDF-1/PRIV-2). */
public class PdfExtractionException extends RuntimeException {
    public PdfExtractionException(String message) { super(message); }
    public PdfExtractionException(String message, Throwable cause) { super(message, cause); }
}
