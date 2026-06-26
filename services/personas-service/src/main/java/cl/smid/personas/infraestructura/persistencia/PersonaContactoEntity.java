package cl.smid.personas.infraestructura.persistencia;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad JPA de la tabla {@code persona_contacto}. Pertenece al agregado {@link PersonaEntity}
 * mediante una relación {@code @ManyToOne} sobre la columna {@code id_persona}.
 */
@Entity
@Table(name = "persona_contacto")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonaContactoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Persona dueña del contacto (lado propietario de la FK). */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_persona", nullable = false)
    private PersonaEntity persona;

    /** Tipo de contacto (TELEFONO | EMAIL | DIRECCION) como texto. */
    @Column(name = "tipo", nullable = false, length = 20)
    private String tipo;

    @Column(name = "valor", nullable = false, length = 240)
    private String valor;
}
