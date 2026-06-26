package cl.smid.requerimientos.dominio.modelo;

/**
 * Complejidad asignada al requerimiento durante la clasificación (Núcleo 6.2 / 6.5).
 *
 * <p>Las complejidades {@link #MEDIANA} y {@link #ALTA} activan la costura hacia
 * Vulneraciones (6.5): marcan {@code requiere_ficha_reservada = 1}.</p>
 */
public enum Complejidad {
    BAJA,
    MEDIANA,
    ALTA,
    FAST_TRACK;

    /**
     * Indica si esta complejidad exige Ficha Interna Reservada (costura hacia 6.5).
     *
     * @return {@code true} para {@code MEDIANA} y {@code ALTA}.
     */
    public boolean requiereFichaReservada() {
        return this == MEDIANA || this == ALTA;
    }
}
