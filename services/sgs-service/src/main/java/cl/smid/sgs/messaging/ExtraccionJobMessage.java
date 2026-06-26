package cl.smid.sgs.messaging;

/** Mensaje de Fase A. NO viaja el MultipartFile: solo el hash; el consumer recupera de MinIO (PDF-3). */
public record ExtraccionJobMessage(String jobId, String pdfHash, String nombreArchivo) {}
