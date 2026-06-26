package cl.smid.antecedentes.infraestructura.persistencia;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Embebidos de las colecciones hijas de {@link FichaAntecedenteEntity}, mapeados como
 * {@code @ElementCollection}. Las tablas {@code ficha_documento}/{@code ficha_historial}
 * llevan ademas un {@code id} BIGINT autoincremental (definido en Flyway) que aqui no se
 * mapea: Hibernate solo gestiona las columnas declaradas y valida nada mas que esas.
 */
public final class EmbebidosFicha {

    private EmbebidosFicha() {
    }

    /** Metadato de documento asociado a la ficha (sin binario; referencia externa opcional). */
    @Embeddable
    @Getter
    @Setter
    @NoArgsConstructor
    public static class DocumentoEmbebido {

        @Column(name = "alt_key", nullable = false, length = 36)
        private String altKey;

        @Column(name = "nombre", nullable = false, length = 255)
        private String nombre;

        @Column(name = "referencia_externa", length = 255)
        private String referenciaExterna;
    }

    /** Asiento del historial del ciclo de vida de la ficha. */
    @Embeddable
    @Getter
    @Setter
    @NoArgsConstructor
    public static class HistorialEmbebido {

        @Column(name = "tipo_evento", nullable = false, length = 40)
        private String tipoEvento;

        @Column(name = "actor_alt", length = 36)
        private String actorAlt;

        @Column(name = "ocurrido_en", nullable = false)
        private LocalDateTime ocurridoEn;

        @Column(name = "observacion", length = 1000)
        private String observacion;
    }
}
