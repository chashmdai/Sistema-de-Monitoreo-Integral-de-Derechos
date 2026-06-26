package cl.smid.instituciones.dominio.modelo;

/**
 * Modelo de lectura para listados de instituciones: combina la institución con el
 * nombre y el ámbito de su tipo, ya resueltos por el adaptador (carga por lotes,
 * sin N+1). Es un proyección de solo lectura; no expone puntos focales.
 *
 * @param institucion la institución
 * @param tipoNombre  nombre del tipo asociado
 * @param ambito      ámbito del tipo asociado
 */
public record ResumenInstitucion(Institucion institucion, String tipoNombre, Ambito ambito) {
}
