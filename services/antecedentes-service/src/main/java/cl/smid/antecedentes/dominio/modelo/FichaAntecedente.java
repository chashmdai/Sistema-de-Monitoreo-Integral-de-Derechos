package cl.smid.antecedentes.dominio.modelo;

import cl.smid.antecedentes.dominio.excepcion.CodigoError;
import cl.smid.antecedentes.dominio.excepcion.ReglaNegocioException;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Agregado raiz <strong>FichaAntecedente</strong> (RF028): aprendizaje institucional que un
 * profesional levanta a partir de un caso. POJO puro de dominio (sin Spring/JPA/Lombok),
 * dueno de sus invariantes:
 *
 * <ul>
 *   <li>Maquina de estados {@code BORRADOR -> EN_REVISION -> APROBADA|RECHAZADA} (la
 *       legalidad de las transiciones la valida {@code MaquinaEstadosFicha}; el agregado
 *       las aplica y registra en el historial).</li>
 *   <li>Edicion/eliminacion <strong>solo</strong> en {@code BORRADOR}.</li>
 *   <li>Articulos CDN enteros en 1..54 sin duplicados.</li>
 *   <li>Maximo 2 categorias secundarias; la principal no se repite en secundarias.</li>
 *   <li>Coherencia de percepcion de hallazgo vs vinculo {@code hallazgoAlt}.</li>
 * </ul>
 *
 * <p>El {@code relato} se maneja en claro en el dominio; el cifrado AES-256-GCM en reposo lo
 * aplica el adaptador de persistencia (el dominio no conoce el cifrado). El contexto
 * territorial ({@code unidadAlt}, {@code sedeAlt}) y la autoria ({@code profesionalAlt}) se
 * fijan al crear y son inmutables. Las referencias ({@code procesoAlt},
 * {@code categoriaPrincipalAlt}, secundarias, {@code hallazgoAlt}, {@code casoAlt}) se guardan
 * como alt_key opacos.</p>
 */
public class FichaAntecedente {

    public static final int CDN_MIN = 1;
    public static final int CDN_MAX = 54;
    public static final int MAX_CATEGORIAS_SECUNDARIAS = 2;

    private final String altKey;
    private final String folio;
    private EstadoFicha estado;

    // Contexto territorial y autoria (inmutables tras la creacion).
    private final String unidadAlt;
    private final String sedeAlt;
    private final String profesionalAlt;
    private String jefaturaAlt;

    // Referencias (alt_key opacos).
    private String procesoAlt;
    private String casoAlt;
    private String categoriaPrincipalAlt;
    private final List<String> categoriasSecundariasAlt = new ArrayList<>();
    private final List<Integer> derechosCdn = new ArrayList<>();

    // Contenido.
    private String descripcion;
    private String relato;
    private Calificacion calificacion;
    private final Set<Criterio> criterios = new LinkedHashSet<>();

    // Evaluacion de hallazgo.
    private PercepcionHallazgo percepcionHallazgo;
    private String hallazgoAlt;

    // Hijos.
    private final List<DocumentoAsociado> documentos = new ArrayList<>();
    private final List<AsientoHistorial> historial = new ArrayList<>();

    private final Instant creadoEn;
    private Instant actualizadoEn;

