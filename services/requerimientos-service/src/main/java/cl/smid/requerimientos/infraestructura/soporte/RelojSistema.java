package cl.smid.requerimientos.infraestructura.soporte;

import cl.smid.requerimientos.dominio.puerto.salida.RelojDominio;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.Instant;

/**
 * Adaptador del puerto {@link RelojDominio} basado en el reloj del sistema en UTC. Aislar el reloj
 * permite inyectar uno fijo en las pruebas de dominio.
 */
@Component
public class RelojSistema implements RelojDominio {

    private final Clock clock = Clock.systemUTC();

    @Override
    public Instant ahora() {
        return Instant.now(clock);
    }
}
