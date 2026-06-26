package cl.smid.personas.soporte;

import cl.smid.personas.dominio.puerto.salida.RelojDominio;

import java.time.Instant;

/**
 * Reloj de prueba que devuelve siempre un instante fijo, para hacer deterministas las pruebas
 * que dependen de marcas de tiempo (creado_en/actualizado_en).
 */
public class RelojFijo implements RelojDominio {

    private Instant instante;

    public RelojFijo(Instant instante) {
        this.instante = instante;
    }

    /** Permite avanzar el reloj manualmente entre pasos de una prueba. */
    public void fijar(Instant nuevo) {
        this.instante = nuevo;
    }

    @Override
    public Instant ahora() {
        return instante;
    }
}
