package cl.smid.requerimientos.infraestructura.seguridad;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

/**
 * Portador del token JWT en curso, con ámbito de petición. El filtro deposita aquí el token crudo
 * tras validarlo, y los adaptadores REST de salida lo leen para propagar la identidad del usuario
 * (cabecera {@code Authorization}) en las llamadas servicio a servicio. Así los puertos de salida
 * permanecen libres de detalles de transporte.
 */
@Component
@RequestScope
public class PortadorToken {

    private String token;

    /** @param token token JWT crudo (sin el prefijo {@code Bearer }). */
    public void establecer(String token) {
        this.token = token;
    }

    /** @return el token JWT crudo de la petición en curso, o {@code null} si no hay. */
    public String token() {
        return token;
    }
}
