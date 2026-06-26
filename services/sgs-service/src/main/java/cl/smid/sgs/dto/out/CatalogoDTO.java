package cl.smid.sgs.dto.out;

import cl.smid.sgs.enums.TipoCatalogo;

public record CatalogoDTO(Long id, TipoCatalogo tipo, String codigo, String etiqueta, boolean activo) {}
