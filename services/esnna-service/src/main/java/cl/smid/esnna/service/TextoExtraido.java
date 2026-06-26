package cl.smid.esnna.service;

/**
 * Texto saneado de un PDF + su hash SHA-256 (para auditoría y respaldo en MinIO)
 * + nombre de archivo (metadato seguro).
 */
public record TextoExtraido(String nombreArchivo, String sha256, String texto) {}
