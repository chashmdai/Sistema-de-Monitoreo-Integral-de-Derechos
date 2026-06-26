package cl.smid.requerimientos.infraestructura.seguridad;

import cl.smid.requerimientos.dominio.excepcion.CodigoError;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Manejador de acceso denegado: cuando un usuario autenticado carece del rol requerido (p. ej.
 * Coordinación en la admisibilidad) responde con el sobre unificado y el código {@code AUTZ-004}
 * (403). En este servicio este camino SÍ es alcanzable (a diferencia de personas-service).
 */
@Component
public class ManejadorAccesoDenegado implements AccessDeniedHandler {

    private final EscritorRespuestaError escritor;

    public ManejadorAccesoDenegado(EscritorRespuestaError escritor) {
        this.escritor = escritor;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        escritor.escribir(request, response, CodigoError.ACCESO_DENEGADO);
    }
}
