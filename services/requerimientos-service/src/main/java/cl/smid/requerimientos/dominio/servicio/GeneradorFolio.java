package cl.smid.requerimientos.dominio.servicio;

import cl.smid.requerimientos.dominio.modelo.Folio;
import cl.smid.requerimientos.dominio.modelo.SerieFolio;
import cl.smid.requerimientos.dominio.puerto.salida.CorrelativoFolioRepositorio;
import cl.smid.requerimientos.dominio.puerto.salida.DirectorioSedes;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Objects;

/**
 * Servicio de dominio que compone el folio oficial de un requerimiento (Núcleo 6.5).
 *
 * <p>Combina tres piezas:</p>
 * <ol>
 *   <li>El <b>código institucional</b> de la sede, resuelto por {@link DirectorioSedes}.</li>
 *   <li>El <b>correlativo atómico</b> por (sede, año, serie), reservado por
 *       {@link CorrelativoFolioRepositorio} (seguro ante concurrencia).</li>
 *   <li>La <b>serie</b> (OFICIAL/BETA), determinada por la política de marcha blanca: antes de
 *       {@code inicioOficial} se usa BETA; desde esa fecha, OFICIAL. Un override explícito
 *       ({@code esBeta}) puede forzar la serie.</li>
 * </ol>
 *
 * <p>La serie BETA corre por un contador independiente: nunca consume la numeración OFICIAL,
 * de modo que la serie oficial arranca inmaculada (el primer folio oficial de un año es
 * {@code {CODIGO}-1/{AÑO}}).</p>
 *
 * <p>Es dominio puro: recibe el instante como parámetro (no lee el reloj del sistema) y delega
 * la atomicidad y la resolución del código en puertos.</p>
 */
public final class GeneradorFolio {

    private final DirectorioSedes directorioSedes;
    private final CorrelativoFolioRepositorio correlativos;
    /** Fecha (inclusive) a partir de la cual los folios nuevos usan la serie OFICIAL. */
    private final LocalDate inicioOficial;

    /**
     * @param directorioSedes puerto que resuelve el código institucional de la sede
     * @param correlativos    puerto del contador atómico de folio
     * @param inicioOficial   fecha de inicio de la serie oficial (p. ej. {@code 2027-01-01})
     */
    public GeneradorFolio(DirectorioSedes directorioSedes,
                          CorrelativoFolioRepositorio correlativos,
                          LocalDate inicioOficial) {
        this.directorioSedes = Objects.requireNonNull(directorioSedes, "directorioSedes es obligatorio");
        this.correlativos = Objects.requireNonNull(correlativos, "correlativos es obligatorio");
        this.inicioOficial = Objects.requireNonNull(inicioOficial, "inicioOficial es obligatorio");
    }

    /**
     * Determina la serie de folio a usar.
     *
     * @param ahora       instante actual (UTC)
     * @param overrideBeta override explícito: {@code TRUE} fuerza BETA, {@code FALSE} fuerza
     *                     OFICIAL, {@code null} aplica la política por fecha
     * @return la serie resultante
     */
    public SerieFolio determinarSerie(Instant ahora, Boolean overrideBeta) {
        Objects.requireNonNull(ahora, "El instante es obligatorio");
        if (overrideBeta != null) {
            return overrideBeta ? SerieFolio.BETA : SerieFolio.OFICIAL;
        }
        LocalDate hoy = ahora.atZone(ZoneOffset.UTC).toLocalDate();
        return hoy.isBefore(inicioOficial) ? SerieFolio.BETA : SerieFolio.OFICIAL;
    }

    /**
     * Genera y reserva un folio nuevo, atómico y único para su (sede, año, serie).
     *
     * @param idSedeAlt    alt_key de la sede del requerimiento
     * @param ahora        instante actual (UTC); determina el año del folio y la serie
     * @param overrideBeta override de serie (nulable; ver {@link #determinarSerie})
     * @return el folio compuesto y reservado
     */
    public Folio generar(String idSedeAlt, Instant ahora, Boolean overrideBeta) {
        Objects.requireNonNull(idSedeAlt, "El alt_key de la sede es obligatorio");
        Objects.requireNonNull(ahora, "El instante es obligatorio");

        SerieFolio serie = determinarSerie(ahora, overrideBeta);
        int anio = ahora.atZone(ZoneOffset.UTC).getYear();

        // Reserva atómica del correlativo (bloqueo pesimista / UPSERT en el adaptador).
        long correlativo = correlativos.siguienteCorrelativo(idSedeAlt, anio, serie);

        String codigoSede = directorioSedes.codigoDeSede(idSedeAlt);
        return Folio.componer(codigoSede, serie, correlativo, anio);
    }

    /** @return la serie efectiva resultante de un override (utilidad de lectura). */
    public boolean esBetaEfectiva(Instant ahora, Boolean overrideBeta) {
        return determinarSerie(ahora, overrideBeta) == SerieFolio.BETA;
    }
}
