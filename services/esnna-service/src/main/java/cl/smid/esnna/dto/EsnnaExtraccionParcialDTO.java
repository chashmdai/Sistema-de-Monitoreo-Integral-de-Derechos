package cl.smid.esnna.dto;

import java.util.List;

/**
 * Extracción unitaria desde UN documento (Fase 1 / MAP, gpt-4o-mini).
 * Solo datos literales presentes en el documento; cero derivación compleja.
 *
 * Cambios vs versión original:
 *  - nna: NOMBRE COMPLETO (decisión #2). Se elimina la regla de iniciales (§6.6.2).
 *  - imputados: lista de objetos (MOD-3), no tres arrays correlacionados por posición.
 *
 * Derivados por Fase 2 (no acá): nnaBajoCuidadoEstado, imputadoConocido, tipoViolencia,
 * criterios del semáforo, hechos unificado, observacion, rucAsociados.
 */
public record EsnnaExtraccionParcialDTO(

        // Identificación del documento
        String nroOficio,
        String ruc,
        String fecha,
        String region,

        // NNA víctima (nombre completo)
        String nna,
        String cedulaNna,
        String edad,
        String sexoNna,
        String fechaNacimiento,
        String nacionalidadNna,
        String consumoDrogasAlcohol,
        String residencia,
        String nombreProgramaResidencia,
        String curador,

        // Denunciante
        String denunciante,
        String contactoDenunciante,

        // Delito
        String delitoConcreto,

        // Imputados (objetos, no listas paralelas)
        List<ImputadoDTO> imputados,

        // Hechos y lugares
        String lugarOcurrenciaHechos,
        String comunasInvolucradas,
        List<String> redesSocialesMencionadas,
        String identificacionLocalesBaresHoteles,

        // Antecedentes
        String denunciasAnteriores,

        // Resumen narrativo de este documento
        String hechosAislados
) {}
