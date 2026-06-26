package cl.smid.esnna.dto;

import cl.smid.esnna.domain.RespuestaSiNo;
import cl.smid.esnna.domain.Semaforo;
import cl.smid.esnna.domain.SexoNna;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * PATCH /api/esnna/casos/{id}. Semántica parcial: solo los campos no nulos se
 * aplican. No permite tocar semaforoIa ni la auditoría (inmutables).
 *
 * Si viene semaforoFinal, el servicio lo trata como override y registra autor +
 * fecha. imputados: si viene no-null, reemplaza la lista completa.
 */
public record EsnnaCasoUpdateRequest(

        Semaforo semaforoFinal,

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
