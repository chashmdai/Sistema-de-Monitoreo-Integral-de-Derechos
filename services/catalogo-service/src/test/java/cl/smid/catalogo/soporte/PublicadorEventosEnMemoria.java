package cl.smid.catalogo.soporte;

import cl.smid.catalogo.dominio.puerto.salida.EventoDominio;
import cl.smid.catalogo.dominio.puerto.salida.EventoPublicador;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Doble del {@link EventoPublicador} que captura los eventos publicados para poder verificarlos
 * en las pruebas (nombre del evento, recurso afectado, metadatos como descendientes afectados).
 */
public class PublicadorEventosEnMemoria implements EventoPublicador {

    private final List<EventoDominio> publicados = new ArrayList<>();

    @Override
    public void publicar(EventoDominio evento) {
        publicados.add(evento);
    }

    public List<EventoDominio> publicados() {
        return List.copyOf(publicados);
    }

    public int cantidad() {
        return publicados.size();
    }

    /** Último evento con el nombre indicado, si existe. */
    public Optional<EventoDominio> ultimoConNombre(String nombre) {
        for (int i = publicados.size() - 1; i >= 0; i--) {
            if (publicados.get(i).evento().equals(nombre)) {
                return Optional.of(publicados.get(i));
            }
        }
        return Optional.empty();
    }

    public void limpiar() {
        publicados.clear();
    }
}
