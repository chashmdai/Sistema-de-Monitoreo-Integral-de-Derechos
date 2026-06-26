package cl.smid.requerimientos.dominio.excepcion;

/**
 * Autenticado pero sin el rol requerido (AUTZ-004, HTTP 403).
 *
 * <p>En este servicio el manejador 403 SÍ es alcanzable: el endpoint de admisibilidad exige
 * el rol de Coordinación. La denegación territorial, en cambio, se expresa como 404.</p>
 */
public class AccesoDenegado extends RequerimientoException {

    public AccesoDenegado(String mensaje) {
        super(CodigoError.ACCESO_DENEGADO, mensaje);
    }
}
