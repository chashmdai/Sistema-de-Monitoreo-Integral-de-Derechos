package cl.smid.antecedentes.infraestructura.persistencia;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Tabla de referencia local: categorias DDN (principal y secundarias de la ficha).
 */
@Entity
@Table(name = "categoria_ddn")
@Getter
@Setter
@NoArgsConstructor
public class CategoriaDdnEntity extends ReferenciaEntityBase {
}
