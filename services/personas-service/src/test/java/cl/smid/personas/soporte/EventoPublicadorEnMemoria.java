package cl.smid.personas.soporte;

import cl.smid.personas.dominio.modelo.EventoDominio;
import cl.smid.personas.dominio.puerto.salida.EventoPublicador;

import java.util.ArrayList;
import java.util.List;

/**
 * Publicador de eventos de prueba que acumula los eventos emitidos en memoria, para verificar
 * en las aserciones que el dominio publicó {@code persona.creada}/{@code persona.actualizada}
 * con los metadatos esperados (y sólo metadatos no sensibles).
 */
public class EventoPublicadorEnMemoria implements EventoPublicador {

    private final List<EventoDominio> eventos = new ArrayList<>();

    @Override
    public void publicar(EventoDominio evento) {
        eventos.add(evento);
    }

    /** Eventos publicados, en orden de emisión. */
    public List<EventoDominio> eventos() {
        return List.copyOf(eventos);
    }

    /** Último evento publicado (útil en aserciones puntuales). */
    public EventoDominio ultimo() {
        if (eventos.isEmpty()) {
            throw new IllegalStateException("No se ha publicado ningún evento.");
        }
        return eventos.get(eventos.size() - 1);
    }
}
