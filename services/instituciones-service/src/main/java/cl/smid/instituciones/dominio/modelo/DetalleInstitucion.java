package cl.smid.instituciones.dominio.modelo;

import java.util.List;

/**
 * Modelo de lectura para el detalle de una institución: la institución, el nombre y
 * ámbito de su tipo, y la lista de puntos focales asociados. Lo compone el servicio
 * de dominio a partir de las cargas individuales.
 *
 * @param institucion  la institución
 * @param tipoNombre   nombre del tipo asociado
 * @param ambito       ámbito del tipo asociado
 * @param puntosFocales puntos focales de la institución (puede estar vacía)
 */
public record DetalleInstitucion(
        Institucion institucion,
        String tipoNombre,
        Ambito ambito,
        List<PuntoFocal> puntosFocales) {

    public DetalleInstitucion {
        puntosFocales = puntosFocales == null ? List.of() : List.copyOf(puntosFocales);
    }
}
