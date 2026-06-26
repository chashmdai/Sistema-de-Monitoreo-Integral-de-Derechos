package cl.smid.antecedentes.dominio.servicio;

import cl.smid.antecedentes.dominio.modelo.Folio;
import cl.smid.antecedentes.dominio.puerto.salida.CorrelativoFolioPort;
import cl.smid.antecedentes.dominio.puerto.salida.RelojDominio;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

/**
 * Genera folios de las dos series independientes con reinicio anual:
 * <ul>
 *   <li>Fichas: {@code FA-{N}/{anio}}.</li>
 *   <li>Hallazgos: {@code HZ-{N}/{anio}}.</li>
 * </ul>
 * El correlativo lo reserva atomicamente {@link CorrelativoFolioPort} (modismo MySQL UPSERT con
 * {@code LAST_INSERT_ID}); este servicio compone el valor final. El anio se calcula en UTC.
 */
public class GeneradorFolio {

    private static final String PREFIJO_FICHA = "FA";
    private static final String PREFIJO_HALLAZGO = "HZ";

    private final CorrelativoFolioPort correlativo;
    private final RelojDominio reloj;

    public GeneradorFolio(CorrelativoFolioPort correlativo, RelojDominio reloj) {
        this.correlativo = correlativo;
        this.reloj = reloj;
    }

    /** Reserva y compone el siguiente folio de ficha. */
    public Folio siguienteFicha() {
        return componer(PREFIJO_FICHA);
    }

    /** Reserva y compone el siguiente folio de hallazgo. */
    public Folio siguienteHallazgo() {
        return componer(PREFIJO_HALLAZGO);
    }

    private Folio componer(String prefijo) {
        int anio = anioActualUtc();
        String serie = prefijo + "-" + anio;
        long n = correlativo.siguiente(serie, anio);
        return new Folio(prefijo + "-" + n + "/" + anio);
    }

    private int anioActualUtc() {
        return ZonedDateTime.ofInstant(reloj.ahora(), ZoneOffset.UTC).getYear();
    }
}