    /**
     * Constructor de reconstruccion desde persistencia (todos los campos conocidos).
     */
    public FichaAntecedente(String altKey, String folio, EstadoFicha estado, String unidadAlt, String sedeAlt,
                            String profesionalAlt, String jefaturaAlt, String procesoAlt, String casoAlt,
                            String categoriaPrincipalAlt, List<String> categoriasSecundariasAlt,
                            List<Integer> derechosCdn, String descripcion, String relato, Calificacion calificacion,
                            Set<Criterio> criterios, PercepcionHallazgo percepcionHallazgo, String hallazgoAlt,
                            List<DocumentoAsociado> documentos, List<AsientoHistorial> historial,
                            Instant creadoEn, Instant actualizadoEn) {
        this.altKey = exigir(altKey, "alt_key de la ficha");
        this.folio = exigir(folio, "folio de la ficha");
        this.estado = exigirNoNulo(estado, "estado");
        this.unidadAlt = exigir(unidadAlt, "unidad de la ficha");
        this.sedeAlt = exigir(sedeAlt, "sede de la ficha");
        this.profesionalAlt = exigir(profesionalAlt, "profesional autor");
        this.jefaturaAlt = normalizar(jefaturaAlt);
        this.procesoAlt = exigir(procesoAlt, "proceso");
        this.casoAlt = normalizar(casoAlt);
        this.categoriaPrincipalAlt = exigir(categoriaPrincipalAlt, "categoria principal");
        if (categoriasSecundariasAlt != null) {
            this.categoriasSecundariasAlt.addAll(deduplicar(categoriasSecundariasAlt));
        }
        if (derechosCdn != null) {
            this.derechosCdn.addAll(deduplicarEnteros(derechosCdn));
        }
        this.descripcion = exigir(descripcion, "descripcion");
        this.relato = exigir(relato, "relato");
        this.calificacion = exigirNoNulo(calificacion, "calificacion");
        if (criterios != null) {
            this.criterios.addAll(criterios);
        }
        this.percepcionHallazgo = exigirNoNulo(percepcionHallazgo, "percepcion de hallazgo");
        this.hallazgoAlt = normalizar(hallazgoAlt);
        if (documentos != null) {
            this.documentos.addAll(documentos);
        }
        if (historial != null) {
            this.historial.addAll(historial);
        }
        this.creadoEn = exigirNoNulo(creadoEn, "creadoEn");
        this.actualizadoEn = exigirNoNulo(actualizadoEn, "actualizadoEn");
    }

    /**
     * Crea una ficha nueva en estado {@code BORRADOR}, validando todas las invariantes y
     * asentando el hito {@code CREACION} en el historial. El {@code hallazgoAlt} ya debe venir
     * resuelto por el servicio (nulo, existente, o el recien propuesto), coherente con la
     * percepcion.
     */
    public static FichaAntecedente crear(String altKey, String folio, String unidadAlt, String sedeAlt,
                                         String profesionalAlt, String jefaturaAlt, String procesoAlt,
                                         String casoAlt, String categoriaPrincipalAlt,
                                         List<String> categoriasSecundariasAlt, List<Integer> derechosCdn,
                                         String descripcion, String relato, Calificacion calificacion,
                                         Set<Criterio> criterios, PercepcionHallazgo percepcionHallazgo,
                                         String hallazgoAlt, Instant ahora) {
        FichaAntecedente ficha = new FichaAntecedente(altKey, folio, EstadoFicha.BORRADOR, unidadAlt, sedeAlt,
                profesionalAlt, jefaturaAlt, procesoAlt, casoAlt, categoriaPrincipalAlt, categoriasSecundariasAlt,
                derechosCdn, descripcion, relato, calificacion, criterios, percepcionHallazgo, hallazgoAlt,
                List.of(), List.of(), ahora, ahora);
        ficha.validarCategorias();
        ficha.validarDerechos();
        ficha.validarCoherenciaHallazgo();
        ficha.historial.add(new AsientoHistorial("CREACION", profesionalAlt, ahora, null));
        return ficha;
    }

