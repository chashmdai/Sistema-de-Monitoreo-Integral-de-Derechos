package cl.smid.requerimientos.dominio.excepcion;

/**
 * Recurso inexistente o fuera del alcance territorial (REQ-404, HTTP 404).
 *
 * <p>Se usa tanto cuando el requerimiento no existe como cuando existe pero queda fuera
 * del alcance del usuario (Núcleo 2.3): en ambos casos se responde 404 para no revelar la
 * existencia del registro.</p>
 */
public class RecursoNoEncontrado extends RequerimientoException {

    public RecursoNoEncontrado(String mensaje) {
        super(CodigoError.NO_ENCONTRADO, mensaje);
    }
}
