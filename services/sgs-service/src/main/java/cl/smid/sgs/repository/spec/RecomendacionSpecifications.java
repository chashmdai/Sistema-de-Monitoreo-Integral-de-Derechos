package cl.smid.sgs.repository.spec;

import cl.smid.sgs.entity.Oficio;
import cl.smid.sgs.entity.Recomendacion;
import cl.smid.sgs.enums.EstadoGestion;
import cl.smid.sgs.enums.PlazoRecomendacion;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

/** Filtros combinables del tablero (REP-1/REP-4): se componen con .and(). */
public final class RecomendacionSpecifications {

    private RecomendacionSpecifications() {}

    public static Specification<Recomendacion> noAnuladas() {
        return (root, q, cb) -> cb.isFalse(root.get("anulado"));
    }

    public static Specification<Recomendacion> estado(EstadoGestion estado) {
        return (root, q, cb) -> estado == null ? null : cb.equal(root.get("estado"), estado);
    }

    public static Specification<Recomendacion> plazo(PlazoRecomendacion plazo) {
        return (root, q, cb) -> plazo == null ? null : cb.equal(root.get("plazo"), plazo);
    }

    public static Specification<Recomendacion> gv(Boolean gv) {
        return (root, q, cb) -> gv == null ? null : cb.equal(root.get("gv"), gv);
    }

    public static Specification<Recomendacion> dimension(String dimension) {
        return (root, q, cb) -> blank(dimension) ? null
                : cb.like(cb.lower(root.get("dimension")), like(dimension));
    }

    public static Specification<Recomendacion> region(String region) {
        return (root, q, cb) -> {
            if (blank(region)) return null;
            Join<Recomendacion, Oficio> o = root.join("oficio");
            return cb.like(cb.lower(o.get("region")), like(region));
        };
    }

    public static Specification<Recomendacion> institucion(String institucion) {
        return (root, q, cb) -> {
            if (blank(institucion)) return null;
            Join<Recomendacion, Oficio> o = root.join("oficio");
            return cb.like(cb.lower(o.get("institucion")), like(institucion));
        };
    }

    public static Specification<Recomendacion> nroOficio(String nroOficio) {
        return (root, q, cb) -> {
            if (blank(nroOficio)) return null;
            Join<Recomendacion, Oficio> o = root.join("oficio");
            return cb.like(cb.lower(o.get("nroOficio")), like(nroOficio));
        };
    }

    public static Specification<Recomendacion> responsable(String responsable) {
        return (root, q, cb) -> blank(responsable) ? null
                : cb.like(cb.lower(root.get("responsableSeguimiento")), like(responsable));
    }

    private static boolean blank(String s) { return s == null || s.isBlank(); }
    private static String like(String s) { return "%" + s.toLowerCase().trim() + "%"; }
}
