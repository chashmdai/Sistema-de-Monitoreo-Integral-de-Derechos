package cl.smid.requerimientos.infraestructura.persistencia.adaptador;

import cl.smid.requerimientos.dominio.modelo.AccionAdmisibilidad;
import cl.smid.requerimientos.dominio.modelo.Admisibilidad;
import cl.smid.requerimientos.dominio.modelo.Anexo;
import cl.smid.requerimientos.dominio.modelo.CanalIngreso;
import cl.smid.requerimientos.dominio.modelo.Complejidad;
import cl.smid.requerimientos.dominio.modelo.DerechoVulnerado;
import cl.smid.requerimientos.dominio.modelo.EstadoRequerimiento;
import cl.smid.requerimientos.dominio.modelo.Folio;
import cl.smid.requerimientos.dominio.modelo.NnaAfectado;
import cl.smid.requerimientos.dominio.modelo.Requerimiento;
import cl.smid.requerimientos.dominio.modelo.Urgencia;
import cl.smid.requerimientos.infraestructura.persistencia.entidad.AdmisibilidadEntity;
import cl.smid.requerimientos.infraestructura.persistencia.entidad.RequerimientoAnexoEntity;
import cl.smid.requerimientos.infraestructura.persistencia.entidad.RequerimientoEntity;
import cl.smid.requerimientos.infraestructura.persistencia.entidad.RequerimientoNnaDerechoEntity;
import cl.smid.requerimientos.infraestructura.persistencia.entidad.RequerimientoNnaEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Mapeador explícito entre el agregado de dominio {@link Requerimiento} y su grafo de entidades
 * JPA. Mantiene el dominio libre de JPA. La estrategia de actualización aprovecha que la API solo
 * <b>agrega</b> hijos (no edita ni borra líneas): al actualizar, se refrescan los escalares y se
 * <b>anexan los hijos nuevos</b> (los que aún no tienen handle de persistencia), preservando los
 * existentes.
 */
@Component
public class MapeadorRequerimiento {

    private final MapeadorSnapshot mapeadorSnapshot;

    public MapeadorRequerimiento(MapeadorSnapshot mapeadorSnapshot) {
        this.mapeadorSnapshot = mapeadorSnapshot;
    }

    // ------------------------------------------------------------------------
    //  Dominio -> Entidad (alta)
    // ------------------------------------------------------------------------

    /**
     * Construye un grafo de entidades nuevo a partir de un agregado recién creado.
     *
     * @param req agregado de dominio (sin persistir)
     * @return la entidad raíz con sus hijos listos para insertar
     */
    public RequerimientoEntity aEntidadNueva(Requerimiento req) {
        RequerimientoEntity e = new RequerimientoEntity();
        copiarEscalares(e, req);
        for (NnaAfectado nna : req.nnas()) {
            e.agregarNna(aEntidadNna(nna));
        }
        for (Anexo anexo : req.anexos()) {
            e.agregarAnexo(aEntidadAnexo(anexo));
        }
        for (Admisibilidad adm : req.admisibilidades()) {
            e.agregarAdmisibilidad(aEntidadAdmisibilidad(adm));
        }
        return e;
    }

    // ------------------------------------------------------------------------
    //  Dominio -> Entidad (actualización: escalares + anexar hijos nuevos)
    // ------------------------------------------------------------------------

    /**
     * Actualiza una entidad gestionada con el estado del agregado: refresca los escalares y anexa
     * únicamente los hijos nuevos (sin handle de persistencia), conservando los existentes.
     *
     * @param e   entidad gestionada (cargada desde la BD)
     * @param req agregado de dominio con los cambios
     */
    public void actualizarEntidad(RequerimientoEntity e, Requerimiento req) {
        copiarEscalares(e, req);
        for (NnaAfectado nna : req.nnas()) {
            if (nna.esNuevo()) {
                e.agregarNna(aEntidadNna(nna));
            }
        }
        for (Anexo anexo : req.anexos()) {
            if (anexo.idInterno() == null) {
                e.agregarAnexo(aEntidadAnexo(anexo));
            }
        }
        for (Admisibilidad adm : req.admisibilidades()) {
            if (adm.idInterno() == null) {
                e.agregarAdmisibilidad(aEntidadAdmisibilidad(adm));
            }
        }
    }

    private void copiarEscalares(RequerimientoEntity e, Requerimiento req) {
        e.setAltKey(req.altKey());
        e.setFolio(req.folio().valor());
        e.setIdSede(req.idSedeAlt());
        e.setIdUnidadDestino(req.idUnidadDestinoAlt());
        e.setEstado(req.estado().name());
        e.setCanal(req.canal() == null ? null : req.canal().name());
        e.setComplejidad(req.complejidad() == null ? null : req.complejidad().name());
        e.setUrgencia(req.urgencia() == null ? null : req.urgencia().name());
        e.setRequiereFichaReservada(req.requiereFichaReservada());
        e.setIdRequirenteAlt(req.idRequirenteAlt());
        e.setRequirenteSnapshot(mapeadorSnapshot.aJson(req.requirenteSnapshot()));
        e.setResumen(req.resumen());
        e.setFechaIngreso(req.fechaIngreso());
        e.setEsBeta(req.esBeta());
        e.setVigente(req.vigente());
        e.setCreadoEn(req.creadoEn());
        e.setActualizadoEn(req.actualizadoEn());
        e.setCreadoPor(req.creadoPor());
    }

