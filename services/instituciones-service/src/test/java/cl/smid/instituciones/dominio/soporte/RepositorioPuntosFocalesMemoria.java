package cl.smid.instituciones.dominio.soporte;

import cl.smid.instituciones.dominio.modelo.PuntoFocal;
import cl.smid.instituciones.dominio.puerto.salida.RepositorioPuntosFocales;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Implementación en memoria de {@link RepositorioPuntosFocales} para pruebas. Reproduce
 * el desmarcado masivo de principales para poder verificar la invariante "a lo sumo un
 * principal activo por institución".
 */
public class RepositorioPuntosFocalesMemoria implements RepositorioPuntosFocales {

    private final Map<String, PuntoFocal> porAlt = new LinkedHashMap<>();

    @Override
    public PuntoFocal guardar(PuntoFocal puntoFocal) {
        porAlt.put(puntoFocal.altKey(), puntoFocal);
        return puntoFocal;
    }

    @Override
    public Optional<PuntoFocal> buscarPorAlt(String altKey) {
        return Optional.ofNullable(porAlt.get(altKey));
    }

    @Override
    public List<PuntoFocal> listarPorInstitucion(String institucionAlt) {
        List<PuntoFocal> lista = new ArrayList<>();
        for (PuntoFocal p : porAlt.values()) {
            if (p.institucionAlt().equals(institucionAlt)) {
                lista.add(p);
            }
        }
        lista.sort(Comparator.comparing(PuntoFocal::principal).reversed());
        return lista;
    }

    @Override
    public void desmarcarOtrosPrincipales(String institucionAlt, String altKeyActual) {
        for (Map.Entry<String, PuntoFocal> entrada : porAlt.entrySet()) {
            PuntoFocal p = entrada.getValue();
            if (p.institucionAlt().equals(institucionAlt)
                    && !p.altKey().equals(altKeyActual)
                    && p.activo() && p.principal()) {
                p.quitarPrincipal(p.actualizadoEn());
            }
        }
    }
}
