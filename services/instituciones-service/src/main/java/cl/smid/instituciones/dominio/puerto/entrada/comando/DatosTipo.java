package cl.smid.instituciones.dominio.puerto.entrada.comando;

/**
 * Datos de entrada para crear o editar un {@link cl.smid.instituciones.dominio.modelo.TipoInstitucion}.
 * El {@code ambito} llega como texto y el dominio lo interpreta/valida
 * ({@link cl.smid.instituciones.dominio.modelo.Ambito#desde(String)}).
 *
 * @param nombre      nombre del tipo (obligatorio)
 * @param ambito      ámbito sectorial como texto (obligatorio)
 * @param descripcion descripción opcional
 */
public record DatosTipo(String nombre, String ambito, String descripcion) {
}
