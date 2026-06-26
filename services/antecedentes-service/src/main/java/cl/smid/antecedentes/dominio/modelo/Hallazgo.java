package cl.smid.antecedentes.dominio.modelo;

import cl.smid.antecedentes.dominio.excepcion.CodigoError;
import cl.smid.antecedentes.dominio.excepcion.ReglaNegocioException;

import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Agregado raiz <strong>Hallazgo</strong>: conocimiento institucional agregado que la
 * Unidad de Estudios y Gestion consolida. Un hallazgo nace {@code PROPUESTO} (por una ficha
 * que lo propone o por creacion directa de gestion) y evoluciona a {@code ASOCIADO} o
 * {@code RECHAZADO}.
 *
 * <p>POJO puro de dominio: sin Spring/JPA/Lombok. Las referencias externas
 * ({@code instrumentoAlt}, {@code unidadesInvolucradas}, {@code institucionesExternas},
 * {@code origenFichaAlt}) se guardan como alt_key opacos; no se verifican contra otros
 * servicios (servicio hoja).</p>
 */
public class Hallazgo {

    private final String altKey;
    private final String folio;
    private String titulo;
    private String descripcion;
    private EstadoHallazgo estado;
    /** alt_key del instrumento de referencia (la PK interna nunca se expone). */
    private String instrumentoAlt;
    private Temporalidad temporalidad;
    private final List<String> unidadesInvolucradas = new ArrayList<>();
    private final List<String> institucionesExternas = new ArrayList<>();
    /** alt_key de la ficha que origino el hallazgo (puede ser nulo en creacion directa). */
    private final String origenFichaAlt;
    private final Instant creadoEn;
    private Instant actualizadoEn;

    /**
     * Reconstruye un hallazgo desde persistencia.
     */
    public Hallazgo(String altKey, String folio, String titulo, String descripcion, EstadoHallazgo estado,
                    String instrumentoAlt, Temporalidad temporalidad, List<String> unidadesInvolucradas,
                    List<String> institucionesExternas, String origenFichaAlt, Instant creadoEn,
                    Instant actualizadoEn) {
        this.altKey = exigir(altKey, "alt_key del hallazgo");
        this.folio = exigir(folio, "folio del hallazgo");
        this.titulo = exigir(titulo, "titulo del hallazgo");
        this.descripcion = exigir(descripcion, "descripcion del hallazgo");
        if (estado == null) {
            throw new IllegalArgumentException("El estado del hallazgo es obligatorio");
        }
        if (temporalidad == null) {
            throw new IllegalArgumentException("La temporalidad del hallazgo es obligatoria");
        }
        this.estado = estado;
        this.instrumentoAlt = instrumentoAlt;
        this.temporalidad = temporalidad;
        if (unidadesInvolucradas != null) {
            this.unidadesInvolucradas.addAll(deduplicar(unidadesInvolucradas));
        }
        if (institucionesExternas != null) {
            this.institucionesExternas.addAll(deduplicar(institucionesExternas));
        }
        this.origenFichaAlt = origenFichaAlt;
        if (creadoEn == null || actualizadoEn == null) {
            throw new IllegalArgumentException("Las marcas temporales del hallazgo son obligatorias");
        }
        this.creadoEn = creadoEn;
        this.actualizadoEn = actualizadoEn;
    }

    /**
     * Crea un hallazgo nuevo en estado {@code PROPUESTO}.
     *
     * @param altKey                identificador publico opaco
     * @param folio                 folio HZ ya generado
     * @param titulo                titulo
     * @param descripcion           descripcion
     * @param instrumentoAlt        alt_key del instrumento (obligatorio para la propuesta)
     * @param temporalidad          horizonte temporal
     * @param unidadesInvolucradas  alt_key de unidades involucradas (puede ser vacio)
     * @param institucionesExternas alt_key de instituciones externas (puede ser vacio)
     * @param origenFichaAlt        alt_key de la ficha de origen (nulo si creacion directa)
     * @param ahora                 instante UTC de creacion
     */
    public static Hallazgo proponer(String altKey, String folio, String titulo, String descripcion,
                                    String instrumentoAlt, Temporalidad temporalidad,
                                    List<String> unidadesInvolucradas, List<String> institucionesExternas,
                                    String origenFichaAlt, Instant ahora) {
        if (instrumentoAlt == null || instrumentoAlt.isBlank()) {
            throw new ReglaNegocioException(CodigoError.ANT_422,
                    "Un hallazgo propuesto exige un instrumento de referencia.");
        }
        return new Hallazgo(altKey, folio, titulo, descripcion, EstadoHallazgo.PROPUESTO, instrumentoAlt,
                temporalidad, unidadesInvolucradas, institucionesExternas, origenFichaAlt, ahora, ahora);
    }

