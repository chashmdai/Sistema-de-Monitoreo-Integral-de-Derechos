package cl.smid.antecedentes.api;

import cl.smid.antecedentes.api.dto.DocumentoResponse;
import cl.smid.antecedentes.api.dto.FichaDetalleResponse;
import cl.smid.antecedentes.api.dto.FichaResumenResponse;
import cl.smid.antecedentes.api.dto.HallazgoResponse;
import cl.smid.antecedentes.api.dto.HistorialResponse;
import cl.smid.antecedentes.api.dto.ReferenciaResponse;
import cl.smid.antecedentes.dominio.modelo.FichaAntecedente;
import cl.smid.antecedentes.dominio.modelo.Hallazgo;
import cl.smid.antecedentes.dominio.modelo.Referencia;
import cl.smid.antecedentes.dominio.modelo.ResumenFicha;
import org.springframework.stereotype.Component;

/**
 * Proyecta los modelos de dominio a los DTOs de respuesta. Los identificadores de referencia se
 * exponen como alt_key (UUID); el relato solo aparece en el detalle de la ficha.
 */
@Component
public class MapeadorRespuesta {

    public FichaDetalleResponse aDetalle(FichaAntecedente f) {
        return new FichaDetalleResponse(
                f.altKey(), f.folio(), f.estado(), f.unidadAlt(), f.sedeAlt(), f.profesionalAlt(), f.jefaturaAlt(),
                f.procesoAlt(), f.casoAlt(), f.categoriaPrincipalAlt(), f.categoriasSecundariasAlt(),
                f.derechosCdn(), f.descripcion(), f.relato(), f.calificacion(), f.criterios(),
                f.percepcionHallazgo(), f.hallazgoAlt(),
                f.documentos().stream()
                        .map(d -> new DocumentoResponse(d.altKey(), d.nombre(), d.referenciaExterna()))
                        .toList(),
                f.historial().stream()
                        .map(h -> new HistorialResponse(h.tipoEvento(), h.actorAlt(), h.ocurridoEn(), h.observacion()))
                        .toList(),
                f.creadoEn(), f.actualizadoEn());
    }

    public FichaResumenResponse aResumen(ResumenFicha r) {
        return new FichaResumenResponse(r.altKey(), r.folio(), r.estado(), r.calificacion(),
                r.percepcionHallazgo(), r.unidadAlt(), r.sedeAlt(), r.casoAlt(), r.creadoEn(), r.actualizadoEn());
    }

    public HallazgoResponse aHallazgo(Hallazgo h) {
        return new HallazgoResponse(h.altKey(), h.folio(), h.titulo(), h.descripcion(), h.estado(),
                h.instrumentoAlt(), h.temporalidad(), h.unidadesInvolucradas(), h.institucionesExternas(),
                h.origenFichaAlt(), h.creadoEn(), h.actualizadoEn());
    }

    public ReferenciaResponse aReferencia(Referencia r) {
        return new ReferenciaResponse(r.altKey(), r.codigo(), r.nombre(), r.vigente(),
                r.creadoEn(), r.actualizadoEn());
    }
}
