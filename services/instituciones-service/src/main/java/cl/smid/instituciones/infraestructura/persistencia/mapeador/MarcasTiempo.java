package cl.smid.instituciones.infraestructura.persistencia.mapeador;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * Conversión entre {@link Instant} (representación del dominio) y {@link LocalDateTime}
 * (representación de las entidades JPA), siempre anclada a UTC para ser coherente con
 * {@code hibernate.jdbc.time_zone=UTC} y las columnas {@code DATETIME(6)}.
 */
final class MarcasTiempo {

    private MarcasTiempo() {
    }

    /** Convierte un instante UTC a {@link LocalDateTime} (o {@code null}). */
    static LocalDateTime aLocal(Instant instante) {
        return instante == null ? null : LocalDateTime.ofInstant(instante, ZoneOffset.UTC);
    }

    /** Convierte un {@link LocalDateTime} UTC a {@link Instant} (o {@code null}). */
    static Instant aInstante(LocalDateTime fecha) {
        return fecha == null ? null : fecha.toInstant(ZoneOffset.UTC);
    }
}
