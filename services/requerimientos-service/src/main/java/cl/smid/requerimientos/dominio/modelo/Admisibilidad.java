package cl.smid.requerimientos.dominio.modelo;

import java.time.Instant;
import java.util.Objects;

/**
 * Registro de una decisión de admisibilidad (USR.02, Núcleo 6.5). Inmutable.
 *
 * <p>Cada decisión ejecuta exactamente una de las tres acciones disjuntas. Para
 * {@link AccionAdmisibilidad#ASIGNACION} el campo {@code idProfesionalAsignadoAlt} es
 * obligatorio; para las demás es nulo.</p>
 *
 * @param idInterno                 handle de persistencia (nulo si aún no persistida)
 * @param accion                    acción ejecutada
 * @param idCoordinadorAlt          alt_key del coordinador que decidió (claim {@code sub})
 * @param escaladoADefensora        solo aplica a {@code INADMISIBLE}
 * @param idProfesionalAsignadoAlt  alt_key del profesional (solo {@code ASIGNACION})
 * @param observacion               texto libre opcional
 * @param decididoEn                instante UTC de la decisión
 */
public record Admisibilidad(
        Long idInterno,
        AccionAdmisibilidad accion,
        String idCoordinadorAlt,
        boolean escaladoADefensora,
        String idProfesionalAsignadoAlt,
        String observacion,
        Instant decididoEn) {

    public Admisibilidad {
        Objects.requireNonNull(accion, "La acción de admisibilidad es obligatoria");
        Objects.requireNonNull(idCoordinadorAlt, "El alt_key del coordinador es obligatorio");
        Objects.requireNonNull(decididoEn, "El instante de la decisión es obligatorio");
    }
}
