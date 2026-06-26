package cl.smid.catalogo.dominio.puerto.entrada;

/**
 * Comando de creación de un derecho. Es un objeto del dominio (sin anotaciones de
 * framework): la validación de formato vive en el DTO de la capa api; aquí llegan ya
 * los valores de negocio.
 *
 * @param idPadreAltKey alt_key del derecho padre; {@code null} para crear una categoría raíz
 * @param codigo        código estable e inmutable del derecho (obligatorio)
 * @param nombre        nombre legible (obligatorio)
 * @param descripcion   descripción opcional (puede ser nula)
 * @param orden         orden de despliegue entre hermanos; {@code null} ⇒ 0
 */
public record CrearDerechoCmd(
        String idPadreAltKey,
        String codigo,
        String nombre,
        String descripcion,
        Short orden
) {}