    /**
     * Edita los campos mutables de una propuesta. Solo se permite mientras el hallazgo
     * sigue {@code PROPUESTO}; en estados terminales lanza {@link ReglaNegocioException}.
     */
    public void editar(String titulo, String descripcion, String instrumentoAlt, Temporalidad temporalidad,
                       List<String> unidadesInvolucradas, List<String> institucionesExternas, Instant ahora) {
        if (estado != EstadoHallazgo.PROPUESTO) {
            throw new ReglaNegocioException(CodigoError.ANT_422,
                    "Solo se puede editar un hallazgo en estado PROPUESTO.");
        }
        this.titulo = exigir(titulo, "titulo del hallazgo");
        this.descripcion = exigir(descripcion, "descripcion del hallazgo");
        this.instrumentoAlt = exigir(instrumentoAlt, "instrumento del hallazgo");
        if (temporalidad == null) {
            throw new IllegalArgumentException("La temporalidad del hallazgo es obligatoria");
        }
        this.temporalidad = temporalidad;
        this.unidadesInvolucradas.clear();
        if (unidadesInvolucradas != null) {
            this.unidadesInvolucradas.addAll(deduplicar(unidadesInvolucradas));
        }
        this.institucionesExternas.clear();
        if (institucionesExternas != null) {
            this.institucionesExternas.addAll(deduplicar(institucionesExternas));
        }
        this.actualizadoEn = ahora;
    }

    /**
     * Cambia el estado de la propuesta a {@code ASOCIADO} o {@code RECHAZADO}. Solo es
     * valido desde {@code PROPUESTO}; cualquier otra transicion lanza {@code ANT-422}.
     */
    public void cambiarEstado(EstadoHallazgo destino, Instant ahora) {
        if (destino == null || destino == EstadoHallazgo.PROPUESTO) {
            throw new ReglaNegocioException(CodigoError.ANT_422,
                    "El estado destino del hallazgo debe ser ASOCIADO o RECHAZADO.");
        }
        if (estado != EstadoHallazgo.PROPUESTO) {
            throw new ReglaNegocioException(CodigoError.ANT_422,
                    "El hallazgo no esta en estado PROPUESTO; no admite cambio de estado.");
        }
        this.estado = destino;
        this.actualizadoEn = ahora;
    }

    private static String exigir(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("El " + campo + " es obligatorio");
        }
        return valor.trim();
    }

    private static List<String> deduplicar(List<String> valores) {
        Set<String> set = new LinkedHashSet<>();
        for (String v : valores) {
            if (v != null && !v.isBlank()) {
                set.add(v.trim());
            }
        }
        return new ArrayList<>(set);
    }

    public String altKey() {
        return altKey;
    }

    public String folio() {
        return folio;
    }

    public String titulo() {
        return titulo;
    }

    public String descripcion() {
        return descripcion;
    }

    public EstadoHallazgo estado() {
        return estado;
    }

    public String instrumentoAlt() {
        return instrumentoAlt;
    }

    public Temporalidad temporalidad() {
        return temporalidad;
    }

    public List<String> unidadesInvolucradas() {
        return List.copyOf(unidadesInvolucradas);
    }

    public List<String> institucionesExternas() {
        return List.copyOf(institucionesExternas);
    }

    public String origenFichaAlt() {
        return origenFichaAlt;
    }

    public Instant creadoEn() {
        return creadoEn;
    }

    public Instant actualizadoEn() {
        return actualizadoEn;
    }
}
