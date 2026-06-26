package cl.smid.casos.soporte;

import cl.smid.casos.dominio.excepcion.CasoYaMaterializadoException;
import cl.smid.casos.dominio.modelo.Caso;
import cl.smid.casos.dominio.modelo.DatosEnriquecimiento;
import cl.smid.casos.dominio.modelo.FiltroCasos;
import cl.smid.casos.dominio.modelo.Pagina;
import cl.smid.casos.dominio.modelo.SerieExpediente;
import cl.smid.casos.dominio.puerto.salida.ClienteRequerimientos;
import cl.smid.casos.dominio.puerto.salida.CorrelativoExpedientePort;
import cl.smid.casos.dominio.puerto.salida.DirectorioSedes;
import cl.smid.casos.dominio.puerto.salida.GeneradorIdentificadores;
import cl.smid.casos.dominio.puerto.salida.PublicadorEventos;
import cl.smid.casos.dominio.puerto.salida.Reloj;
import cl.smid.casos.dominio.puerto.salida.RepositorioCasos;

import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Conjunto de dobles de prueba (in-memory) de los puertos de salida, para ejercitar el dominio sin
 * Spring, JPA, Docker ni red. Reúne implementaciones simples y deterministas.
 */
public final class DoblesEnMemoria {

    private DoblesEnMemoria() {
    }

    /** Repositorio en memoria que respeta la unicidad de requerimiento de origen (idempotencia). */
    public static final class RepositorioEnMemoria implements RepositorioCasos {
        private final Map<String, Caso> porAltKey = new ConcurrentHashMap<>();
        private final Map<String, String> origenAAltKey = new ConcurrentHashMap<>();

        @Override
        public Caso guardar(Caso caso) {
            boolean esNuevo = !porAltKey.containsKey(caso.altKey());
            if (esNuevo) {
                String existente = origenAAltKey.get(caso.idRequerimientoOrigenAlt());
                if (existente != null && !existente.equals(caso.altKey())) {
                    throw new CasoYaMaterializadoException(caso.idRequerimientoOrigenAlt());
                }
            }
            Caso almacenado = Caso.reconstituir(
                    caso.altKey(), caso.numeroExpediente(), caso.idRequerimientoOrigenAlt(),
                    caso.folioRequerimiento(), caso.idSedeAlt(), caso.idUnidadAlt(),
                    caso.idProfesionalResponsableAlt(), caso.estado(), caso.complejidad(),
                    caso.requiereFichaReservada(), caso.esBeta(), caso.abiertoEn(), caso.cerradoEn(),
                    caso.creadoEn(), caso.actualizadoEn(), caso.creadoPor(), caso.historialCompleto());
            porAltKey.put(almacenado.altKey(), almacenado);
            origenAAltKey.put(almacenado.idRequerimientoOrigenAlt(), almacenado.altKey());
            return almacenado;
        }

        @Override
        public Optional<Caso> buscarPorAltKey(String altKey) {
            return Optional.ofNullable(porAltKey.get(altKey));
        }

        @Override
        public Optional<Caso> buscarPorRequerimientoOrigen(String requerimientoOrigenAlt) {
            String altKey = origenAAltKey.get(requerimientoOrigenAlt);
            return altKey == null ? Optional.empty() : Optional.ofNullable(porAltKey.get(altKey));
        }

        @Override
        public boolean existePorRequerimientoOrigen(String requerimientoOrigenAlt) {
            return origenAAltKey.containsKey(requerimientoOrigenAlt);
        }

        @Override
        public Pagina<Caso> listar(FiltroCasos filtro, int pagina, int tamano) {
            List<Caso> todos = new ArrayList<>(porAltKey.values());
            List<Caso> filtrados = new ArrayList<>();
            for (Caso c : todos) {
                if (filtro.estado() != null && c.estado() != filtro.estado()) {
                    continue;
                }
                boolean visible = switch (filtro.alcance()) {
                    case NACIONAL -> true;
                    case SEDE -> c.idSedeAlt().equals(filtro.ctxSedeAlt());
                    case UNIDAD -> c.idUnidadAlt() != null && c.idUnidadAlt().equals(filtro.ctxUnidadAlt());
                };
                if (visible) {
                    filtrados.add(c);
                }
            }
            return new Pagina<>(filtrados, pagina, tamano, filtrados.size());
        }

        public int cantidad() {
            return porAltKey.size();
        }
    }

    /** Correlativo en memoria, atómico por (sede, año, serie). */
    public static final class CorrelativoEnMemoria implements CorrelativoExpedientePort {
        private final Map<String, AtomicLong> contadores = new ConcurrentHashMap<>();

        @Override
        public long reservarSiguiente(String idSedeAlt, int anio, SerieExpediente serie) {
            String clave = idSedeAlt + "|" + anio + "|" + serie.name();
            return contadores.computeIfAbsent(clave, k -> new AtomicLong(0)).incrementAndGet();
        }
    }

    /** Directorio de sedes que siempre devuelve el mismo código. */
    public static final class DirectorioSedesFijo implements DirectorioSedes {
        private final String codigo;

        public DirectorioSedesFijo(String codigo) {
            this.codigo = codigo;
        }

        @Override
        public String codigoDe(String idSedeAlt) {
            return codigo;
        }
    }

    /** Publicador que acumula los eventos emitidos para su verificación. */
    public static final class PublicadorEnMemoria implements PublicadorEventos {
        private final List<EventoDominio> eventos = new ArrayList<>();

        @Override
        public synchronized void publicar(EventoDominio evento) {
            eventos.add(evento);
        }

        public synchronized List<EventoDominio> eventos() {
            return new ArrayList<>(eventos);
        }

        public synchronized List<String> tipos() {
            List<String> tipos = new ArrayList<>();
            for (EventoDominio e : eventos) {
                tipos.add(e.tipo());
            }
            return tipos;
        }
    }

    /** Cliente de enriquecimiento que devuelve un valor configurable. */
    public static final class ClienteRequerimientosFijo implements ClienteRequerimientos {
        private final DatosEnriquecimiento valor;

        public ClienteRequerimientosFijo(DatosEnriquecimiento valor) {
            this.valor = valor;
        }

        public ClienteRequerimientosFijo() {
            this(DatosEnriquecimiento.noDisponible());
        }

        @Override
        public DatosEnriquecimiento enriquecer(String requerimientoOrigenAlt, String tokenBearer) {
            return valor;
        }
    }

    /** Reloj fijo en un instante dado. */
    public static final class RelojFijo implements Reloj {
        private Instant instante;

        public RelojFijo(Instant instante) {
            this.instante = instante;
        }

        public void fijar(Instant instante) {
            this.instante = instante;
        }

        @Override
        public Instant ahora() {
            return instante;
        }
    }

    /** Generador determinista de identificadores (prefijo + secuencia) para aserciones estables. */
    public static final class GeneradorSecuencial implements GeneradorIdentificadores {
        private final AtomicLong secuencia = new AtomicLong(0);
        private final Map<Long, String> emitidos = new LinkedHashMap<>();

        @Override
        public String nuevoAltKey() {
            long n = secuencia.incrementAndGet();
            String valor = String.format("00000000-0000-0000-0000-%012d", n);
            emitidos.put(n, valor);
            return valor;
        }
    }
}
