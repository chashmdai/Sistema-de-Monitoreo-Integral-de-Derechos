package cl.smid.sgs.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Referencia a documento por hash (nunca contenido) para la auditoría legal. */
@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DocumentoHash {

    @Column(name = "nombre_archivo", length = 255)
    private String nombreArchivo;

    @Column(name = "sha256", length = 64)
    private String sha256;
}
