package cl.smid.requerimientos.api;

import cl.smid.requerimientos.api.dto.AdmisibilidadDTO;
import cl.smid.requerimientos.api.dto.AnexoDTO;
import cl.smid.requerimientos.api.dto.DerechoVulneradoDTO;
import cl.smid.requerimientos.api.dto.NnaAfectadoDTO;
import cl.smid.requerimientos.api.dto.PaginaDTO;
import cl.smid.requerimientos.api.dto.RequerimientoDTO;
import cl.smid.requerimientos.api.dto.ResumenRequerimientoDTO;
import cl.smid.requerimientos.api.dto.SnapshotDTO;
import cl.smid.requerimientos.dominio.modelo.Admisibilidad;
import cl.smid.requerimientos.dominio.modelo.Anexo;
import cl.smid.requerimientos.dominio.modelo.DerechoVulnerado;
import cl.smid.requerimientos.dominio.modelo.NnaAfectado;
import cl.smid.requerimientos.dominio.modelo.PaginaDominio;
import cl.smid.requerimientos.dominio.modelo.Requerimiento;
import cl.smid.requerimientos.dominio.modelo.SnapshotPersona;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Traduce los agregados de dominio a los DTO públicos. Garantiza que la PK interna nunca se exponga
 * (cierre IDOR, Núcleo 2.2): los DTO solo llevan alt_key.
 */
@Component
public class MapeadorRespuesta {

    /**
     * Mapea el agregado completo a su DTO de detalle.
     *
     * @param req agregado de dominio
     * @return el DTO de detalle
     */
    public RequerimientoDTO aDetalle(Requerimiento req) {
        List<NnaAfectadoDTO> nnas = req.nnas().stream().map(this::aNnaDto).toList();
        List<AnexoDTO> anexos = req.anexos().stream().map(this::aAnexoDto).toList();
        List<AdmisibilidadDTO> admisibilidades = req.admisibilidades().stream()
                .map(this::aAdmisibilidadDto).toList();

        return new RequerimientoDTO(
                req.altKey(),
                req.folio().valor(),
                req.idSedeAlt(),
                req.idUnidadDestinoAlt(),
                req.estado(),
                req.canal(),
                req.complejidad(),
                req.urgencia(),
                req.requiereFichaReservada(),
                req.idRequirenteAlt(),
                aSnapshotDto(req.requirenteSnapshot()),
                req.resumen(),
                req.fechaIngreso(),
                req.esBeta(),
                req.vigente(),
                req.creadoEn(),
                req.actualizadoEn(),
                nnas,
                anexos,
                admisibilidades);
    }

    /**
     * Mapea el agregado a su proyección liviana para listados.
     *
     * @param req agregado de dominio
     * @return el DTO de resumen
     */
    public ResumenRequerimientoDTO aResumen(Requerimiento req) {
        return new ResumenRequerimientoDTO(
                req.altKey(),
                req.folio().valor(),
                req.estado(),
                req.idUnidadDestinoAlt(),
                req.urgencia(),
                req.complejidad(),
                req.requiereFichaReservada(),
                req.creadoEn());
    }

    /**
     * Mapea una página de dominio a una página de resúmenes para la API.
     *
     * @param pagina página de dominio
     * @return la página de DTO de resumen
     */
    public PaginaDTO<ResumenRequerimientoDTO> aPaginaResumen(PaginaDominio<Requerimiento> pagina) {
        List<ResumenRequerimientoDTO> contenido = pagina.contenido().stream().map(this::aResumen).toList();
        return new PaginaDTO<>(contenido, pagina.pagina(), pagina.tamano(), pagina.total());
    }

    private NnaAfectadoDTO aNnaDto(NnaAfectado nna) {
        List<DerechoVulneradoDTO> derechos = nna.derechos().stream().map(this::aDerechoDto).toList();
        return new NnaAfectadoDTO(nna.idPersonaAlt(), aSnapshotDto(nna.snapshot()), derechos);
    }

    private DerechoVulneradoDTO aDerechoDto(DerechoVulnerado d) {
        return new DerechoVulneradoDTO(d.idDerechoAlt(), d.idCausaAlt());
    }

    private AnexoDTO aAnexoDto(Anexo a) {
        return new AnexoDTO(a.nombreArchivo(), a.tipoMime(), a.tamanoBytes(), a.referenciaExterna());
    }

    private AdmisibilidadDTO aAdmisibilidadDto(Admisibilidad a) {
        return new AdmisibilidadDTO(a.accion(), a.idCoordinadorAlt(), a.escaladoADefensora(),
                a.idProfesionalAsignadoAlt(), a.observacion(), a.decididoEn());
    }

    private SnapshotDTO aSnapshotDto(SnapshotPersona s) {
        if (s == null) {
            return null;
        }
        return new SnapshotDTO(s.nombreLegible(), s.rut(), s.capturadoEn());
    }
}
