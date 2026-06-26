package cl.smid.antecedentes.infraestructura.persistencia;

import cl.smid.antecedentes.dominio.modelo.Referencia;
import cl.smid.antecedentes.dominio.modelo.TipoReferencia;
import org.springframework.stereotype.Component;

/**
 * Conversion entre {@link Referencia} (dominio) y las entidades de referencia (que comparten
 * {@link ReferenciaEntityBase}). El cifrado no aplica aqui.
 */
@Component
public class MapeadorReferencia {

    /** Entidad -&gt; dominio. El {@code tipo} lo aporta el adaptador segun la tabla consultada. */
    public Referencia aDominio(ReferenciaEntityBase e, TipoReferencia tipo) {
        return new Referencia(
                e.getAltKey(), tipo, e.getCodigo(), e.getNombre(), e.isVigente(),
                TiempoUtc.aInstante(e.getCreadoEn()), TiempoUtc.aInstante(e.getActualizadoEn()));
    }

    /** Inicializa una entidad nueva con todos los campos del dominio. */
    public void poblarNueva(ReferenciaEntityBase e, Referencia dom) {
        e.setAltKey(dom.altKey());
        e.setCodigo(dom.codigo());
        e.setNombre(dom.nombre());
        e.setVigente(dom.vigente());
        e.setCreadoEn(TiempoUtc.aLocal(dom.creadoEn()));
        e.setActualizadoEn(TiempoUtc.aLocal(dom.actualizadoEn()));
    }

    /** Aplica los campos mutables del dominio sobre una entidad existente. */
    public void actualizar(ReferenciaEntityBase e, Referencia dom) {
        e.setNombre(dom.nombre());
        e.setVigente(dom.vigente());
        e.setActualizadoEn(TiempoUtc.aLocal(dom.actualizadoEn()));
    }
}
