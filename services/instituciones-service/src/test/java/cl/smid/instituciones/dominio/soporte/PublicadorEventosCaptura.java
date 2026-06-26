package cl.smid.instituciones.dominio.soporte;

import cl.smid.instituciones.dominio.modelo.EventoDominio;
import cl.smid.instituciones.dominio.puerto.salida.PublicadorEventos;

import java.util.ArrayList;
import java.util.List;

/** Publicador de pruebas que captura los eventos emitidos para poder verificarlos. */
public class PublicadorEventosCaptura implements PublicadorEventos {

    private final List<EventoDominio> eventos = new ArrayList<>();

    @Override
    public void publicar(EventoDominio evento) {
        eventos.add(evento);
    }

    public List<EventoDominio> eventos() {
        return eventos;
    }

    public long contar(String tipo) {
        return eventos.stream().filter(e -> e.tipo().equals(tipo)).count();
    }

    public EventoDominio ultimo() {
        return eventos.isEmpty() ? null : eventos.get(eventos.size() - 1);
    }
}
