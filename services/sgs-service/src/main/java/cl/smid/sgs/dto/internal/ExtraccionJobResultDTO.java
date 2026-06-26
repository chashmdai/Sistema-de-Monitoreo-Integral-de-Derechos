package cl.smid.sgs.dto.internal;

/** Payload del job de extracción COMPLETADO: datos para el form + meta para que el front la eche en /guardar. */
public record ExtraccionJobResultDTO(
        OficioExtraccionDTO extraccion,
        String modelo,
        String modeloSnapshot,
        OpenAiUsage usage
) {}
