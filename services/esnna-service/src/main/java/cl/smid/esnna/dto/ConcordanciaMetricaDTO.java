package cl.smid.esnna.dto;

import java.util.Map;

/**
 * Métricas de concordancia IA vs humano (decisión #12). Insumo para la unidad
 * de estudios: cuánto y en qué dirección el profesional corrige al motor.
 */
public record ConcordanciaMetricaDTO(
        long totalCasos,
        long overrides,                 // casos donde semaforoFinal != semaforoIa
        double tasaOverride,            // overrides / totalCasos
        Map<String, Long> matrizConfusion,  // "IA_ROJO->FINAL_AMARILLO" -> n
        long subestimacionIa,           // IA menos grave que el humano
        long sobreestimacionIa          // IA más grave que el humano
) {}
