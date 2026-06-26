package cl.smid.requerimientos.infraestructura.persistencia.entidad;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad JPA de un anexo (tabla {@code requerimiento_anexo}). SOLO metadatos: el binario llega
 * con Documentos (6.9), que poblará {@code referencia_externa}.
 */
@Entity
@Table(name = "requerimiento_anexo")
@Getter
@Setter
@NoArgsConstructor
public class RequerimientoAnexoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_requerimiento", nullable = false)
    private RequerimientoEntity requerimiento;

    @Column(name = "nombre_archivo", nullable = false, length = 240)
    private String nombreArchivo;

    @Column(name = "tipo_mime", length = 120)
    private String tipoMime;

    @Column(name = "tamano_bytes")
    private Long tamanoBytes;

    @Column(name = "referencia_externa", length = 240)
    private String referenciaExterna;
}
