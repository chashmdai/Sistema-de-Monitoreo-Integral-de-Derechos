package cl.smid.esnna.dto;

/**
 * Documento materializado en memoria antes de encolar el job.
 *
 * Razón de existir: MultipartFile es un recurso del request HTTP — Tomcat
 * elimina sus temporales al cerrar el request. Como el 202 se devuelve antes
 * de procesar, el pipeline NO puede tocar MultipartFile: los bytes se leen en
 * el submit y viajan en este record. El tope lo siguen dando max-archivos (30)
 * y spring.servlet.multipart.max-request-size.
 *
 * sha256 = hash de los bytes del archivo. Es la misma clave que usa el
 * respaldo MinIO (objeto = hash) y la auditoría, y de ella deriva el loteId
 * (LoteHasher) que unifica idempotencia de jobs y caché de drafts.
 */
public record DocumentoEntrada(
        String nombreArchivo,
        String contentType,
        byte[] bytes,
        String sha256
) {
}