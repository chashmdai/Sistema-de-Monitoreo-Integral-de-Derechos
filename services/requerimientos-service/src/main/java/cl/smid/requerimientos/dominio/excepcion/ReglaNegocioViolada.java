package cl.smid.requerimientos.dominio.excepcion;

/**
 * Regla de negocio del dominio violada (REQ-422, HTTP 422).
 *
 * <p>Ejemplos: intentar enviar (INGRESAR) sin canal, sin sede o sin al menos un NNA;
 * asignar a un profesional que no pertenece a la unidad de destino.</p>
 */
public class ReglaNegocioViolada extends RequerimientoException {

    public ReglaNegocioViolada(String mensaje) {
        super(CodigoError.REGLA_NEGOCIO, mensaje);
    }
}
