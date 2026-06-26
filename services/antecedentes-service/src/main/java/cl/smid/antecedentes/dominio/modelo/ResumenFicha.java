package cl.smid.antecedentes.dominio.modelo;

import java.time.Instant;

/**
 * Proyeccion ligera de una {@link FichaAntecedente} para la bandeja/listado. <strong>No
 * incluye el relato</strong> (que ademas viaja cifrado en reposo): el relato solo se devuelve
 * en claro en el detalle. Evita descifrar y cargar colecciones hijas en los listados.
 *
 * @param altKey             identificador publico
 * @param folio              folio FA
 * @param estado             estado de la ficha
 * @param calificacion       calificacion
 * @param percepcionHallazgo evaluacion de hallazgo
 * @param unidadAlt          alt_key de la unidad
 * @param sedeAlt            alt_key de la sede
 * @param casoAlt            alt_key del caso (puede ser nulo)
 * @param creadoEn           creacion (UTC)
 * @param actualizadoEn      ultima actualizacion (UTC)
 */
public record ResumenFicha(
        String altKey,
        String folio,
        EstadoFicha estado,
        Calificacion calificacion,
        PercepcionHallazgo percepcionHallazgo,
        String unidadAlt,
        String sedeAlt,
        String casoAlt,
        Instant creadoEn,
        Instant actualizadoEn) {
}
