package cl.smid.esnna.repository;

import cl.smid.esnna.domain.EstadoGestion;
import cl.smid.esnna.domain.Semaforo;
import cl.smid.esnna.entity.EsnnaEntity;
import cl.smid.esnna.entity.Imputado;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

/**
 * Filtros combinables del tablero (REPO-1/FEAT). Cada método devuelve null si el
 * parámetro no viene, de modo que Specification.allOf los ignora. Por defecto se
 * excluyen los casos anulados (borrado lógico).
 */
public final class EsnnaSpecifications {

    private EsnnaSpecifications() {}

    public static Specification<EsnnaEntity> noAnulado() {
        return (root, q, cb) -> cb.isFalse(root.get("anulado"));
    }

    public static Specification<EsnnaEntity> conSemaforoFinal(Semaforo semaforo) {
        if (semaforo == null) return null;
        return (root, q, cb) -> cb.equal(root.get("semaforoFinal"), semaforo);
    }

    public static Specification<EsnnaEntity> conEstado(EstadoGestion estado) {
        if (estado == null) return null;
        return (root, q, cb) -> cb.equal(root.get("estadoGestion"), estado);
    }

    public static Specification<EsnnaEntity> conRegion(String region) {
        if (region == null || region.isBlank()) return null;
        return (root, q, cb) -> cb.equal(cb.lower(root.get("region")), region.toLowerCase());
    }

    public static Specification<EsnnaEntity> conDelito(String delito) {
        if (delito == null || delito.isBlank()) return null;
        return (root, q, cb) -> cb.like(cb.lower(root.get("delitoConcreto")), "%" + delito.toLowerCase() + "%");
    }

    public static Specification<EsnnaEntity> conRuc(String ruc) {
        if (ruc == null || ruc.isBlank()) return null;
        return (root, q, cb) -> cb.equal(root.get("rucAsociados"), ruc);
    }

    public static Specification<EsnnaEntity> conCedulaNna(String cedula) {
        if (cedula == null || cedula.isBlank()) return null;
        return (root, q, cb) -> cb.equal(root.get("cedulaNna"), cedula);
    }

    public static Specification<EsnnaEntity> conRutImputado(String rut) {
        if (rut == null || rut.isBlank()) return null;
        return (root, q, cb) -> {
            q.distinct(true);
            Join<EsnnaEntity, Imputado> join = root.join("imputados");
            return cb.equal(join.get("rut"), rut);
        };
    }

    public static Specification<EsnnaEntity> ingresadoDesde(LocalDateTime desde) {
        if (desde == null) return null;
        return (root, q, cb) -> cb.greaterThanOrEqualTo(root.get("fechaIngreso"), desde);
    }

    public static Specification<EsnnaEntity> ingresadoHasta(LocalDateTime hasta) {
        if (hasta == null) return null;
        return (root, q, cb) -> cb.lessThanOrEqualTo(root.get("fechaIngreso"), hasta);
    }
}
