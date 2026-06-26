package cl.smid.instituciones.api;

import cl.smid.instituciones.api.dto.InstitucionDTO;
import cl.smid.instituciones.api.dto.InstitucionResumenDTO;
import cl.smid.instituciones.api.dto.PaginaDTO;
import cl.smid.instituciones.api.dto.PuntoFocalDTO;
import cl.smid.instituciones.api.dto.TipoInstitucionDTO;
import cl.smid.instituciones.dominio.modelo.DetalleInstitucion;
import cl.smid.instituciones.dominio.modelo.Institucion;
import cl.smid.instituciones.dominio.modelo.Pagina;
import cl.smid.instituciones.dominio.modelo.PuntoFocal;
import cl.smid.instituciones.dominio.modelo.ResumenInstitucion;
import cl.smid.instituciones.dominio.modelo.TipoInstitucion;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Ensambla las respuestas públicas (DTO) a partir de los modelos de dominio. Centraliza
 * la traducción de objetos de valor (por ejemplo el RUT a su forma canónica) y los
 * enumerados a texto.
 */
@Component
public class EnsambladorRespuestas {

    public TipoInstitucionDTO aDto(TipoInstitucion tipo) {
        return new TipoInstitucionDTO(
                tipo.altKey(),
                tipo.nombre(),
                tipo.ambito().name(),
                tipo.descripcion(),
                tipo.vigente(),
                tipo.creadoEn(),
                tipo.actualizadoEn());
    }

    public PuntoFocalDTO aDto(PuntoFocal puntoFocal) {
        return new PuntoFocalDTO(
                puntoFocal.altKey(),
                puntoFocal.institucionAlt(),
                puntoFocal.nombre(),
                puntoFocal.cargo(),
                puntoFocal.email(),
                puntoFocal.telefono(),
                puntoFocal.principal(),
                puntoFocal.activo(),
                puntoFocal.creadoEn(),
                puntoFocal.actualizadoEn());
    }

    public InstitucionDTO aDto(DetalleInstitucion detalle) {
        Institucion i = detalle.institucion();
        List<PuntoFocalDTO> puntos = detalle.puntosFocales().stream().map(this::aDto).toList();
        return new InstitucionDTO(
                i.altKey(),
                i.codigo(),
                i.nombre(),
                i.tipoAlt(),
                detalle.tipoNombre(),
                detalle.ambito().name(),
                i.rut() == null ? null : i.rut().canonico(),
                i.regionCodigo(),
                i.comunaCodigo(),
                i.direccion(),
                i.telefono(),
                i.email(),
                i.sitioWeb(),
                i.activa(),
                i.creadoEn(),
                i.actualizadoEn(),
                puntos);
    }

    public InstitucionResumenDTO aResumen(ResumenInstitucion resumen) {
        Institucion i = resumen.institucion();
        return new InstitucionResumenDTO(
                i.altKey(),
                i.codigo(),
                i.nombre(),
                i.tipoAlt(),
                resumen.tipoNombre(),
                resumen.ambito().name(),
                i.rut() == null ? null : i.rut().canonico(),
                i.regionCodigo(),
                i.comunaCodigo(),
                i.activa(),
                i.creadoEn(),
                i.actualizadoEn());
    }

    public PaginaDTO<TipoInstitucionDTO> paginaTipos(Pagina<TipoInstitucion> pagina) {
        return new PaginaDTO<>(
                pagina.contenido().stream().map(this::aDto).toList(),
                pagina.pagina(),
                pagina.tamano(),
                pagina.totalElementos(),
                pagina.totalPaginas());
    }

    public PaginaDTO<InstitucionResumenDTO> paginaInstituciones(Pagina<ResumenInstitucion> pagina) {
        return new PaginaDTO<>(
                pagina.contenido().stream().map(this::aResumen).toList(),
                pagina.pagina(),
                pagina.tamano(),
                pagina.totalElementos(),
                pagina.totalPaginas());
    }
}
