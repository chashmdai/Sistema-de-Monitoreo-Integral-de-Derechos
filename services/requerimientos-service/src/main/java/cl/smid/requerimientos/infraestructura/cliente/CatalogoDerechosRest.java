package cl.smid.requerimientos.infraestructura.cliente;

import cl.smid.requerimientos.dominio.puerto.salida.CatalogoDerechos;
import cl.smid.requerimientos.infraestructura.seguridad.PortadorToken;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Adaptador de salida hacia catalogo-service (6.7). Verifica la existencia de un derecho mediante
 * {@code GET /catalogo/derechos/{altKey}} y la de una causa listando
 * {@code GET /catalogo/derechos/{altKey}/causas} y comprobando pertenencia por alt_key. Propaga el
 * JWT del usuario.
 */
@Component
public class CatalogoDerechosRest implements CatalogoDerechos {

    private static final Logger log = LoggerFactory.getLogger(CatalogoDerechosRest.class);

    private final RestClient restClient;
    private final PortadorToken portadorToken;

    public CatalogoDerechosRest(PropiedadesIntegracion propiedades, PortadorToken portadorToken) {
        this.restClient = RestClient.builder()
                .baseUrl(Objects.requireNonNull(propiedades.catalogoUrl(), "catalogoUrl es obligatoria"))
                .build();
        this.portadorToken = Objects.requireNonNull(portadorToken, "portadorToken es obligatorio");
    }

    @Override
    public boolean existeDerecho(String idDerechoAlt) {
        Boolean existe = restClient.get()
                .uri("/catalogo/derechos/{altKey}", idDerechoAlt)
                .headers(h -> ApoyoClienteRest.propagarToken(h, portadorToken))
                .exchange((request, response) -> {
                    int codigo = response.getStatusCode().value();
                    if (codigo == 200) {
                        return true;
                    }
                    if (codigo == 404) {
                        return false;
                    }
                    log.warn("catalogo-service respondió {} al verificar el derecho {}", codigo, idDerechoAlt);
                    throw new IllegalStateException("catalogo-service respondió " + codigo);
                });
        return Boolean.TRUE.equals(existe);
    }

    @Override
    public boolean existeCausa(String idDerechoAlt, String idCausaAlt) {
        List<CausaDto> causas = Optional.ofNullable(restClient.get()
                .uri("/catalogo/derechos/{altKey}/causas", idDerechoAlt)
                .headers(h -> ApoyoClienteRest.propagarToken(h, portadorToken))
                .exchange((request, response) -> {
                    int codigo = response.getStatusCode().value();
                    if (codigo == 200) {
                        CausaDto[] cuerpo = response.bodyTo(CausaDto[].class);
                        return cuerpo == null ? List.<CausaDto>of()
                                : Arrays.stream(cuerpo).filter(Objects::nonNull).toList();
                    }
                    if (codigo == 404) {
                        return List.<CausaDto>of();
                    }
                    log.warn("catalogo-service respondió {} al listar causas del derecho {}",
                            codigo, idDerechoAlt);
                    throw new IllegalStateException("catalogo-service respondió " + codigo);
                })).orElseGet(List::of);
        return causas.stream().anyMatch(c -> idCausaAlt.equals(c.altKey()));
    }

    /** Proyección de una causa del Catálogo (solo el alt_key necesario para verificar). */
    @JsonIgnoreProperties(ignoreUnknown = true)
    private record CausaDto(String altKey) {
    }
}
