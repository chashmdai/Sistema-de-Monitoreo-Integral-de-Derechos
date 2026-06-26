package cl.smid.requerimientos.dominio.modelo;

/**
 * Serie de numeración del folio (Núcleo 6.5).
 *
 * <p>La serie {@link #BETA} (marcha blanca) corre por un contador independiente y un
 * prefijo distinto, de modo que <b>no consume</b> la numeración {@link #OFICIAL}: la
 * serie oficial arranca inmaculada en {@code 0/2027}.</p>
 */
public enum SerieFolio {

    /** Serie oficial. Su contador nace en 0 cada año (p. ej. {@code RM-1/2027}). */
    OFICIAL(""),

    /** Serie de marcha blanca. Prefijo separado para no contaminar la oficial. */
    BETA("B");

    private final String prefijo;

    SerieFolio(String prefijo) {
        this.prefijo = prefijo;
    }

    /**
     * Prefijo que se antepone al correlativo en el folio para distinguir la serie.
     *
     * @return cadena vacía para {@code OFICIAL}; {@code "B"} para {@code BETA}.
     */
    public String prefijo() {
        return prefijo;
    }
}
