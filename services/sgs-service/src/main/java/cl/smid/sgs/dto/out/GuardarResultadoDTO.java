package cl.smid.sgs.dto.out;

/** Resultado de /guardar. 'duplicado' advierte oficio repetido sin bloquear (higiene operacional). */
public record GuardarResultadoDTO(Long oficioId, int recomendacionesCreadas, boolean duplicado) {}
