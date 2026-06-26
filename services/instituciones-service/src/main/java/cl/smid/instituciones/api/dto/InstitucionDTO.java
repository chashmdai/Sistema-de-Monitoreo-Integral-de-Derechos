package cl.smid.instituciones.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;
import java.util.List;

/**
 * Representación pública del detalle de una institución, con el nombre y ámbito de su
 * tipo resueltos, el RUT en formato canónico y la lista de puntos focales.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record InstitucionDTO(
        String altKey,
        String codigo,
        String nombre,
        String tipoAlt,
        String tipoNombre,
        String ambito,
        String rut,
        String regionCodigo,
        String comunaCodigo,
        String direccion,
        String telefono,
        String email,
        String sitioWeb,
        boolean activa,
        Instant creadoEn,
        Instant actualizadoEn,
        List<PuntoFocalDTO> puntosFocales) {
}
