package cl.smid.esnna.dto;

import cl.smid.esnna.domain.RespuestaSiNo;
import cl.smid.esnna.domain.SexoNna;

import java.util.List;

/**
 * Salida estructurada de Fase 2 (REDUCE, gpt-5.5). Es el contrato que enforcea
 * el json_schema de la llamada.
 *
 * NO trae el color del semáforo ni la justificación: trae la evidencia por
 * criterio (criterios) y la confianza; el backend computa el semáforo de forma
 * determinista (FEAT semáforo-en-backend) y arma la justificación.
 *
 * fecha/fechaNacimiento/edad viajan como String crudo (la IA los emite como
 * texto); el mapper los parsea con tolerancia. sexoNna y los SÍ/NO usan los
 * enums con deserialización tolerante (@JsonCreator).
 */
public record EsnnaConsolidadoDTO(

        // Evidencia para el semáforo (la IA detecta; el backend decide)
        CriteriosSemaforoDTO criterios,
        Double confianzaAnalisis,

        // Gestión
        String paraQuerella,
        String requerimiento,
        String nroCorrelativo,
        String fecha,
        String nroOficio,
        String carpeta,
        String region,
        String tipoPrograma,
        String nombreProgramaResidencia,

        // Delito + contexto
        String delitoConcreto,
        RespuestaSiNo nnaBajoCuidadoEstado,
        String residencia,
        String denunciante,
        String contactoDenunciante,

        // NNA (nombre completo, decisión #2)
        String nna,
        SexoNna sexoNna,
        String cedulaNna,
        String nacionalidadNna,
        String fechaNacimiento,
        String edad,
        String consumoDrogasAlcohol,
        String curador,
        String nadPma,
        String contactoNadPma,

        // Imputados (MOD-3: lista de objetos)
        RespuestaSiNo imputadoConocido,
        List<ImputadoDTO> imputados,

        // Hechos y lugares
        String lugarOcurrenciaHechos,
        String comunasInvolucradas,
        String hechos,
        String tipoViolencia,
        List<String> redesSocialesMencionadas,
        String identificacionLocalesBaresHoteles,
        RespuestaSiNo presuntaRedExplotacion,

        // Seguimiento legal
        String observacion,
        String querella,
        String denunciasAnteriores,
        String rucAsociados,
        String gestiones,
        String descripcion,
        String pendiente
) {}
