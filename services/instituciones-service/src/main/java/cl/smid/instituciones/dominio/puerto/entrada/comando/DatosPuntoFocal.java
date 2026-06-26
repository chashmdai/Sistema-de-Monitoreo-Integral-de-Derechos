package cl.smid.instituciones.dominio.puerto.entrada.comando;

/**
 * Datos de entrada para crear o editar un {@link cl.smid.instituciones.dominio.modelo.PuntoFocal}.
 *
 * @param nombre    nombre del contacto (obligatorio)
 * @param cargo     cargo opcional
 * @param email     correo opcional (se valida formato si viene)
 * @param telefono  teléfono opcional
 * @param principal si es el contacto principal de la institución
 */
public record DatosPuntoFocal(String nombre, String cargo, String email, String telefono, boolean principal) {
}