    private RequerimientoNnaEntity aEntidadNna(NnaAfectado nna) {
        RequerimientoNnaEntity e = new RequerimientoNnaEntity();
        e.setIdPersonaAlt(nna.idPersonaAlt());
        e.setPersonaSnapshot(mapeadorSnapshot.aJson(nna.snapshot()));
        for (DerechoVulnerado d : nna.derechos()) {
            RequerimientoNnaDerechoEntity de = new RequerimientoNnaDerechoEntity();
            de.setIdDerechoAlt(d.idDerechoAlt());
            de.setIdCausaAlt(d.idCausaAlt());
            e.agregarDerecho(de);
        }
        return e;
    }

    private RequerimientoAnexoEntity aEntidadAnexo(Anexo anexo) {
        RequerimientoAnexoEntity e = new RequerimientoAnexoEntity();
        e.setNombreArchivo(anexo.nombreArchivo());
        e.setTipoMime(anexo.tipoMime());
        e.setTamanoBytes(anexo.tamanoBytes());
        e.setReferenciaExterna(anexo.referenciaExterna());
        return e;
    }

    private AdmisibilidadEntity aEntidadAdmisibilidad(Admisibilidad adm) {
        AdmisibilidadEntity e = new AdmisibilidadEntity();
        e.setAccion(adm.accion().name());
        e.setIdCoordinadorAlt(adm.idCoordinadorAlt());
        e.setEscaladoADefensora(adm.escaladoADefensora());
        e.setIdProfesionalAsignadoAlt(adm.idProfesionalAsignadoAlt());
        e.setObservacion(adm.observacion());
        e.setDecididoEn(adm.decididoEn());
        return e;
    }

    // ------------------------------------------------------------------------
    //  Entidad -> Dominio (rehidratación)
    // ------------------------------------------------------------------------

    /**
     * Rehidrata el agregado de dominio desde su grafo de entidades.
     *
     * @param e entidad raíz cargada (con hijos)
     * @return el agregado de dominio
     */
    public Requerimiento aDominio(RequerimientoEntity e) {
        List<NnaAfectado> nnas = new ArrayList<>();
        for (RequerimientoNnaEntity ne : e.getNnas()) {
            List<DerechoVulnerado> derechos = new ArrayList<>();
            for (RequerimientoNnaDerechoEntity de : ne.getDerechos()) {
                derechos.add(new DerechoVulnerado(de.getId(), de.getIdDerechoAlt(), de.getIdCausaAlt()));
            }
            nnas.add(new NnaAfectado(ne.getId(), ne.getIdPersonaAlt(),
                    mapeadorSnapshot.desdeJson(ne.getPersonaSnapshot()), derechos));
        }

        List<Anexo> anexos = new ArrayList<>();
        for (RequerimientoAnexoEntity ae : e.getAnexos()) {
            anexos.add(new Anexo(ae.getId(), ae.getNombreArchivo(), ae.getTipoMime(),
                    ae.getTamanoBytes(), ae.getReferenciaExterna()));
        }

        List<Admisibilidad> admisibilidades = new ArrayList<>();
        for (AdmisibilidadEntity de : e.getAdmisibilidades()) {
            admisibilidades.add(new Admisibilidad(de.getId(),
                    AccionAdmisibilidad.valueOf(de.getAccion()), de.getIdCoordinadorAlt(),
                    de.isEscaladoADefensora(), de.getIdProfesionalAsignadoAlt(),
                    de.getObservacion(), de.getDecididoEn()));
        }

        return new Requerimiento(
                e.getId(), e.getAltKey(), new Folio(e.getFolio()), e.getIdSede(), e.getIdUnidadDestino(),
                EstadoRequerimiento.valueOf(e.getEstado()),
                e.getCanal() == null ? null : CanalIngreso.valueOf(e.getCanal()),
                e.getComplejidad() == null ? null : Complejidad.valueOf(e.getComplejidad()),
                e.getUrgencia() == null ? null : Urgencia.valueOf(e.getUrgencia()),
                e.isRequiereFichaReservada(), e.getIdRequirenteAlt(),
                mapeadorSnapshot.desdeJson(e.getRequirenteSnapshot()), e.getResumen(),
                e.getFechaIngreso(), e.isEsBeta(), e.isVigente(), e.getCreadoEn(),
                e.getActualizadoEn(), e.getCreadoPor(), nnas, anexos, admisibilidades);
    }
}
