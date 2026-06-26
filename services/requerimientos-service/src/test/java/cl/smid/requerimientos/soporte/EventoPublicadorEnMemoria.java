package cl.smid.requerimientos.soporte;

import cl.smid.requerimientos.dominio.modelo.EventoDominio;
import cl.smid.requerimientos.dominio.puerto.salida.EventoPublicador;

import java.util.ArrayList;
import java.util.List;

/** Publicador de eventos que los acumula en memoria para verificarlos en las pruebas. */
public class EventoPublicadorEnMemoria implements EventoPublicador {

    private final List<EventoDominio> eventos = new ArrayList<>();

    @Override
    public void publicar(EventoDominio evento) {
        eventos.add(evento);
    }

    public List<EventoDominio> eventos() {
        return List.copyOf(eventos);
    }

    public boolean contieneTipo(String tipo) {
        return eventos.stream().anyMatch(e -> e.tipo().equals(tipo));
    }

    public long conteoTipo(String tipo) {
        return eventos.stream().filter(e -> e.tipo().equals(tipo)).count();
    }
}
