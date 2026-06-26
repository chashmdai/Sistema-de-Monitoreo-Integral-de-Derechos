package cl.smid.antecedentes.dominio.excepcion;

/**
 * Regla de negocio incumplida ({@code ANT-422}, HTTP 422): transicion de estado invalida,
 * incoherencia de hallazgo, referencia inexistente o no vigente, limites de categorias o
 * articulos CDN, etc.
 */
public class ReglaNegocioException extends AntecedentesException {

    public ReglaNegocioException(String mensaje) {
        super(CodigoError.ANT_422, mensaje);
    }

    public ReglaNegocioException(CodigoError codigoError, String mensaje) {
        super(codigoError, mensaje);
    }
}
