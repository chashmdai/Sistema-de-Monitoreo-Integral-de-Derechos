package cl.smid.instituciones.dominio.soporte;

import cl.smid.instituciones.dominio.puerto.salida.GeneradorIdentificadores;

import java.util.concurrent.atomic.AtomicInteger;

/** Generador determinista de identificadores para pruebas: {@code alt-1}, {@code alt-2}, ... */
public class GeneradorSecuencial implements GeneradorIdentificadores {

    private final AtomicInteger contador = new AtomicInteger(0);

    @Override
    public String nuevoAltKey() {
        return "alt-" + contador.incrementAndGet();
    }
}
