package cl.smid.antecedentes.dominio;

import cl.smid.antecedentes.dominio.modelo.CriterioTerritorial;
import cl.smid.antecedentes.dominio.modelo.EventoDominio;
import cl.smid.antecedentes.dominio.modelo.FichaAntecedente;
import cl.smid.antecedentes.dominio.modelo.FiltroFichas;
import cl.smid.antecedentes.dominio.modelo.FiltroHallazgos;
import cl.smid.antecedentes.dominio.modelo.Hallazgo;
import cl.smid.antecedentes.dominio.modelo.Pagina;
import cl.smid.antecedentes.dominio.modelo.Referencia;
import cl.smid.antecedentes.dominio.modelo.ResumenFicha;
import cl.smid.antecedentes.dominio.modelo.TipoReferencia;
import cl.smid.antecedentes.dominio.puerto.salida.CorrelativoFolioPort;
import cl.smid.antecedentes.dominio.puerto.salida.GeneradorIdentificadores;
import cl.smid.antecedentes.dominio.puerto.salida.PublicadorEventos;
import cl.smid.antecedentes.dominio.puerto.salida.RelojDominio;
import cl.smid.antecedentes.dominio.puerto.salida.RepositorioFichas;
import cl.smid.antecedentes.dominio.puerto.salida.RepositorioHallazgos;
import cl.smid.antecedentes.dominio.puerto.salida.RepositorioReferencias;

import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Dobles in-memory de los puertos de salida para pruebas unitarias de dominio (sin Spring ni BD).
 */
public final class DoblesEnMemoria {

    private DoblesEnMemoria() {
    }

    /** Reloj fijo (controla el tiempo en pruebas). */
    public static final class RelojFijo implements RelojDominio {
        private Instant instante;

        public RelojFijo(Instant instante) {
            this.instante = instante;
        }

        public void avanzar(Instant nuevo) {
            this.instante = nuevo;
        }

        @Override
        public Instant ahora() {
            return instante;
        }
    }

    /** Generador de alt_key secuencial y predecible. */
    public static final class GeneradorIdSecuencial implements GeneradorIdentificadores {
        private final AtomicInteger contador = new AtomicInteger();
        private final String prefijo;

        public GeneradorIdSecuencial(String prefijo) {
            this.prefijo = prefijo;
        }

        @Override
        public String nuevoAltKey() {
            return prefijo + "-" + contador.incrementAndGet();
        }
    }

    /** Correlativo en memoria por serie. */
    public static final class CorrelativoFake implements CorrelativoFolioPort {
        private final Map<String, AtomicLong> contadores = new LinkedHashMap<>();

        @Override
        public long siguiente(String serie, int anio) {
            return contadores.computeIfAbsent(serie, s -> new AtomicLong()).incrementAndGet();
        }
    }

    /** Captura los eventos publicados. */
    public static final class PublicadorFake implements PublicadorEventos {
        public final List<EventoDominio> eventos = new ArrayList<>();

        @Override
        public void publicar(EventoDominio evento) {
            eventos.add(evento);
        }

        public List<String> tipos() {
            return eventos.stream().map(EventoDominio::tipo).toList();
        }
    }

    /** Repositorio de fichas en memoria (sin cifrado; el dominio maneja texto plano). */
    public static final class RepositorioFichasFake implements RepositorioFichas {
        public final Map<String, FichaAntecedente> almacen = new LinkedHashMap<>();

        @Override
        public FichaAntecedente guardar(FichaAntecedente ficha) {
            almacen.put(ficha.altKey(), ficha);
            return ficha;
        }

        @Override
        public Optional<FichaAntecedente> buscarPorAltKey(String altKey) {
            return Optional.ofNullable(almacen.get(altKey));
        }

        @Override
        public void eliminarPorAltKey(String altKey) {
            almacen.remove(altKey);
        }

        @Override
        public Pagina<ResumenFicha> buscar(FiltroFichas filtro, CriterioTerritorial territorio) {
            List<ResumenFicha> contenido = almacen.values().stream()
                    .map(f -> new ResumenFicha(f.altKey(), f.folio(), f.estado(), f.calificacion(),
                            f.percepcionHallazgo(), f.unidadAlt(), f.sedeAlt(), f.casoAlt(),
                            f.creadoEn(), f.actualizadoEn()))
                    .toList();
            return new Pagina<>(contenido, filtro.pagina(), filtro.tamano(), contenido.size());
        }
    }

    /** Repositorio de hallazgos en memoria. */
    public static final class RepositorioHallazgosFake implements RepositorioHallazgos {
        public final Map<String, Hallazgo> almacen = new LinkedHashMap<>();

        @Override
        public Hallazgo guardar(Hallazgo hallazgo) {
            almacen.put(hallazgo.altKey(), hallazgo);
            return hallazgo;
        }

        @Override
        public Optional<Hallazgo> buscarPorAltKey(String altKey) {
            return Optional.ofNullable(almacen.get(altKey));
        }

        @Override
        public boolean existePorAltKey(String altKey) {
            return altKey != null && almacen.containsKey(altKey);
        }

        @Override
        public Pagina<Hallazgo> buscar(FiltroHallazgos filtro) {
            List<Hallazgo> contenido = new ArrayList<>(almacen.values());
            return new Pagina<>(contenido, filtro.pagina(), filtro.tamano(), contenido.size());
        }
    }

    /**
     * Repositorio de referencias en memoria. Por defecto considera vigente cualquier alt_key
     * registrado mediante {@link #registrarVigente}.
     */
    public static final class RepositorioReferenciasFake implements RepositorioReferencias {
        private final Map<TipoReferencia, Map<String, Referencia>> porTipo = new LinkedHashMap<>();

        public void registrarVigente(TipoReferencia tipo, String altKey) {
            Referencia r = new Referencia(altKey, tipo, "COD-" + altKey, "Nombre " + altKey, true,
                    Instant.EPOCH, Instant.EPOCH);
            porTipo.computeIfAbsent(tipo, t -> new LinkedHashMap<>()).put(altKey, r);
        }

        @Override
        public Referencia guardar(Referencia referencia) {
            porTipo.computeIfAbsent(referencia.tipo(), t -> new LinkedHashMap<>())
                    .put(referencia.altKey(), referencia);
            return referencia;
        }

        @Override
        public Optional<Referencia> buscarPorAltKey(TipoReferencia tipo, String altKey) {
            return Optional.ofNullable(porTipo.getOrDefault(tipo, Map.of()).get(altKey));
        }

        @Override
        public boolean existePorCodigo(TipoReferencia tipo, String codigo) {
            return porTipo.getOrDefault(tipo, Map.of()).values().stream()
                    .anyMatch(r -> r.codigo().equals(codigo));
        }

        @Override
        public boolean existeVigentePorAltKey(TipoReferencia tipo, String altKey) {
            Referencia r = porTipo.getOrDefault(tipo, Map.of()).get(altKey);
            return r != null && r.vigente();
        }

        @Override
        public Pagina<Referencia> listar(TipoReferencia tipo, String texto, Boolean vigente, int pagina, int tamano) {
            List<Referencia> contenido = new ArrayList<>(porTipo.getOrDefault(tipo, Map.of()).values());
            return new Pagina<>(contenido, pagina, tamano, contenido.size());
        }
    }
}
