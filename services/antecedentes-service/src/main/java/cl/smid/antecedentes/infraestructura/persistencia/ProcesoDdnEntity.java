package cl.smid.antecedentes.infraestructura.persistencia;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Tabla de referencia local: procesos DDN del que emana la ficha.
 */
@Entity
@Table(name = "proceso_ddn")
@Getter
@Setter
@NoArgsConstructor
public class ProcesoDdnEntity extends ReferenciaEntityBase {
}
