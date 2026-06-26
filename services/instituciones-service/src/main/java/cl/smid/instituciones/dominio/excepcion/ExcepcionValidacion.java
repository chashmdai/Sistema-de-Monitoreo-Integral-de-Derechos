package cl.smid.instituciones.dominio.excepcion;

/**
 * Entrada inválida detectada en el dominio (por ejemplo, un campo obligatorio en
 * blanco o un ámbito desconocido). Se traduce a {@code INS-001 / 400}.
 */
public class ExcepcionValidacion extends ExcepcionDominio {

    public ExcepcionValidacion(String mensaje) {
        super(mensaje);
    }

    @Override
    public CodigoError codigo() {
        return CodigoError.VALIDACION;
    }
}
