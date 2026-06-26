package cl.smid.personas.infraestructura.seguridad;

import cl.smid.personas.dominio.excepcion.CodigoError;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Punto de entrada de autenticación: se invoca cuando una petición llega sin credenciales
 * válidas (token ausente, mal firmado, expirado o con claims inválidos). Devuelve el sobre de
 * error unificado con código {@code AUTZ-003} y HTTP 401 (Núcleo 2.4/2.5).
 *
 * <p>El sobre se serializa con un mapa ordenado (ver {@link EscritorSobreError}) en vez de
 * importar el record de la capa {@code api}, para no acoplar la capa de seguridad
 * (infraestructura) con la de presentación. La forma del JSON es idéntica a la del resto del
 * ecosistema.</p>
 */
@Component
public class PuntoEntradaNoAutorizado implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    public PuntoEntradaNoAutorizado(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        EscritorSobreError.escribir(
                objectMapper,
                response,
                request.getRequestURI(),
                CodigoError.NO_AUTENTICADO,
                "Autenticación requerida o token inválido.");
    }
}
