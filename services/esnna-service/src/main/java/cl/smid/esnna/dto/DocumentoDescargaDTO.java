package cl.smid.esnna.dto;

/**
 * Referencia descargable de un documento fuente respaldado en MinIO.
 * url puede ser null si el storage no está disponible.
 */
public record DocumentoDescargaDTO(
        String nombreArchivo,
        String sha256,
        String url
) {}
