package cl.smid.instituciones.dominio.soporte;

import cl.smid.instituciones.dominio.puerto.salida.RelojDominio;

import java.time.Instant;

/** Reloj de pruebas con un instante fijo y controlable. */
public class RelojFijo implements RelojDominio {

    private Instant instante;

    public RelojFijo(Instant instante) {
        this.instante = instante;
    }

    public void establecer(Instant instante) {
        this.instante = instante;
    }

    @Override
    public Instant ahora() {
        return instante;
    }
}
