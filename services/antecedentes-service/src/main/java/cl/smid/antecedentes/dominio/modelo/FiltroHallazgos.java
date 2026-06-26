package cl.smid.antecedentes.dominio.modelo;

/**
 * Criterios de listado de hallazgos. Los hallazgos son conocimiento institucional de lectura
 * nacional autenticada; no llevan acotacion territorial.
 *
 * @param estado       estado del hallazgo (nulo = cualquiera)
 * @param temporalidad temporalidad (nulo = cualquiera)
 * @param texto        texto libre sobre titulo/descripcion/folio (nulo o vacio = sin filtro)
 * @param pagina       indice de pagina (base 0)
 * @param tamano       tamano de pagina
 */
public record FiltroHallazgos(
        EstadoHallazgo estado,
        Temporalidad temporalidad,
        String texto,
        int pagina,
        int tamano) {

    public FiltroHallazgos {
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
