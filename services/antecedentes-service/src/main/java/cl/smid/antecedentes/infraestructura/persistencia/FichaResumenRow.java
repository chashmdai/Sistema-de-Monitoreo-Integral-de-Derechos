package cl.smid.antecedentes.infraestructura.persistencia;

import cl.smid.antecedentes.dominio.modelo.Calificacion;
import cl.smid.antecedentes.dominio.modelo.EstadoFicha;
import cl.smid.antecedentes.dominio.modelo.PercepcionHallazgo;

import java.time.LocalDateTime;

/**
 * Fila de proyeccion para la bandeja de fichas, poblada por una expresion {@code new} en JPQL.
 * Evita cargar colecciones hijas y descifrar el relato. El adaptador la convierte a
 * {@code ResumenFicha} del dominio (LocalDateTime UTC -&gt; Instant).
 */
public record FichaResumenRow(
        String altKey,
        String folio,
        EstadoFicha estado,
        Calificacion calificacion,
        PercepcionHallazgo percepcionHallazgo,
        String unidadAlt,
        String sedeAlt,
        String casoAlt,
        LocalDateTime creadoEn,
        LocalDateTime actualizadoEn) {
}
