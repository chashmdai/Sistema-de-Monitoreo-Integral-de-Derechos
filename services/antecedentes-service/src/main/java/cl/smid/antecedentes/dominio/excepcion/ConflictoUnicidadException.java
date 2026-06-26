package cl.smid.antecedentes.dominio.excepcion;

/**
 * Conflicto de unicidad ({@code ANT-409}, HTTP 409): folio duplicado o codigo de
 * referencia repetido.
 */
public class ConflictoUnicidadException extends AntecedentesException {

    public ConflictoUnicidadException(String mensaje) {
        super(CodigoError.ANT_409, mensaje);
    }
}
