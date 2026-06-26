package cl.smid.catalogo.soporte;

import cl.smid.catalogo.dominio.puerto.salida.GeneradorAltKey;
import cl.smid.catalogo.dominio.puerto.salida.RelojDominio;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Dobles deterministas del tiempo y de la generación de identificadores, para que las pruebas
 * del dominio sean reproducibles.
 */
public final class DoblesDeterministas {

    private DoblesDeterministas() {
    }

    /** Reloj fijo en una fecha/instante conocidos (UTC). */
    public static final class RelojFijo implements RelojDominio {
        private final LocalDate fecha;
        private final Instant instante;

        public RelojFijo(LocalDate fecha) {
            this.fecha = fecha;
            this.instante = fecha.atStartOfDay().toInstant(ZoneOffset.UTC);
        }

        @Override
        public LocalDate hoy() {
            return fecha;
        }

        @Override
        public Instant ahora() {
            return instante;
        }
    }

    /** Generador de alt_key secuencial y predecible ({@code ak-1}, {@code ak-2}, ...). */
    public static final class GeneradorAltKeySecuencial implements GeneradorAltKey {
        private final AtomicLong secuencia = new AtomicLong(0);

        @Override
        public String nuevo() {
            return "ak-" + secuencia.incrementAndGet();
        }
    }
}
