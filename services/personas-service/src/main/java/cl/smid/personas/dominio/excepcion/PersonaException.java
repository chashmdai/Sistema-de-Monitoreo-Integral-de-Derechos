package cl.smid.personas.dominio.excepcion;

import java.util.Collections;
import java.util.Map;

/**
 * Excepción base de todo error de negocio del dominio de Personas.
 *
 * <p>Transporta un {@link CodigoError} (que ya conoce su estado HTTP y frase) y, sólo en
 * el caso de validación, un mapa de {@code detalles} campo→mensaje. El manejador global de
 * la capa {@code api} la traduce al sobre de error unificado (Núcleo 2.5). El dominio no
 * depende del framework: esta excepción es un POJO.</p>
 */
public class PersonaException extends RuntimeException {

    private final CodigoError codigoError;
    private final transient Map<String, String> detalles;

    protected PersonaException(CodigoError codigoError, String mensaje) {
        this(codigoError, mensaje, Collections.emptyMap());
    }

    protected PersonaException(CodigoError codigoError, String mensaje, Map<String, String> detalles) {
        super(mensaje);
        this.codigoError = codigoError;
        this.detalles = (detalles == null) ? Collections.emptyMap() : Map.copyOf(detalles);
    }

    /** Código de negocio asociado (incluye estado HTTP y frase). */
    public CodigoError codigoError() {
        return codigoError;
    }

    /** Detalles de validación (campo→mensaje). Vacío salvo en {@code PER-001}. */
    public Map<String, String> detalles() {
        return detalles;
    }
}
