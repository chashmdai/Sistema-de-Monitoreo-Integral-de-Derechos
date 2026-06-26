package cl.smid.catalogo.dominio.puerto.entrada;

/**
 * Comando de creación de una causa vinculada a un derecho.
 *
 * @param codigo código de la causa (único dentro del derecho)
 * @param nombre nombre legible de la causa
 */
public record CrearCausaCmd(String codigo, String nombre) {}
