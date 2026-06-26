package cl.smid.sgs.service;

import cl.smid.sgs.config.SgsProperties;
import cl.smid.sgs.exception.PdfExtractionException;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Extracción de texto con PDFBox 3.x (Loader.loadPDF). OCR es upstream total (decisión #15): este servicio
 * NO procesa imágenes. Si llega un scan sin capa de texto, la guarda fail-fast (PDF-4) lo rechaza.
 */
@Service
public class PdfExtractionService {

    private final SgsProperties props;

    public PdfExtractionService(SgsProperties props) { this.props = props; }

    /** Valida que los bytes parezcan un PDF por sus magic bytes (CTRL-10/PDF-5). */
    public boolean esPdf(byte[] bytes) {
        return bytes != null && bytes.length > 4
                && bytes[0] == '%' && bytes[1] == 'P' && bytes[2] == 'D' && bytes[3] == 'F';
    }

    /**
     * Extrae texto saneado. Lanza PdfExtractionException si el PDF es ilegible o no tiene capa de texto
     * suficiente (fail-fast antes de gastar GPT). Nunca incluye contenido en el mensaje (PRIV-2).
     */
    public String extractText(byte[] bytes, String nombreArchivo) {
        if (!esPdf(bytes)) {
            throw new PdfExtractionException("El archivo '" + nombreArchivo + "' no es un PDF válido.");
        }
        String texto;
        try (PDDocument document = Loader.loadPDF(bytes)) {
            PDFTextStripper stripper = new PDFTextStripper();
            texto = sanitize(stripper.getText(document));
        } catch (IOException e) {
            throw new PdfExtractionException("No se pudo leer el PDF '" + nombreArchivo + "'.", e);
        }
        if (texto.length() < props.getMinTextoChars()) {
            throw new PdfExtractionException(
                    "El PDF '" + nombreArchivo + "' no contiene capa de texto suficiente "
                    + "(¿documento escaneado sin OCR?). Re-súbalo con OCR previo.");
        }
        return texto;
    }

    private String sanitize(String text) {
        if (text == null || text.isBlank()) return "";
        return text.trim()
                .replaceAll("(\\r?\\n){2,}", "\n")
                .replaceAll(" +", " ")
                .replaceAll("[\\p{Cntrl}&&[^\\n\\t\\r]]", "");
    }
}
