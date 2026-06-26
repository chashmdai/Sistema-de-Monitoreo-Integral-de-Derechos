package cl.smid.instituciones.dominio.excepcion;

/**
 * Conflicto de unicidad: nombre de tipo o código de institución ya existente.
 * Se traduce a {@code INS-409 / 409}.
 */
public class ConflictoException extends ExcepcionDominio {

    public ConflictoException(String mensaje) {
        super(mensaje);
    }

    @Override
    public CodigoError codigo() {
        return CodigoError.CONFLICTO;
    }
}