    /**
     * Edita los campos mutables de la ficha. Solo permitido en {@code BORRADOR}; en otro
     * estado lanza {@code ANT-422}. El {@code hallazgoAlt} ya debe venir resuelto por el
     * servicio de forma coherente con la nueva percepcion.
     */
    public void editar(String jefaturaAlt, String procesoAlt, String casoAlt, String categoriaPrincipalAlt,
                       List<String> categoriasSecundariasAlt, List<Integer> derechosCdn, String descripcion,
                       String relato, Calificacion calificacion, Set<Criterio> criterios,
                       PercepcionHallazgo percepcionHallazgo, String hallazgoAlt, Instant ahora) {
        if (!estado.esMutable()) {
            throw new ReglaNegocioException(CodigoError.ANT_422,
                    "Solo una ficha en BORRADOR admite edicion.");
        }
        this.jefaturaAlt = normalizar(jefaturaAlt);
        this.procesoAlt = exigir(procesoAlt, "proceso");
        this.casoAlt = normalizar(casoAlt);
        this.categoriaPrincipalAlt = exigir(categoriaPrincipalAlt, "categoria principal");
        this.categoriasSecundariasAlt.clear();
        if (categoriasSecundariasAlt != null) {
            this.categoriasSecundariasAlt.addAll(deduplicar(categoriasSecundariasAlt));
        }
        this.derechosCdn.clear();
        if (derechosCdn != null) {
            this.derechosCdn.addAll(deduplicarEnteros(derechosCdn));
        }
        this.descripcion = exigir(descripcion, "descripcion");
        this.relato = exigir(relato, "relato");
        this.calificacion = exigirNoNulo(calificacion, "calificacion");
        this.criterios.clear();
        if (criterios != null) {
            this.criterios.addAll(criterios);
        }
        this.percepcionHallazgo = exigirNoNulo(percepcionHallazgo, "percepcion de hallazgo");
        this.hallazgoAlt = normalizar(hallazgoAlt);
        validarCategorias();
        validarDerechos();
        validarCoherenciaHallazgo();
        this.actualizadoEn = ahora;
    }

    /**
     * Aplica una transicion de estado ya validada por la maquina de estados, registrando el
     * hito en el historial. No revalida la legalidad (responsabilidad de
     * {@code MaquinaEstadosFicha}); solo aplica destino, marca temporal e historial.
     */
    public void aplicarTransicion(AccionRevision accion, EstadoFicha destino, String actorAlt,
                                  String observacion, Instant ahora) {
        this.estado = exigirNoNulo(destino, "estado destino");
        this.actualizadoEn = exigirNoNulo(ahora, "instante de transicion");
        this.historial.add(new AsientoHistorial(accion.name(), actorAlt, ahora, normalizar(observacion)));
    }

    /** Agrega el metadato de un documento. Disponible en cualquier estado. */
    public void agregarDocumento(DocumentoAsociado documento, Instant ahora) {
        if (documento == null) {
            throw new IllegalArgumentException("El documento es obligatorio");
        }
        this.documentos.add(documento);
        this.actualizadoEn = ahora;
    }

    // ---------------------------------------------------------------------------------------
    // Invariantes
    // ---------------------------------------------------------------------------------------

    private void validarDerechos() {
        Set<Integer> vistos = new LinkedHashSet<>();
        for (Integer articulo : derechosCdn) {
            if (articulo == null || articulo < CDN_MIN || articulo > CDN_MAX) {
                throw new ReglaNegocioException(CodigoError.ANT_422,
                        "Los articulos CDN deben ser enteros entre " + CDN_MIN + " y " + CDN_MAX + ".");
            }
            if (!vistos.add(articulo)) {
                throw new ReglaNegocioException(CodigoError.ANT_422,
                        "Los articulos CDN no pueden repetirse.");
            }
        }
    }

    private void validarCategorias() {
        if (categoriasSecundariasAlt.size() > MAX_CATEGORIAS_SECUNDARIAS) {
            throw new ReglaNegocioException(CodigoError.ANT_422,
                    "Se admiten como maximo " + MAX_CATEGORIAS_SECUNDARIAS + " categorias secundarias.");
        }
        if (categoriasSecundariasAlt.contains(categoriaPrincipalAlt)) {
            throw new ReglaNegocioException(CodigoError.ANT_422,
                    "La categoria principal no puede repetirse entre las secundarias.");
        }
    }

