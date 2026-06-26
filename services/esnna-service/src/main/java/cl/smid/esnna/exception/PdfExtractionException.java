package cl.smid.esnna.exception;

/**
 * Falla en lectura, parseo o saneamiento de un PDF.
 * PRIV-2: el mensaje SOLO debe contener metadato (nombre de archivo, página),
 * nunca contenido extraído del documento.
 */
public class PdfExtractionException extends EsnnaException {

    public PdfExtractionException(String message) {
        super(message);
    }

    public PdfExtractionException(String message, Throwable cause) {
        super(message, cause);
    }
}
