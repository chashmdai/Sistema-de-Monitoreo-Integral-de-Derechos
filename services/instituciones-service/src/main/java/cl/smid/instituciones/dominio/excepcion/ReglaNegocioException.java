package cl.smid.instituciones.dominio.excepcion;

/**
 * Regla de negocio incumplida: tipo inexistente o no vigente al asociarlo, o RUT
 * que no supera la validación de módulo 11. Se traduce a {@code INS-422 / 422}.
 */
public class ReglaNegocioException extends ExcepcionDominio {

    public ReglaNegocioException(String mensaje) {
        super(mensaje);
    }

    @Override
    public CodigoError codigo() {
        return CodigoError.REGLA_NEGOCIO;
    }
}
