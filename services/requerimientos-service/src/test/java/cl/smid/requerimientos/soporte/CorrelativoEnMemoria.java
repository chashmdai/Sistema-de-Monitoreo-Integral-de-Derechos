package cl.smid.requerimientos.soporte;

import cl.smid.requerimientos.dominio.modelo.SerieFolio;
import cl.smid.requerimientos.dominio.puerto.salida.CorrelativoFolioRepositorio;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Contador correlativo en memoria, atómico por (sede, año, serie). Reproduce la garantía del
 * adaptador real (reserva única bajo concurrencia) usando {@link ConcurrentHashMap#compute}, lo que
 * permite probar la unicidad de folios con múltiples hilos sin base de datos.
 */
public class CorrelativoEnMemoria implements CorrelativoFolioRepositorio {

    private final ConcurrentHashMap<String, Long> contadores = new ConcurrentHashMap<>();

    @Override
    public long siguienteCorrelativo(String idSedeAlt, int anio, SerieFolio serie) {
        String clave = idSedeAlt + "|" + anio + "|" + serie.name();
        return contadores.compute(clave, (k, actual) -> (actual == null) ? 1L : actual + 1L);
    }
}
