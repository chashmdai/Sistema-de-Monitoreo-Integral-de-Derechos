package cl.smid.requerimientos.infraestructura.cliente;

import cl.smid.requerimientos.dominio.puerto.salida.DirectorioPersonas;
import cl.smid.requerimientos.dominio.puerto.salida.PersonaResuelta;
import cl.smid.requerimientos.infraestructura.seguridad.PortadorToken;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Objects;
import java.util.Optional;

/**
 * Adaptador de salida hacia personas-service (6.2). Resuelve una persona por su alt_key mediante
 * {@code GET /personas/{altKey}}, propagando el JWT del usuario para que Personas aplique su filtro
 * territorial. Un {@code 404} (inexistente o fuera de alcance) se traduce en {@link Optional#empty()}.
 */
@Component
public class DirectorioPersonasRest implements DirectorioPersonas {

    private static final Logger log = LoggerFactory.getLogger(DirectorioPersonasRest.class);

    private final RestClient restClient;
    private final PortadorToken portadorToken;

    public DirectorioPersonasRest(PropiedadesIntegracion propiedades, PortadorToken portadorToken) {
        this.restClient = RestClient.builder()
                .baseUrl(Objects.requireNonNull(propiedades.personasUrl(), "personasUrl es obligatoria"))
                .build();
        this.portadorToken = Objects.requireNonNull(portadorToken, "portadorToken es obligatorio");
    }

    @Override
    public Optional<PersonaResuelta> resolver(String altKey) {
        return restClient.get()
                .uri("/personas/{altKey}", altKey)
                .headers(h -> ApoyoClienteRest.propagarToken(h, portadorToken))
                .exchange((request, response) -> {
                    int codigo = response.getStatusCode().value();
                    if (codigo == 200) {
                        PersonaDto dto = response.bodyTo(PersonaDto.class);
                        if (dto == null) {
                            return Optional.<PersonaResuelta>empty();
                        }
                        return Optional.of(new PersonaResuelta(dto.altKey(), dto.nombreLegible(), dto.rut()));
                    }
                    if (codigo == 404) {
                        return Optional.<PersonaResuelta>empty();
                    }
                    log.warn("personas-service respondió {} al resolver la persona {}", codigo, altKey);
                    throw new IllegalStateException("personas-service respondió " + codigo);
                });
    }

    /** Proyección de la respuesta de Personas (solo los campos que necesita el snapshot). */
    @JsonIgnoreProperties(ignoreUnknown = true)
    private record PersonaDto(String altKey, String nombreLegible, String rut) {
    }
}
