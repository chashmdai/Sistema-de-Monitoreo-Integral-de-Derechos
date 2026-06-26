package cl.smid.catalogo.infraestructura.persistencia.adaptador;

import cl.smid.catalogo.dominio.modelo.Causa;
import cl.smid.catalogo.dominio.modelo.Derecho;
import cl.smid.catalogo.infraestructura.persistencia.CausaEntity;
import cl.smid.catalogo.infraestructura.persistencia.DerechoEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * Traduce entre las entidades JPA de infraestructura y los modelos de dominio.
 *
 * <p>Mantener esta frontera explícita evita que las anotaciones de persistencia y el ciclo de
 * vida de las entidades gestionadas se filtren al núcleo, y permite que el dominio evolucione
 * con independencia del esquema físico.</p>
 */
@Component
public class MapeadorPersistencia {

    // ----------------------------- Derecho -----------------------------

    /** Reconstruye el modelo de dominio a partir de la fila persistida. */
    @NonNull
    public Derecho aDominio(DerechoEntity e) {
        return new Derecho(
                e.getId(),
                e.getIdPadre(),
                e.getAltKey(),
                e.getCodigo(),
                e.getNombre(),
                e.getDescripcion(),
                e.getNivel(),
                e.getOrden(),
                e.isVigente(),
                e.getVigenteDesde(),
                e.getVigenteHasta());
    }

    /**
     * Proyecta el modelo de dominio a entidad para persistir. Copia el {@code id} (si lo hay)
     * de modo que {@code save} actúe como inserción (id nulo) o actualización (id presente).
     */
    @NonNull
    public DerechoEntity aEntidad(Derecho d) {
        DerechoEntity e = new DerechoEntity();
        e.setId(d.getId());
        e.setIdPadre(d.getIdPadre());
        e.setAltKey(d.getAltKey());
        e.setCodigo(d.getCodigo());
        e.setNombre(d.getNombre());
        e.setDescripcion(d.getDescripcion());
        e.setNivel(d.getNivel());
        e.setOrden(d.getOrden());
        e.setVigente(d.estaVigente());
        e.setVigenteDesde(d.getVigenteDesde());
        e.setVigenteHasta(d.getVigenteHasta());
        return e;
    }

    @NonNull
    public List<DerechoEntity> aEntidades(List<Derecho> derechos) {
        return Objects.requireNonNull(derechos.stream().map(this::aEntidad).toList());
    }

    // ----------------------------- Causa -----------------------------

    @NonNull
    public Causa aDominio(CausaEntity e) {
        return new Causa(
                e.getId(),
                e.getIdDerecho(),
                e.getAltKey(),
                e.getCodigo(),
                e.getNombre(),
                e.isVigente());
    }

    @NonNull
    public CausaEntity aEntidad(Causa c) {
        CausaEntity e = new CausaEntity();
        e.setId(c.getId());
        e.setIdDerecho(c.getIdDerecho());
        e.setAltKey(c.getAltKey());
        e.setCodigo(c.getCodigo());
        e.setNombre(c.getNombre());
        e.setVigente(c.estaVigente());
        return e;
    }
}
