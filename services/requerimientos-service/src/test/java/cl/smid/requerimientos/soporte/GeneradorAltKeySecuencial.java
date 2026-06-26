package cl.smid.requerimientos.soporte;

import cl.smid.requerimientos.dominio.puerto.salida.GeneradorAltKey;

import java.util.concurrent.atomic.AtomicLong;

/** Generador de alt_key determinista para pruebas ({@code req-1}, {@code req-2}, ...). */
public class GeneradorAltKeySecuencial implements GeneradorAltKey {

    private final AtomicLong contador = new AtomicLong(0);

    @Override
    public String nuevo() {
        return "req-" + contador.incrementAndGet();
    }
}
