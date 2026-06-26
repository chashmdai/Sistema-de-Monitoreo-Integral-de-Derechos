package cl.smid.requerimientos.infraestructura.seguridad;

import cl.smid.requerimientos.dominio.excepcion.CodigoError;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Punto de entrada de autenticación: cuando falta una credencial válida (token ausente, mal
 * firmado, expirado o inválido) responde con el sobre unificado y el código {@code AUTZ-003} (401).
 */
@Component
public class EntryPointNoAutenticado implements AuthenticationEntryPoint {

    private final EscritorRespuestaError escritor;

    public EntryPointNoAutenticado(EscritorRespuestaError escritor) {
        this.escritor = escritor;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        escritor.escribir(request, response, CodigoError.NO_AUTENTICADO);
    }
}
