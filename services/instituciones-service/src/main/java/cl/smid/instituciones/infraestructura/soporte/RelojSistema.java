package cl.smid.instituciones.infraestructura.soporte;

import cl.smid.instituciones.dominio.puerto.salida.RelojDominio;
import org.springframework.stereotype.Component;

import java.time.Instant;

/**
 * Implementación del puerto {@link RelojDominio} basada en el reloj del sistema (UTC).
 */
@Component
public class RelojSistema implements RelojDominio {

    @Override
    public Instant ahora() {
        return Instant.now();
    }
}
