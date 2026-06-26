package cl.smid.casos.infraestructura.persistencia;

import cl.smid.casos.dominio.modelo.Caso;
import cl.smid.casos.dominio.modelo.NumeroExpediente;
import cl.smid.casos.dominio.modelo.Transicion;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Mapeo EXPLÍCITO entre el dominio (POJO puro) y las entidades JPA. Concentra la traducción para que
 * ni el dominio conozca JPA, ni las entidades conozcan reglas de negocio. La PK numérica interna no
 * cruza hacia el dominio.
 */
@Component
public class MapeadorCasoPersistencia {

    /** Reconstituye el agregado de dominio a partir de la entidad y sus asientos de transición. */
    public Caso aDominio(CasoEntity e, List<TransicionEntity> transiciones) {
        NumeroExpediente numero = new NumeroExpediente(
                e.getNumeroExpediente(), e.getCodigoSede(), e.getSerie(), e.getCorrelativo(), e.getAnio());
        List<Transicion> historial = transiciones.stream().map(this::aDominio).toList();
        return Caso.reconstituir(
                e.getAltKey(), numero, e.getIdRequerimientoOrigenAlt(), e.getFolioRequerimiento(),
                e.getIdSedeAlt(), e.getIdUnidadAlt(), e.getIdProfesionalResponsableAlt(), e.getEstado(),
                e.getComplejidad(), e.isRequiereFichaReservada(), e.isEsBeta(), e.getAbiertoEn(),
                e.getCerradoEn(), e.getCreadoEn(), e.getActualizadoEn(), e.getCreadoPor(), historial);
    }

    /**
     * Vuelca los campos del dominio sobre la entidad (para alta o actualización). No toca {@code id}
     * (lo gestiona JPA) ni inserta transiciones (eso lo hace el adaptador a partir de las nuevas).
     */
    public void volcar(Caso caso, CasoEntity e) {
        NumeroExpediente n = caso.numeroExpediente();
        e.setAltKey(caso.altKey());
        e.setNumeroExpediente(n.valor());
        e.setCodigoSede(n.codigoSede());
        e.setSerie(n.serie());
        e.setCorrelativo(n.correlativo());
        e.setAnio(n.anio());
        e.setIdRequerimientoOrigenAlt(caso.idRequerimientoOrigenAlt());
        e.setFolioRequerimiento(caso.folioRequerimiento());
        e.setIdSedeAlt(caso.idSedeAlt());
        e.setIdUnidadAlt(caso.idUnidadAlt());
        e.setIdProfesionalResponsableAlt(caso.idProfesionalResponsableAlt());
        e.setEstado(caso.estado());
        e.setComplejidad(caso.complejidad());
        e.setRequiereFichaReservada(caso.requiereFichaReservada());
        e.setEsBeta(caso.esBeta());
        e.setAbiertoEn(caso.abiertoEn());
        e.setCerradoEn(caso.cerradoEn());
        e.setCreadoEn(caso.creadoEn());
        e.setActualizadoEn(caso.actualizadoEn());
        e.setCreadoPor(caso.creadoPor());
        e.setVigente(true);
    }

    /** Traduce un asiento de transición de dominio a entidad, asociándolo al caso indicado. */
    public TransicionEntity aEntidad(Transicion t, Long idCaso) {
        TransicionEntity te = new TransicionEntity();
        te.setAltKey(t.altKey());
        te.setIdCaso(idCaso);
        te.setEstadoOrigen(t.estadoOrigen());
        te.setEstadoDestino(t.estadoDestino());
        te.setAccion(t.accion());
        te.setObservacion(t.observacion());
        te.setActorAlt(t.actorAlt());
        te.setOcurridoEn(t.ocurridoEn());
        return te;
    }

    /** Traduce un asiento de transición de entidad a dominio. */
    public Transicion aDominio(TransicionEntity te) {
        return new Transicion(te.getAltKey(), te.getEstadoOrigen(), te.getEstadoDestino(),
                te.getAccion(), te.getObservacion(), te.getActorAlt(), te.getOcurridoEn());
    }
}
