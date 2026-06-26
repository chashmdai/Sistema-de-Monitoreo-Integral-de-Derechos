package cl.smid.antecedentes.infraestructura.persistencia;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Tabla de referencia local: instrumentos asociados a los hallazgos.
 */
@Entity
@Table(name = "instrumento")
@Getter
@Setter
@NoArgsConstructor
public class InstrumentoEntity extends ReferenciaEntityBase {
}
