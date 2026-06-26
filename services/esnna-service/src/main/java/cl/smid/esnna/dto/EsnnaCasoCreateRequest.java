package cl.smid.esnna.dto;

import cl.smid.esnna.domain.RespuestaSiNo;
import cl.smid.esnna.domain.Semaforo;
import cl.smid.esnna.domain.SexoNna;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * Payload de POST /api/esnna/guardar.
 *
 * Integridad de auditoría: los datos inmutables del análisis (semáforo IA,
 * confianza, modelos, hashes de documentos, usage) NO vienen del cliente; el
 * servicio los toma del borrador referenciado por draftId. El cliente solo
 * envía los campos editables y su decisión de semáforo final.
 *
 * No incluye id, fechaIngreso, estadoGestion, creadoPor: los controla el server.
 */
public record EsnnaCasoCreateRequest(

        @NotBlank(message = "draftId es obligatorio")
        String draftId,

        @NotNull(message = "semaforoFinal es obligatorio")
        Semaforo semaforoFinal,

        // ---- Campos editables (el humano puede corregir lo que la IA extrajo) ----
        String paraQuerella,
        String requerimiento,
        String nroCorrelativo,
        String fecha,
        String nroOficio,
        String carpeta,
        String region,
        String tipoPrograma,
        String nombreProgramaResidencia,
        String delitoConcreto,
        RespuestaSiNo nnaBajoCuidadoEstado,
        String residencia,
        String denunciante,
        String contactoDenunciante,
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
        RespuestaSiNo imputadoConocido,
        List<ImputadoDTO> imputados,
        String lugarOcurrenciaHechos,
        String comunasInvolucradas,

        @Size(max = 200_000, message = "hechos excede el largo permitido")
        String hechos,

        String tipoViolencia,
        List<String> redesSocialesMencionadas,
        String identificacionLocalesBaresHoteles,
        RespuestaSiNo presuntaRedExplotacion,
        String observacion,
        String querella,
        String denunciasAnteriores,
        String rucAsociados,
        String gestiones,
        String descripcion,
        String pendiente
) {}
