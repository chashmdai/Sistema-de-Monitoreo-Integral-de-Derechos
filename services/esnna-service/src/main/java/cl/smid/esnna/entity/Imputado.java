package cl.smid.esnna.entity;

import cl.smid.esnna.domain.RespuestaSiNo;
import jakarta.persistence.*;
import lombok.*;

/**
 * MOD-3: un imputado es una unidad. El modelo original guardaba tres listas
 * paralelas (nombres/ruts/domicilios) correlacionadas por posición: si la IA
 * extraía 3 nombres y 2 RUTs, las listas se desalineaban en silencio y se
 * atribuía el RUT de un imputado a otro, en datos que alimentan querellas.
 *
 * Aquí cada imputado es una fila; 'orden' preserva el orden de aparición.
 * 'sexo' se mantiene como texto libre porque el documento suele expresarlo de
 * forma imprecisa ("sexo posible imputado"). 'esFuncionarioPublico' alimenta el
 * criterio C3 (agente cualificado) del semáforo.
 */
@Entity
@Table(name = "esnna_imputados", indexes = {
        @Index(name = "idx_imputado_caso", columnList = "caso_id"),
        @Index(name = "idx_imputado_rut", columnList = "rut")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Imputado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @ToString.Include
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "caso_id", nullable = false)
    private EsnnaEntity caso;

    @Column(name = "orden")
    private Integer orden;

    @ToString.Include
    @Column(name = "nombre", length = 255)
    private String nombre;

    @Column(name = "rut", length = 20)
    private String rut;

    @Column(name = "domicilio", columnDefinition = "TEXT")
    private String domicilio;

    @Column(name = "sexo", length = 50)
    private String sexo;

    @Enumerated(EnumType.STRING)
    @Column(name = "es_funcionario_publico", length = 2)
    private RespuestaSiNo esFuncionarioPublico;
}
