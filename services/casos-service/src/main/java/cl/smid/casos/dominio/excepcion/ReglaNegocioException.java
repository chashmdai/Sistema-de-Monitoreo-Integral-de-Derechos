package cl.smid.casos.dominio.excepcion;

/**
 * Incumplimiento de una regla de negocio del dominio de Casos. Responde {@code 422} (CAS-422).
 */
public class ReglaNegocioException extends ExcepcionDominio {

    public ReglaNegocioException(String mensaje) {
        super(CodigoError.CAS_422, mensaje);
    }
}
