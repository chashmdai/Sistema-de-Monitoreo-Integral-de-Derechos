package cl.smid.catalogo.infraestructura.comun;

import cl.smid.catalogo.dominio.puerto.salida.RelojDominio;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;

/**
 * Implementación de producción del {@link RelojDominio}, anclada a <b>UTC</b> (Núcleo 2.10).
 * Centralizar el acceso al tiempo aquí permite que el dominio sea determinista y verificable
 * (las pruebas inyectan un reloj fijo en su lugar).
 */
@Component
public class RelojSistema implements RelojDominio {

    private final Clock reloj = Clock.systemUTC();

    @Override
    public LocalDate hoy() {
        return LocalDate.now(reloj);
    }

    @Override
    public Instant ahora() {
        return Instant.now(reloj);
    }
}
