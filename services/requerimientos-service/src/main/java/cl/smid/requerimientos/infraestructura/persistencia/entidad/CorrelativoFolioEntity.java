package cl.smid.requerimientos.infraestructura.persistencia.entidad;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad JPA del contador correlativo de folio (tabla {@code correlativo_folio}). La unicidad
 * (id_sede, anio, serie) más el bloqueo pesimista en el repositorio garantizan folios únicos
 * bajo concurrencia. La serie BETA es una fila independiente: no consume la numeración OFICIAL.
 */
@Entity
@Table(name = "correlativo_folio",
        uniqueConstraints = @UniqueConstraint(name = "uk_correlativo",
                columnNames = {"id_sede", "anio", "serie"}))
@Getter
@Setter
@NoArgsConstructor
public class CorrelativoFolioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "id_sede", nullable = false, columnDefinition = "CHAR(36)")
    private String idSede;

    @Column(name = "anio", nullable = false)
    private Integer anio;

    @Column(name = "serie", nullable = false, length = 10)
    private String serie;

    @Column(name = "ultimo_valor", nullable = false)
    private Long ultimoValor;
}
