package cl.smid.requerimientos.infraestructura.persistencia.entidad;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad JPA de un derecho vulnerado (tabla {@code requerimiento_nna_derecho}). Solo guarda
 * referencias por alt_key hacia catalogo-service (derecho y causa opcional).
 */
@Entity
@Table(name = "requerimiento_nna_derecho")
@Getter
@Setter
@NoArgsConstructor
public class RequerimientoNnaDerechoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_requerimiento_nna", nullable = false)
    private RequerimientoNnaEntity nna;

    @Column(name = "id_derecho_alt", nullable = false, columnDefinition = "CHAR(36)")
    private String idDerechoAlt;

    @Column(name = "id_causa_alt", columnDefinition = "CHAR(36)")
    private String idCausaAlt;
}