    /**
     * Coherencia entre la percepcion de hallazgo y el vinculo {@code hallazgoAlt}:
     * {@code NO_ES_HALLAZGO} prohibe vinculo; {@code ANTECEDENTE_DE_HALLAZGO} y
     * {@code SE_PROPONE_HALLAZGO} exigen vinculo.
     */
    private void validarCoherenciaHallazgo() {
        boolean tieneVinculo = hallazgoAlt != null && !hallazgoAlt.isBlank();
        switch (percepcionHallazgo) {
            case NO_ES_HALLAZGO -> {
                if (tieneVinculo) {
                    throw new ReglaNegocioException(CodigoError.ANT_422,
                            "NO_ES_HALLAZGO no admite vinculo a un hallazgo.");
                }
            }
            case ANTECEDENTE_DE_HALLAZGO -> {
                if (!tieneVinculo) {
                    throw new ReglaNegocioException(CodigoError.ANT_422,
                            "ANTECEDENTE_DE_HALLAZGO exige un hallazgo existente.");
                }
            }
            case SE_PROPONE_HALLAZGO -> {
                if (!tieneVinculo) {
                    throw new ReglaNegocioException(CodigoError.ANT_422,
                            "SE_PROPONE_HALLAZGO debe quedar vinculada al hallazgo propuesto.");
                }
            }
        }
    }

    // ---------------------------------------------------------------------------------------
    // Utilidades
    // ---------------------------------------------------------------------------------------

    private static String exigir(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new ReglaNegocioException(CodigoError.ANT_422, "El campo '" + campo + "' es obligatorio.");
        }
        return valor.trim();
    }

    private static <T> T exigirNoNulo(T valor, String campo) {
        if (valor == null) {
            throw new ReglaNegocioException(CodigoError.ANT_422, "El campo '" + campo + "' es obligatorio.");
        }
        return valor;
    }

    private static String normalizar(String valor) {
        if (valor == null) {
            return null;
        }
        String limpio = valor.trim();
        return limpio.isEmpty() ? null : limpio;
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

    private static List<Integer> deduplicarEnteros(List<Integer> valores) {
        Set<Integer> set = new LinkedHashSet<>();
        for (Integer v : valores) {
            if (v != null) {
                set.add(v);
            }
        }
        return new ArrayList<>(set);
    }

    // ---------------------------------------------------------------------------------------
    // Accesores (solo lectura; las colecciones se devuelven como copias inmutables)
    // ---------------------------------------------------------------------------------------

    public String altKey() {
        return altKey;
    }

    public String folio() {
        return folio;
    }

    public EstadoFicha estado() {
        return estado;
    }

    public String unidadAlt() {
        return unidadAlt;
    }

    public String sedeAlt() {
        return sedeAlt;
    }

    public String profesionalAlt() {
        return profesionalAlt;
    }

    public String jefaturaAlt() {
        return jefaturaAlt;
    }

    public String procesoAlt() {
        return procesoAlt;
    }

    public String casoAlt() {
        return casoAlt;
    }

    public String categoriaPrincipalAlt() {
        return categoriaPrincipalAlt;
    }

    public List<String> categoriasSecundariasAlt() {
        return List.copyOf(categoriasSecundariasAlt);
    }

    public List<Integer> derechosCdn() {
        return List.copyOf(derechosCdn);
    }

    public String descripcion() {
        return descripcion;
    }

    public String relato() {
        return relato;
    }

    public Calificacion calificacion() {
        return calificacion;
    }

    public Set<Criterio> criterios() {
        return Collections.unmodifiableSet(new LinkedHashSet<>(criterios));
    }

    public PercepcionHallazgo percepcionHallazgo() {
        return percepcionHallazgo;
    }

    public String hallazgoAlt() {
        return hallazgoAlt;
    }

    public List<DocumentoAsociado> documentos() {
        return List.copyOf(documentos);
    }

    public List<AsientoHistorial> historial() {
        return List.copyOf(historial);
    }

    public Instant creadoEn() {
        return creadoEn;
    }

    public Instant actualizadoEn() {
        return actualizadoEn;
    }
}
