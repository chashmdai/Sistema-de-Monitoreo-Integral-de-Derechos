package cl.smid.requerimientos.infraestructura.cliente;

import cl.smid.requerimientos.infraestructura.seguridad.PortadorToken;
import org.springframework.http.HttpHeaders;
import org.springframework.util.MultiValueMap;

/**
 * Utilidades comunes a los adaptadores REST de salida. Centraliza la propagación de la identidad
 * del usuario: copia el token JWT en curso (del {@link PortadorToken}) a la cabecera
 * {@code Authorization} de la llamada saliente, de modo que el servicio destino aplique su propio
 * control territorial y de roles.
 */
final class ApoyoClienteRest {

    private ApoyoClienteRest() {
    }

    /**
     * Añade la cabecera {@code Authorization: Bearer <token>} si hay un token en la petición actual.
     *
     * @param cabeceras cabeceras de la llamada saliente
     * @param portador  portador del token en curso
     */
    static void propagarToken(MultiValueMap<String, String> cabeceras, PortadorToken portador) {
        String token = portador.token();
        if (token != null && !token.isBlank()) {
            cabeceras.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        }
    }
}
