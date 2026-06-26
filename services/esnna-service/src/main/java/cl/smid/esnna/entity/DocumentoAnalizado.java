package cl.smid.esnna.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

/**
 * Referencia a un documento que participó de un análisis, para la auditoría.
 * Se guarda el hash, NUNCA el contenido: el registro prueba "este documento
 * exacto entró al análisis" sin almacenar de nuevo PII del menor en la tabla
 * de auditoría. El contenido del PDF vive en MinIO (Fase 4), indexado por el
 * mismo sha256.
 */
@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentoAnalizado {

    @Column(name = "nombre_archivo", length = 255)
    private String nombreArchivo;

    @Column(name = "sha256", length = 64)
    private String sha256;
}
