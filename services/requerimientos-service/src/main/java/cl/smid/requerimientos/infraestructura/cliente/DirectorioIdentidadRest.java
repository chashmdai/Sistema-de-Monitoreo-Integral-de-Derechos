package cl.smid.requerimientos.infraestructura.cliente;

import cl.smid.requerimientos.dominio.puerto.salida.DirectorioIdentidad;
import cl.smid.requerimientos.dominio.puerto.salida.ProfesionalIdentidad;
import cl.smid.requerimientos.infraestructura.seguridad.PortadorToken;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Objects;
import java.util.Optional;

/**
 * Adaptador de salida hacia Identidad (6.1) para resolver un profesional y su unidad.
 *
 * <p><b>Costura documentada:</b> Identidad aún no publica un endpoint estable de consulta de
 * usuario por alt_key. Este adaptador intenta {@code GET /usuarios/{altKey}} y, ante cualquier
 * indisponibilidad (conexión, 4xx/5xx), devuelve {@link Optional#empty()}. La política
 * ({@code permisiva}/{@code estricta}) la aplica el caso de uso: en modo permisivo tolera el vacío;
 * en estricto lo convierte en una regla de negocio no satisfecha.</p>
 */
@Component
public class DirectorioIdentidadRest implements DirectorioIdentidad {

    private static final Logger log = LoggerFactory.getLogger(DirectorioIdentidadRest.class);

    private final RestClient restClient;
    private final PortadorToken portadorToken;

    public DirectorioIdentidadRest(PropiedadesIntegracion propiedades, PortadorToken portadorToken) {
        this.restClient = RestClient.builder()
                .baseUrl(Objects.requireNonNull(propiedades.identidadUrl(), "identidadUrl es obligatoria"))
                .build();
        this.portadorToken = Objects.requireNonNull(portadorToken, "portadorToken es obligatorio");
    }

    @Override
    public Optional<ProfesionalIdentidad> resolverProfesional(String altKey) {
        try {
            return restClient.get()
                    .uri("/usuarios/{altKey}", altKey)
                    .headers(h -> ApoyoClienteRest.propagarToken(h, portadorToken))
                    .exchange((request, response) -> {
                        int codigo = response.getStatusCode().value();
                        if (codigo == 200) {
                            UsuarioDto dto = response.bodyTo(UsuarioDto.class);
                            if (dto == null) {
                                return Optional.<ProfesionalIdentidad>empty();
                            }
                            return Optional.of(new ProfesionalIdentidad(dto.altKey(), dto.idUnidad()));
                        }
                        return Optional.<ProfesionalIdentidad>empty();
                    });
        } catch (Exception e) {
            // Costura: Identidad puede no estar disponible. El caso de uso decide según la política.
            log.debug("No se pudo resolver al profesional {} en Identidad: {}", altKey, e.getMessage());
            return Optional.empty();
        }
    }

    /** Proyección del usuario de Identidad (alt_key y su unidad). */
    @JsonIgnoreProperties(ignoreUnknown = true)
    private record UsuarioDto(String altKey, String idUnidad) {
    }
}
