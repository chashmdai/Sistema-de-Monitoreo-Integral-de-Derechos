package cl.smid.antecedentes.infraestructura.persistencia;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * Conversor entre {@link Instant} (dominio) y {@link LocalDateTime} (entidad), anclado a
 * {@link ZoneOffset#UTC}. Las columnas son {@code DATETIME(6)} y la app fija
 * {@code hibernate.jdbc.time_zone=UTC}; con esto la marca temporal se persiste y recupera
 * siempre en UTC sin ambiguedad de zona horaria.
 */
public final class TiempoUtc {

    private TiempoUtc() {
    }

    /** Convierte un instante a LocalDateTime en UTC (para persistir). */
    public static LocalDateTime aLocal(Instant instante) {
        return instante == null ? null : LocalDateTime.ofInstant(instante, ZoneOffset.UTC);
    }

    /** Interpreta un LocalDateTime almacenado como UTC y lo devuelve como instante. */
    public static Instant aInstante(LocalDateTime local) {
        return local == null ? null : local.toInstant(ZoneOffset.UTC);
    }
}
