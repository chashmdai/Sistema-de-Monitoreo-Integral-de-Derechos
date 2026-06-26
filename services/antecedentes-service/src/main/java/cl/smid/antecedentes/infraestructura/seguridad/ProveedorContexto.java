package cl.smid.antecedentes.infraestructura.seguridad;

import cl.smid.antecedentes.dominio.excepcion.NoAutenticadoException;
import cl.smid.antecedentes.dominio.modelo.ContextoSesion;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * Provee el {@link ContextoSesion} de la peticion en curso. Se respalda en
 * <strong>request attributes</strong> (no {@code @RequestScope}) para evitar el problema de
 * timing: el filtro de seguridad escribe el contexto antes de que el {@code DispatcherServlet}
 * enlace el scope de request. El controlador lo lee durante su ejecucion.
 */
@Component
public class ProveedorContexto {

    /** Nombre del atributo de request donde el filtro deja el contexto. */
    public static final String ATRIBUTO_CONTEXTO = "cl.smid.antecedentes.CONTEXTO_SESION";

    /**
     * Devuelve el contexto de la peticion actual.
     *
     * @throws NoAutenticadoException ({@code AUTZ-003}) si no hay contexto (peticion sin autenticar)
     */
    public ContextoSesion contextoActual() {
        RequestAttributes atributos = RequestContextHolder.getRequestAttributes();
        if (atributos != null) {
            Object contexto = atributos.getAttribute(ATRIBUTO_CONTEXTO, RequestAttributes.SCOPE_REQUEST);
            if (contexto instanceof ContextoSesion sesion) {
                return sesion;
            }
        }
        throw new NoAutenticadoException("No hay contexto de sesion en la peticion.");
    }
}
