package cl.smid.personas.infraestructura.soporte;

import cl.smid.personas.dominio.puerto.salida.RelojDominio;
import org.springframework.stereotype.Component;

import java.time.Instant;

/**
 * Adaptador del puerto {@link RelojDominio} basado en el reloj del sistema (UTC). Es el único
 * lugar donde el tiempo "real" entra al sistema; el dominio depende de la abstracción y queda
 * determinista en pruebas.
 */
@Component
public class RelojSistema implements RelojDominio {

    @Override
    public Instant ahora() {
        return Instant.now();
    }
}
