package cl.smid.catalogo.config;

import cl.smid.catalogo.dominio.excepcion.CodigoError;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Punto de entrada de autenticación de Spring Security: se invoca cuando una solicitud sin
 * autenticación válida intenta acceder a un recurso protegido. Responde con el sobre 401
 * ({@code AUTZ-003}), homogéneo con el resto de errores del ecosistema.
 */
@Component
public class PuntoEntradaJwt implements AuthenticationEntryPoint {

    private final EscritorErrorSeguridad escritorError;

    public PuntoEntradaJwt(EscritorErrorSeguridad escritorError) {
        this.escritorError = escritorError;
    }

    @Override
    public void commence(HttpServletRequest solicitud, HttpServletResponse respuesta,
                         AuthenticationException excepcion) throws IOException {
        escritorError.escribir(solicitud, respuesta,
                CodigoError.NO_AUTENTICADO, CodigoError.NO_AUTENTICADO.getMensajePorDefecto());
    }
}
