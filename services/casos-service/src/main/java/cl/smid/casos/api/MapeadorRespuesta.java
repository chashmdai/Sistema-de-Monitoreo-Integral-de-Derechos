package cl.smid.casos.api;

import cl.smid.casos.api.dto.CasoDetalleDTO;
import cl.smid.casos.api.dto.CasoResumenDTO;
import cl.smid.casos.api.dto.EnriquecimientoDTO;
import cl.smid.casos.api.dto.HistorialTransicionDTO;
import cl.smid.casos.api.dto.PaginaDTO;
import cl.smid.casos.dominio.modelo.Caso;
import cl.smid.casos.dominio.modelo.CasoEnriquecido;
import cl.smid.casos.dominio.modelo.DatosEnriquecimiento;
import cl.smid.casos.dominio.modelo.Pagina;
import cl.smid.casos.dominio.modelo.Transicion;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Traduce el dominio a DTOs de salida. Expone exclusivamente identificadores opacos {@code alt_key};
 * la PK numérica interna nunca aparece en las respuestas (cierre IDOR).
 */
@Component
public class MapeadorRespuesta {

    public CasoResumenDTO aResumen(Caso caso) {
        return new CasoResumenDTO(
                caso.altKey(),
                caso.numeroExpediente().valor(),
                caso.estado().name(),
                caso.complejidad() == null ? null : caso.complejidad().name(),
                caso.idSedeAlt(),
                caso.idUnidadAlt(),
                caso.idProfesionalResponsableAlt(),
                caso.requiereFichaReservada(),
                caso.esBeta(),
                caso.abiertoEn(),
                caso.cerradoEn(),
                caso.actualizadoEn());
    }

    public CasoDetalleDTO aDetalle(CasoEnriquecido enriquecido) {
        Caso caso = enriquecido.caso();
        List<HistorialTransicionDTO> historial = caso.historialCompleto().stream()
                .map(this::aHistorial)
                .toList();
        return new CasoDetalleDTO(
                caso.altKey(),
                caso.numeroExpediente().valor(),
                caso.idRequerimientoOrigenAlt(),
                caso.folioRequerimiento(),
                caso.estado().name(),
                caso.complejidad() == null ? null : caso.complejidad().name(),
                caso.idSedeAlt(),
                caso.idUnidadAlt(),
                caso.idProfesionalResponsableAlt(),
                caso.requiereFichaReservada(),
                caso.esBeta(),
                caso.abiertoEn(),
                caso.cerradoEn(),
                caso.creadoEn(),
                caso.actualizadoEn(),
                aEnriquecimiento(enriquecido.enriquecimiento()),
                historial);
    }

    public HistorialTransicionDTO aHistorial(Transicion t) {
        return new HistorialTransicionDTO(
                t.altKey(),
                t.estadoOrigen() == null ? null : t.estadoOrigen().name(),
                t.estadoDestino().name(),
                t.accion(),
                t.observacion(),
                t.actorAlt(),
                t.ocurridoEn());
    }

    public EnriquecimientoDTO aEnriquecimiento(DatosEnriquecimiento d) {
        return new EnriquecimientoDTO(d.disponible(), d.estadoRequerimiento(), d.canal(),
                d.cantidadNnaAfectados());
    }

    public PaginaDTO<CasoResumenDTO> aPagina(Pagina<Caso> pagina) {
        List<CasoResumenDTO> contenido = pagina.contenido().stream().map(this::aResumen).toList();
        return new PaginaDTO<>(contenido, pagina.pagina(), pagina.tamano(), pagina.total());
    }
}
