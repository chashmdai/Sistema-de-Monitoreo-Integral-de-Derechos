package cl.smid.casos.dominio.modelo;

import java.util.Objects;

/**
 * Resultado de consultar un caso con su enriquecimiento on-demand. El {@link #enriquecimiento()}
 * puede no estar disponible (esqueleto), en cuyo caso se entrega
 * {@link DatosEnriquecimiento#noDisponible()}.
 */
public record CasoEnriquecido(Caso caso, DatosEnriquecimiento enriquecimiento) {

    public CasoEnriquecido {
        Objects.requireNonNull(caso, "caso");
        Objects.requireNonNull(enriquecimiento, "enriquecimiento");
    }
}
