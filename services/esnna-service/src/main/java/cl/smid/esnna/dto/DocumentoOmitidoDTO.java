package cl.smid.esnna.dto;

/**
 * Documento que no aportó al consolidado, con la fase y razón (ERR-3 / BIZ-1).
 * 'razon' es metadato seguro, nunca contenido del documento (PRIV-2).
 */
public record DocumentoOmitidoDTO(
        String nombreArchivo,
        String fase,       // "EXTRACCION_PDF" | "EXTRACCION_IA"
        String razon
) {}
