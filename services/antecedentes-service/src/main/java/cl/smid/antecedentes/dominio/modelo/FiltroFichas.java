package cl.smid.antecedentes.dominio.modelo;

/**
 * Criterios de busqueda de la bandeja de fichas. La acotacion territorial (alcance, sede,
 * unidad) viaja por separado en {@link CriterioTerritorial}; este filtro solo recoge los
 * criterios funcionales. {@code procesoAlt} transporta el alt_key del proceso (la PK interna
 * nunca se expone); el adaptador lo traduce al id interno para filtrar.
 *
 * @param estado      estado de la ficha (nulo = cualquiera)
 * @param calificacion calificacion (nulo = cualquiera)
 * @param casoAlt     alt_key del caso (nulo = cualquiera)
 * @param procesoAlt  alt_key del proceso (nulo = cualquiera)
 * @param texto       texto libre sobre descripcion/folio (nulo o vacio = sin filtro)
 * @param pagina      indice de pagina (base 0)
 * @param tamano      tamano de pagina
 */
public record FiltroFichas(
        EstadoFicha estado,
        Calificacion calificacion,
        String casoAlt,
        String procesoAlt,
        String texto,
        int pagina,
        int tamano) {

    public FiltroFichas {
        if (pagina < 0) {
            pagina = 0;
        }
        if (tamano <= 0) {
            tamano = 20;
        }
        if (tamano > 200) {
            tamano = 200;
        }
    }
}
