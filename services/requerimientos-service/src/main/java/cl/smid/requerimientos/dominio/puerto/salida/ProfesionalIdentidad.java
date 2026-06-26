package cl.smid.requerimientos.dominio.puerto.salida;

/**
 * Proyección mínima de un profesional resuelto contra Identidad (6.1), usada para validar su
 * pertenencia a la unidad de destino al asignar un requerimiento (USR.02).
 *
 * @param altKey    alt_key del profesional
 * @param idUnidadAlt alt_key de la unidad a la que pertenece (puede ser nulo si Identidad no lo expone)
 */
public record ProfesionalIdentidad(String altKey, String idUnidadAlt) {
}
