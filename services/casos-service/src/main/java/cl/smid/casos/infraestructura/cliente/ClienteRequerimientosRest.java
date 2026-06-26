package cl.smid.casos.infraestructura.cliente;

import cl.smid.casos.config.PropiedadesEnriquecimiento;
import cl.smid.casos.dominio.modelo.DatosEnriquecimiento;
import cl.smid.casos.dominio.puerto.salida.ClienteRequerimientos;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Objects;

/**
 * Adaptador REST de enriquecimiento on-demand contra requerimientos-service (6.3). Propaga el token
 * del usuario (cruce respetando territorial y G7) y devuelve SOLO metadatos no sensibles.
 *
 * <p>Es una COSTURA tolerante: cualquier fallo (servicio caído, 4xx/5xx, contrato no disponible) se
 * degrada a {@link DatosEnriquecimiento#noDisponible()} sin afectar la consulta del caso. El contrato
 * exacto del endpoint se confirmará cuando 6.3 lo exponga; aquí se asume un recurso de resumen.</p>
 *
 * <p>Activo cuando {@code smid.enriquecimiento.activo=true}.</p>
 */
@Component
@ConditionalOnProperty(name = "smid.enriquecimiento.activo", havingValue = "true")
public class ClienteRequerimientosRest implements ClienteRequerimientos {

    private static final Logger log = LoggerFactory.getLogger(ClienteRequerimientosRest.class);

    private final RestClient restClient;

    public ClienteRequerimientosRest(PropiedadesEnriquecimiento propiedades) {
        this.restClient = RestClient.builder()
                .baseUrl(Objects.requireNonNull(propiedades.requerimientosUrl(),
                        "smid.enriquecimiento.requerimientos-url es obligatorio"))
                .build();
    }

    @Override
    public DatosEnriquecimiento enriquecer(String requerimientoOrigenAlt, String tokenBearer) {
        try {
            RespuestaResumen resumen = restClient.get()
                    .uri("/requerimientos/{alt}/resumen", requerimientoOrigenAlt)
                    .headers(h -> {
                        if (tokenBearer != null && !tokenBearer.isBlank()) {
                            h.set(HttpHeaders.AUTHORIZATION, "Bearer " + tokenBearer);
                        }
                    })
                    .retrieve()
                    .body(RespuestaResumen.class);
            if (resumen == null) {
                return DatosEnriquecimiento.noDisponible();
            }
            return new DatosEnriquecimiento(true, resumen.estado(), resumen.canal(),
                    resumen.cantidadNnaAfectados());
        } catch (Exception ex) {
            log.debug("Enriquecimiento no disponible para requerimiento {}: {}",
                    requerimientoOrigenAlt, ex.getMessage());
            return DatosEnriquecimiento.noDisponible();
        }
    }

    /** Proyección tolerante del resumen del requerimiento (solo metadatos no sensibles). */
    @JsonIgnoreProperties(ignoreUnknown = true)
    private record RespuestaResumen(String estado, String canal, Integer cantidadNnaAfectados) {
    }
}
