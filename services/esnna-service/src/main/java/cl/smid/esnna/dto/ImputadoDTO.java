package cl.smid.esnna.dto;

import cl.smid.esnna.domain.RespuestaSiNo;

/**
 * Imputado como unidad (MOD-3). nombre/rut/domicilio viajan juntos, sin
 * correlación posicional frágil.
 */
public record ImputadoDTO(
        Integer orden,
        String nombre,
        String rut,
        String domicilio,
        String sexo,
        RespuestaSiNo esFuncionarioPublico
) {}
