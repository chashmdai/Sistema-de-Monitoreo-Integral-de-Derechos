package cl.smid.instituciones.dominio.modelo;

import cl.smid.instituciones.dominio.excepcion.ExcepcionValidacion;

/**
 * Solicitud de paginación, expresada en términos del dominio (independiente de Spring Data).
 *
 * @param pagina índice de página, base 0
 * @param tamano cantidad de elementos por página, en el rango [1, 200]
 */
public record Paginado(int pagina, int tamano) {

    /** Tamaño máximo permitido por página (contrato del ecosistema). */
    public static final int TAMANO_MAXIMO = 200;
    /** Tamaño por defecto cuando el cliente no lo especifica. */
    public static final int TAMANO_DEFECTO = 20;

    public Paginado {
        if (pagina < 0) {
            throw new ExcepcionValidacion("El parámetro 'pagina' no puede ser negativo.");
        }
        if (tamano < 1 || tamano > TAMANO_MAXIMO) {
            throw new ExcepcionValidacion(
                "El parámetro 'tamano' debe estar entre 1 y " + TAMANO_MAXIMO + ".");
        }
    }

    /**
     * Construye un {@link Paginado} aplicando los valores por defecto del contrato
     * cuando los parámetros llegan nulos desde la capa REST.
     *
     * @param pagina índice de página (nulo => 0)
     * @param tamano tamaño de página (nulo => {@link #TAMANO_DEFECTO})
     * @return el paginado validado
     */
    public static Paginado de(Integer pagina, Integer tamano) {
        int p = pagina == null ? 0 : pagina;
        int t = tamano == null ? TAMANO_DEFECTO : tamano;
        return new Paginado(p, t);
    }
}
