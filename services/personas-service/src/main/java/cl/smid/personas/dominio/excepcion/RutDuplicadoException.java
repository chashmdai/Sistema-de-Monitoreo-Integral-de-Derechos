package cl.smid.personas.dominio.excepcion;

/**
 * Se lanza al intentar crear o actualizar una persona con un RUT que ya pertenece a otra
 * persona vigente (regla de unicidad, Núcleo 5.5). Código {@code PER-003} → HTTP 409.
 *
 * <p>Es la defensa de negocio que se anticipa a la restricción {@code UNIQUE(rut)} del
 * motor; ambas capas protegen el mismo invariante.</p>
 */
public class RutDuplicadoException extends PersonaException {

    public RutDuplicadoException(String rutCanonico) {
        super(CodigoError.RUT_DUPLICADO,
                "Ya existe una persona vigente con el RUT '" + rutCanonico + "'.");
    }
}
