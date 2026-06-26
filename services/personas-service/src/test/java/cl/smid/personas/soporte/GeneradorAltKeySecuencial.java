package cl.smid.personas.soporte;

import cl.smid.personas.dominio.puerto.salida.GeneradorAltKey;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Generador de alt_key determinista para pruebas: produce identificadores predecibles del
 * estilo {@code alt-0000000001}. Mantiene el formato opaco (no expone secuencias internas de la
 * persistencia) pero es reproducible.
 */
public class GeneradorAltKeySecuencial implements GeneradorAltKey {

    private final AtomicInteger contador = new AtomicInteger(0);

    @Override
    public String nuevo() {
        return String.format("alt-%010d", contador.incrementAndGet());
    }
}
