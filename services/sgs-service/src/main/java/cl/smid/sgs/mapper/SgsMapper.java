package cl.smid.sgs.mapper;

import cl.smid.sgs.dto.out.*;
import cl.smid.sgs.entity.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Mapeo entity -> DTO de salida (DTO-1: nunca se serializa la entity).
 * Implementado como componente Java en vez de MapStruct para garantizar correctitud verificable
 * (sin paso de generación en build); puede migrarse a MapStruct sin tocar las firmas públicas.
 */
@Component
public class SgsMapper {

    public CatalogoDTO toCatalogo(Catalogo c) {
        if (c == null) return null;
        return new CatalogoDTO(c.getId(), c.getTipo(), c.getCodigo(), c.getEtiqueta(), c.isActivo());
    }

    private String etiqueta(Catalogo c) { return c == null ? null : c.getEtiqueta(); }

    public AccionOutDTO toAccion(Accion a) {
        return new AccionOutDTO(a.getId(), a.getOrden(), a.getDescripcion());
    }

    public SeguimientoDetalleDTO toSeguimiento(Seguimiento s) {
        return new SeguimientoDetalleDTO(
                s.getId(), s.getFase(), s.getFechaSeguimiento(),
                etiqueta(s.getTipoSeguimiento()), etiqueta(s.getTipoRespuesta()),
                s.getFechaRespuesta(), s.getOtroSeguimientoInstitucional(),
                s.getEvaluacionIA(), s.getValoracionRubrica(), s.getConfianza(), s.getRazonamiento(),
                s.isRequiereRevisionHumana(),
                s.getEvaluacionFinal(), s.getEvaluacionFinalAutor(), s.getEvaluacionFinalFecha(),
                s.getResponsableSeguimiento());
    }

    public RecomendacionDetalleDTO toRecomendacionDetalle(Recomendacion r) {
        List<AccionOutDTO> acciones = r.getAcciones().stream().map(this::toAccion).toList();
        List<SeguimientoDetalleDTO> segs = r.getSeguimientos().stream().map(this::toSeguimiento).toList();
        return new RecomendacionDetalleDTO(
                r.getId(), r.getCorrelativo(), r.getDimension(), r.getNudoCritico(), r.getTipoRecomendacion(),
                r.getVerbo(), r.getDescripcion(), r.getPlazo(), r.getPlazoRaw(), r.isGv(), r.getAcoge(),
                r.getEstado(), etiqueta(r.getMateria()), etiqueta(r.getCategoria()),
                r.getProfesionalResponsable(), r.getResponsableSeguimiento(), r.isAnulado(),
                acciones, segs);
    }

    public OficioDetalleDTO toOficioDetalle(Oficio o) {
        List<RecomendacionDetalleDTO> recs = o.getRecomendaciones().stream()
                .map(this::toRecomendacionDetalle).toList();
        return new OficioDetalleDTO(
                o.getId(), o.getNroOficio(), o.getRegion(), o.getInstitucion(), o.getResidenciaCentro(),
                o.getNivel(), o.getPdfHash(), o.getFechaIngreso(), o.getFechaActualizacion(), recs);
    }

    /** Fila de tablero. 'ultimo' = último seguimiento de la recomendación (o null). */
    public SgsResumenDTO toResumen(Recomendacion r, Seguimiento ultimo) {
        return new SgsResumenDTO(
                r.getOficio().getId(), r.getId(), r.getOficio().getNroOficio(),
                r.getOficio().getRegion(), r.getOficio().getInstitucion(), r.getDimension(),
                r.getCorrelativo(), r.getPlazo(), r.isGv(), r.getEstado(),
                ultimo == null ? null : (ultimo.getEvaluacionFinal() != null ? ultimo.getEvaluacionFinal() : ultimo.getEvaluacionIA()),
                ultimo != null && ultimo.isRequiereRevisionHumana(),
                r.getOficio().getFechaIngreso());
    }
}
