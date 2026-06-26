package cl.smid.esnna.service;

import cl.smid.esnna.exception.PdfExtractionException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.HexFormat;

/**
 * Extracción de texto de PDFs.
 *  - Calcula SHA-256 del archivo (auditoría + clave de respaldo en MinIO).
 *  - PRIV-2: los mensajes de error llevan SOLO metadato (nombre, conteo), nunca
 *    contenido del documento.
 *  - Mantiene la detección de scans sin OCR y el saneamiento del original.
 */
@Service
public class PdfExtractionService {

    @Value("${esnna.pdf.min-chars-utiles:200}")
    private int minCharsUtiles;

    public TextoExtraido extraer(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new PdfExtractionException("Archivo vacío o ausente.");
        }
        String nombre = file.getOriginalFilename() != null ? file.getOriginalFilename() : "(sin nombre)";

        final byte[] bytes;
        try {
            bytes = file.getBytes();
        } catch (IOException e) {
            throw new PdfExtractionException("No se pudo leer el archivo: " + nombre, e);
        }
        String sha256 = sha256(bytes);

        try (PDDocument document = PDDocument.load(bytes)) {
            if (document.isEncrypted()) {
                throw new PdfExtractionException("El PDF está encriptado y no puede procesarse: " + nombre);
            }
            String sanitized = sanitizeText(new PDFTextStripper().getText(document));
            if (sanitized.length() < minCharsUtiles) {
                throw new PdfExtractionException(
                        "El PDF no contiene texto extraíble (posible escaneo sin OCR): " + nombre);
            }
            return new TextoExtraido(nombre, sha256, sanitized);
        } catch (PdfExtractionException e) {
            throw e;
        } catch (IOException e) {
            throw new PdfExtractionException("Error en la lectura del PDF: " + nombre, e);
        }
    }

    private String sha256(byte[] data) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(md.digest(data));
        } catch (Exception e) {
            throw new PdfExtractionException("No se pudo calcular el hash del documento.", e);
        }
    }

    private String sanitizeText(String text) {
        if (text == null || text.isBlank()) return "";
        return text.trim()
                .replaceAll("(\\r?\\n){3,}", "\n\n")   // preserva separación de párrafos
                .replaceAll("[ \\t]+", " ")
                .replaceAll("[\\p{Cntrl}&&[^\\n\\t\\r]]", "");
    }
}
