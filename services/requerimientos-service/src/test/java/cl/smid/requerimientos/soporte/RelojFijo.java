package cl.smid.requerimientos.soporte;

import cl.smid.requerimientos.dominio.puerto.salida.RelojDominio;

import java.time.Instant;

/** Reloj de prueba con instante fijo (o ajustable), para hacer deterministas las pruebas. */
public class RelojFijo implements RelojDominio {

    private Instant momento;

    public RelojFijo(Instant momento) {
        this.momento = momento;
    }

    public void fijar(Instant momento) {
        this.momento = momento;
    }

    @Override
    public Instant ahora() {
        return momento;
    }
}
