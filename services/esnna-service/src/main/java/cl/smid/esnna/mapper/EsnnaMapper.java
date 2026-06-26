package cl.smid.esnna.mapper;

import cl.smid.esnna.domain.Semaforo;
import cl.smid.esnna.dto.*;
import cl.smid.esnna.entity.EsnnaEntity;
import cl.smid.esnna.entity.Imputado;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Conversiones entity <-> DTO. Hecho a mano (no MapStruct) porque el parseo de
 * fechas/edad provenientes de oficios legales es irregular y conviene tenerlo
 * explícito y en un solo lugar. Donde un valor no parsea, queda null y se
 * registra a DEBUG; NUNCA revienta el caso (los datos crudos del oficio son
 * impredecibles).
 */
@Component
public class EsnnaMapper {

    private static final Logger log = LoggerFactory.getLogger(EsnnaMapper.class);

    private static final Locale ES = Locale.forLanguageTag("es");
    private static final List<DateTimeFormatter> FORMATOS_FECHA = List.of(
            DateTimeFormatter.ofPattern("dd-MM-yyyy"),
            DateTimeFormatter.ofPattern("dd/MM/yyyy"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd"),
            DateTimeFormatter.ofPattern("d 'de' MMMM 'de' yyyy", ES),
            DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy", ES)
    );
    private static final Pattern SOLO_DIGITOS = Pattern.compile("\\d{1,3}");

    // ============================================================
    // Consolidado del draft + overrides del request -> Entity (al guardar)
    // ============================================================

    /**
     * Construye la entidad a persistir tomando el CONSOLIDADO del draft como base
     * (todo lo que la IA extrajo no se pierde), y aplicando encima solo los campos
     * que el humano editó (los no-null del request). Los datos de análisis
     * inmutables (semaforoIa/justificacion/confianza) los provee el servicio desde
     * el draft, no el cliente.
     */
    public EsnnaEntity toEntity(EsnnaConsolidadoDTO c,
                                EsnnaCasoCreateRequest r,
                                Semaforo semaforoIa,
                                String justificacionIa,
                                Double confianzaIa,
                                String creadoPor) {
        EsnnaEntity e = EsnnaEntity.builder()
                .semaforoIa(semaforoIa)
                .justificacionIa(justificacionIa)
                .confianzaIa(confianzaIa)
                .semaforoFinal(r.semaforoFinal())
                .semaforoFinalAutor(creadoPor)
                .semaforoFinalFecha(LocalDateTime.now())
                .creadoPor(creadoPor)
                .paraQuerella(c.paraQuerella())
                .requerimiento(c.requerimiento())
                .nroCorrelativo(c.nroCorrelativo())
                .fecha(parseFecha(c.fecha()))
                .nroOficio(c.nroOficio())
                .carpeta(c.carpeta())
                .region(c.region())
                .tipoPrograma(c.tipoPrograma())
                .nombreProgramaResidencia(c.nombreProgramaResidencia())
                .delitoConcreto(c.delitoConcreto())
                .nnaBajoCuidadoEstado(c.nnaBajoCuidadoEstado())
                .residencia(c.residencia())
                .denunciante(c.denunciante())
                .contactoDenunciante(c.contactoDenunciante())
                .nna(c.nna())
                .sexoNna(c.sexoNna())
                .cedulaNna(c.cedulaNna())
                .nacionalidadNna(c.nacionalidadNna())
                .fechaNacimiento(parseFecha(c.fechaNacimiento()))
                .edad(parseEdad(c.edad()))
                .consumoDrogasAlcohol(c.consumoDrogasAlcohol())
                .curador(c.curador())
                .nadPma(c.nadPma())
                .contactoNadPma(c.contactoNadPma())
                .imputadoConocido(c.imputadoConocido())
                .lugarOcurrenciaHechos(c.lugarOcurrenciaHechos())
                .comunasInvolucradas(c.comunasInvolucradas())
                .hechos(c.hechos())
                .tipoViolencia(c.tipoViolencia())
                .redesSocialesMencionadas(c.redesSocialesMencionadas())
                .identificacionLocalesBaresHoteles(c.identificacionLocalesBaresHoteles())
                .presuntaRedExplotacion(c.presuntaRedExplotacion())
                .observacion(c.observacion())
                .querella(c.querella())
                .denunciasAnteriores(c.denunciasAnteriores())
                .rucAsociados(c.rucAsociados())
                .gestiones(c.gestiones())
                .descripcion(c.descripcion())
                .pendiente(c.pendiente())
                .build();
        aplicarImputados(e, c.imputados());

        // El humano corrige sobre la base de la IA: solo lo no-null pisa.
        aplicarOverrides(e, r);
        return e;
    }

    /** Aplica sobre la entidad (ya poblada desde el consolidado) los campos no nulos del request. */
    private void aplicarOverrides(EsnnaEntity e, EsnnaCasoCreateRequest r) {
        if (r.paraQuerella() != null) e.setParaQuerella(r.paraQuerella());
        if (r.requerimiento() != null) e.setRequerimiento(r.requerimiento());
        if (r.nroCorrelativo() != null) e.setNroCorrelativo(r.nroCorrelativo());
        if (r.fecha() != null) e.setFecha(parseFecha(r.fecha()));
        if (r.nroOficio() != null) e.setNroOficio(r.nroOficio());
        if (r.carpeta() != null) e.setCarpeta(r.carpeta());
        if (r.region() != null) e.setRegion(r.region());
        if (r.tipoPrograma() != null) e.setTipoPrograma(r.tipoPrograma());
        if (r.nombreProgramaResidencia() != null) e.setNombreProgramaResidencia(r.nombreProgramaResidencia());
        if (r.delitoConcreto() != null) e.setDelitoConcreto(r.delitoConcreto());
        if (r.nnaBajoCuidadoEstado() != null) e.setNnaBajoCuidadoEstado(r.nnaBajoCuidadoEstado());
        if (r.residencia() != null) e.setResidencia(r.residencia());
        if (r.denunciante() != null) e.setDenunciante(r.denunciante());
        if (r.contactoDenunciante() != null) e.setContactoDenunciante(r.contactoDenunciante());
        if (r.nna() != null) e.setNna(r.nna());
        if (r.sexoNna() != null) e.setSexoNna(r.sexoNna());
        if (r.cedulaNna() != null) e.setCedulaNna(r.cedulaNna());
        if (r.nacionalidadNna() != null) e.setNacionalidadNna(r.nacionalidadNna());
        if (r.fechaNacimiento() != null) e.setFechaNacimiento(parseFecha(r.fechaNacimiento()));
        if (r.edad() != null) e.setEdad(parseEdad(r.edad()));
        if (r.consumoDrogasAlcohol() != null) e.setConsumoDrogasAlcohol(r.consumoDrogasAlcohol());
        if (r.curador() != null) e.setCurador(r.curador());
        if (r.nadPma() != null) e.setNadPma(r.nadPma());
        if (r.contactoNadPma() != null) e.setContactoNadPma(r.contactoNadPma());
        if (r.imputadoConocido() != null) e.setImputadoConocido(r.imputadoConocido());
        if (r.imputados() != null) { e.clearImputados(); aplicarImputados(e, r.imputados()); }
        if (r.lugarOcurrenciaHechos() != null) e.setLugarOcurrenciaHechos(r.lugarOcurrenciaHechos());
        if (r.comunasInvolucradas() != null) e.setComunasInvolucradas(r.comunasInvolucradas());
        if (r.hechos() != null) e.setHechos(r.hechos());
        if (r.tipoViolencia() != null) e.setTipoViolencia(r.tipoViolencia());
        if (r.redesSocialesMencionadas() != null) e.setRedesSocialesMencionadas(r.redesSocialesMencionadas());
        if (r.identificacionLocalesBaresHoteles() != null) e.setIdentificacionLocalesBaresHoteles(r.identificacionLocalesBaresHoteles());
        if (r.presuntaRedExplotacion() != null) e.setPresuntaRedExplotacion(r.presuntaRedExplotacion());
        if (r.observacion() != null) e.setObservacion(r.observacion());
        if (r.querella() != null) e.setQuerella(r.querella());
        if (r.denunciasAnteriores() != null) e.setDenunciasAnteriores(r.denunciasAnteriores());
        if (r.rucAsociados() != null) e.setRucAsociados(r.rucAsociados());
        if (r.gestiones() != null) e.setGestiones(r.gestiones());
        if (r.descripcion() != null) e.setDescripcion(r.descripcion());
        if (r.pendiente() != null) e.setPendiente(r.pendiente());
    }

    // ============================================================
    // PATCH parcial sobre entidad existente
    // ============================================================

    /** Aplica solo los campos no nulos. Devuelve true si semaforoFinal cambió. */
    public boolean applyPatch(EsnnaEntity e, EsnnaCasoUpdateRequest r, String autor) {
        boolean overrideSemaforo = false;
        if (r.semaforoFinal() != null && r.semaforoFinal() != e.getSemaforoFinal()) {
            e.setSemaforoFinal(r.semaforoFinal());
            e.setSemaforoFinalAutor(autor);
            e.setSemaforoFinalFecha(LocalDateTime.now());
            overrideSemaforo = true;
        }
        if (r.paraQuerella() != null) e.setParaQuerella(r.paraQuerella());
        if (r.requerimiento() != null) e.setRequerimiento(r.requerimiento());
        if (r.nroCorrelativo() != null) e.setNroCorrelativo(r.nroCorrelativo());
        if (r.fecha() != null) e.setFecha(parseFecha(r.fecha()));
        if (r.nroOficio() != null) e.setNroOficio(r.nroOficio());
        if (r.carpeta() != null) e.setCarpeta(r.carpeta());
        if (r.region() != null) e.setRegion(r.region());
        if (r.tipoPrograma() != null) e.setTipoPrograma(r.tipoPrograma());
        if (r.nombreProgramaResidencia() != null) e.setNombreProgramaResidencia(r.nombreProgramaResidencia());
        if (r.delitoConcreto() != null) e.setDelitoConcreto(r.delitoConcreto());
        if (r.nnaBajoCuidadoEstado() != null) e.setNnaBajoCuidadoEstado(r.nnaBajoCuidadoEstado());
        if (r.residencia() != null) e.setResidencia(r.residencia());
        if (r.denunciante() != null) e.setDenunciante(r.denunciante());
        if (r.contactoDenunciante() != null) e.setContactoDenunciante(r.contactoDenunciante());
        if (r.nna() != null) e.setNna(r.nna());
        if (r.sexoNna() != null) e.setSexoNna(r.sexoNna());
        if (r.cedulaNna() != null) e.setCedulaNna(r.cedulaNna());
        if (r.nacionalidadNna() != null) e.setNacionalidadNna(r.nacionalidadNna());
        if (r.fechaNacimiento() != null) e.setFechaNacimiento(parseFecha(r.fechaNacimiento()));
        if (r.edad() != null) e.setEdad(parseEdad(r.edad()));
        if (r.consumoDrogasAlcohol() != null) e.setConsumoDrogasAlcohol(r.consumoDrogasAlcohol());
        if (r.curador() != null) e.setCurador(r.curador());
        if (r.nadPma() != null) e.setNadPma(r.nadPma());
        if (r.contactoNadPma() != null) e.setContactoNadPma(r.contactoNadPma());
        if (r.imputadoConocido() != null) e.setImputadoConocido(r.imputadoConocido());
        if (r.imputados() != null) { e.clearImputados(); aplicarImputados(e, r.imputados()); }
        if (r.lugarOcurrenciaHechos() != null) e.setLugarOcurrenciaHechos(r.lugarOcurrenciaHechos());
        if (r.comunasInvolucradas() != null) e.setComunasInvolucradas(r.comunasInvolucradas());
        if (r.hechos() != null) e.setHechos(r.hechos());
        if (r.tipoViolencia() != null) e.setTipoViolencia(r.tipoViolencia());
        if (r.redesSocialesMencionadas() != null) e.setRedesSocialesMencionadas(r.redesSocialesMencionadas());
        if (r.identificacionLocalesBaresHoteles() != null) e.setIdentificacionLocalesBaresHoteles(r.identificacionLocalesBaresHoteles());
        if (r.presuntaRedExplotacion() != null) e.setPresuntaRedExplotacion(r.presuntaRedExplotacion());
        if (r.observacion() != null) e.setObservacion(r.observacion());
        if (r.querella() != null) e.setQuerella(r.querella());
        if (r.denunciasAnteriores() != null) e.setDenunciasAnteriores(r.denunciasAnteriores());
        if (r.rucAsociados() != null) e.setRucAsociados(r.rucAsociados());
        if (r.gestiones() != null) e.setGestiones(r.gestiones());
        if (r.descripcion() != null) e.setDescripcion(r.descripcion());
        if (r.pendiente() != null) e.setPendiente(r.pendiente());
        return overrideSemaforo;
    }

    private void aplicarImputados(EsnnaEntity e, List<ImputadoDTO> dtos) {
        if (dtos == null) return;
        int orden = 1;
        for (ImputadoDTO d : dtos) {
            if (d == null) continue;
            Imputado imp = Imputado.builder()
                    .orden(d.orden() != null ? d.orden() : orden)
                    .nombre(d.nombre())
                    .rut(d.rut())
                    .domicilio(d.domicilio())
                    .sexo(d.sexo())
                    .esFuncionarioPublico(d.esFuncionarioPublico())
                    .build();
            e.addImputado(imp);
            orden++;
        }
    }

    // ============================================================
    // Entity -> DTO de salida
    // ============================================================

    public EsnnaResumenDTO toResumen(EsnnaEntity e) {
        return new EsnnaResumenDTO(
                e.getId(), e.getNroOficio(), e.getSemaforoFinal(), e.getSemaforoIa(),
                e.getRegion(), e.getDelitoConcreto(), e.getEdad(), e.getSexoNna(),
                e.getEstadoGestion(), e.getFechaIngreso(), e.getFechaActualizacion());
    }

    public EsnnaDetalleDTO toDetalle(EsnnaEntity e) {
        List<ImputadoDTO> imputados = e.getImputados().stream()
                .map(i -> new ImputadoDTO(i.getOrden(), i.getNombre(), i.getRut(),
                        i.getDomicilio(), i.getSexo(), i.getEsFuncionarioPublico()))
                .toList();
        return new EsnnaDetalleDTO(
                e.getId(), e.getVersion(),
                e.getSemaforoIa(), e.getJustificacionIa(), e.getConfianzaIa(),
                e.getSemaforoFinal(), e.getSemaforoFinalAutor(), e.getSemaforoFinalFecha(),
                e.getParaQuerella(), e.getRequerimiento(), e.getNroCorrelativo(), e.getFecha(),
                e.getNroOficio(), e.getCarpeta(), e.getRegion(), e.getTipoPrograma(),
                e.getNombreProgramaResidencia(), e.getDelitoConcreto(), e.getNnaBajoCuidadoEstado(),
                e.getResidencia(), e.getDenunciante(), e.getContactoDenunciante(),
                e.getNna(), e.getSexoNna(), e.getCedulaNna(), e.getNacionalidadNna(),
                e.getFechaNacimiento(), e.getEdad(), e.getConsumoDrogasAlcohol(), e.getCurador(),
                e.getNadPma(), e.getContactoNadPma(), e.getImputadoConocido(), imputados,
                e.getLugarOcurrenciaHechos(), e.getComunasInvolucradas(), e.getHechos(),
                e.getTipoViolencia(), e.getRedesSocialesMencionadas(),
                e.getIdentificacionLocalesBaresHoteles(), e.getPresuntaRedExplotacion(),
                e.getObservacion(), e.getQuerella(), e.getDenunciasAnteriores(), e.getRucAsociados(),
                e.getGestiones(), e.getDescripcion(), e.getPendiente(),
                e.getEstadoGestion(), e.getCreadoPor(), e.getFechaIngreso(), e.getFechaActualizacion(),
                e.isAnulado(), e.getMotivoAnulacion(), e.getAnuladoPor(), e.getFechaAnulacion());
    }

    // ============================================================
    // Parsers tolerantes
    // ============================================================

    public LocalDate parseFecha(String v) {
        if (v == null || v.isBlank()) return null;
        String s = v.trim();
        for (DateTimeFormatter f : FORMATOS_FECHA) {
            try {
                return LocalDate.parse(s.toLowerCase(ES), f);
            } catch (Exception ignored) {
                // siguiente formato
            }
        }
        log.debug("Fecha no parseable, queda null: '{}'", s);
        return null;
    }

    public Integer parseEdad(String v) {
        if (v == null || v.isBlank()) return null;
        Matcher m = SOLO_DIGITOS.matcher(v);
        if (m.find()) {
            try {
                int edad = Integer.parseInt(m.group());
                if (edad >= 0 && edad <= 120) return edad;
                log.debug("Edad fuera de rango plausible, queda null: '{}'", v);
            } catch (NumberFormatException ignored) {
                // cae a null
            }
        }
        return null;
    }
}