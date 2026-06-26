package cl.smid.instituciones.dominio.puerto.entrada.comando;

/**
 * Datos de entrada para crear o editar una {@link cl.smid.instituciones.dominio.modelo.Institucion}.
 * El {@code rut} llega como texto opcional y el dominio lo valida por módulo 11 si viene.
 *
 * @param codigo       código institucional opcional (único si viene)
 * @param nombre       nombre (obligatorio)
 * @param tipoAlt      alt_key del tipo (obligatorio; debe existir y estar vigente)
 * @param rut          RUT en texto, opcional
 * @param regionCodigo código de región opcional
 * @param comunaCodigo código de comuna opcional
 * @param direccion    dirección opcional
 * @param telefono     teléfono opcional
 * @param email        correo opcional (se valida formato si viene)
 * @param sitioWeb     sitio web opcional
 */
public record DatosInstitucion(
        String codigo,
        String nombre,
        String tipoAlt,
        String rut,
        String regionCodigo,
        String comunaCodigo,
        String direccion,
        String telefono,
        String email,
        String sitioWeb) {
}
