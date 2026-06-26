package cl.smid.esnna.dto;

import cl.smid.esnna.domain.EstadoGestion;
import cl.smid.esnna.domain.RespuestaSiNo;
import cl.smid.esnna.domain.Semaforo;
import cl.smid.esnna.domain.SexoNna;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Detalle completo del caso (GET /casos/{id}). Proyección segura: el controller
 * nunca serializa EsnnaEntity (CTRL-1). Incluye el desdoble del semáforo, estado,
 * metadatos de gestión y, si corresponde, datos de anulación.
 */
public record EsnnaDetalleDTO(
        Long id,
        Long version,

        // Semáforo IA (inmutable) vs final (humano)
        Semaforo semaforoIa,
        String justificacionIa,
        Double confianzaIa,
        Semaforo semaforoFinal,
        String semaforoFinalAutor,
        LocalDateTime semaforoFinalFecha,

        // Gestión
        String paraQuerella,
        String requerimiento,
        String nroCorrelativo,
        LocalDate fecha,
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

        // NNA
        String nna,
        SexoNna sexoNna,
        String cedulaNna,
        String nacionalidadNna,
        LocalDate fechaNacimiento,
        Integer edad,
        String consumoDrogasAlcohol,
        String curador,
        String nadPma,
        String contactoNadPma,

        // Imputados
        RespuestaSiNo imputadoConocido,
        List<ImputadoDTO> imputados,

        // Hechos
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
        String pendiente,

        // Metadatos
        EstadoGestion estadoGestion,
        String creadoPor,
        LocalDateTime fechaIngreso,
        LocalDateTime fechaActualizacion,

        // Anulación (borrado lógico)
        boolean anulado,
        String motivoAnulacion,
        String anuladoPor,
        LocalDateTime fechaAnulacion
) {}
