package cl.smid.instituciones.dominio.modelo;

import cl.smid.instituciones.dominio.excepcion.ExcepcionValidacion;

import java.time.Instant;

/**
 * Agregado <strong>TipoInstitucion</strong>: catálogo de clasificación plano de las
 * instituciones (sin máquina de estados; vigencia por bandera de baja lógica).
 *
 * <p>Invariantes:</p>
 * <ul>
 *   <li>{@code nombre} obligatorio y normalizado (único a nivel de persistencia).</li>
 *   <li>{@code ambito} obligatorio.</li>
 * </ul>
 *
 * <p>El dominio trabaja en {@link Instant} (UTC); la conversión a {@code LocalDateTime}
 * de las entidades JPA ocurre en la capa de persistencia.</p>
 */
public final class TipoInstitucion {

    private final String altKey;
    private String nombre;
    private Ambito ambito;
    private String descripcion;
    private boolean vigente;
    private final Instant creadoEn;
    private Instant actualizadoEn;

    private TipoInstitucion(String altKey, String nombre, Ambito ambito, String descripcion,
                            boolean vigente, Instant creadoEn, Instant actualizadoEn) {
        this.altKey = altKey;
        this.nombre = nombre;
        this.ambito = ambito;
        this.descripcion = descripcion;
        this.vigente = vigente;
        this.creadoEn = creadoEn;
        this.actualizadoEn = actualizadoEn;
    }

    /**
     * Crea un tipo nuevo, vigente, validando obligatoriedad de nombre y ámbito.
     *
     * @param altKey      identificador público generado
     * @param nombre      nombre (obligatorio; se normaliza)
     * @param ambito      ámbito sectorial (obligatorio)
     * @param descripcion descripción opcional (se limpia)
     * @param ahora       instante de creación (UTC)
     * @return el tipo recién creado
     */
    public static TipoInstitucion crear(String altKey, String nombre, Ambito ambito,
                                        String descripcion, Instant ahora) {
        if (Textos.esVacio(nombre)) {
            throw new ExcepcionValidacion("El nombre del tipo de institución es obligatorio.");
        }
        if (ambito == null) {
            throw new ExcepcionValidacion("El ámbito del tipo de institución es obligatorio.");
        }
        return new TipoInstitucion(
                altKey,
                Textos.normalizarNombre(nombre),
                ambito,
                Textos.limpiarOpcional(descripcion),
                true,
                ahora,
                ahora);
    }

    /**
     * Reconstruye un tipo desde persistencia (sin re-validar; los datos ya son válidos).
     */
    public static TipoInstitucion reconstruir(String altKey, String nombre, Ambito ambito,
                                              String descripcion, boolean vigente,
                                              Instant creadoEn, Instant actualizadoEn) {
        return new TipoInstitucion(altKey, nombre, ambito, descripcion, vigente, creadoEn, actualizadoEn);
    }

    /**
     * Actualiza los datos editables del tipo.
     *
     * @param nombre      nuevo nombre (obligatorio; se normaliza)
     * @param ambito      nuevo ámbito (obligatorio)
     * @param descripcion nueva descripción opcional
     * @param ahora       instante de la edición (UTC)
     */
    public void actualizar(String nombre, Ambito ambito, String descripcion, Instant ahora) {
        if (Textos.esVacio(nombre)) {
            throw new ExcepcionValidacion("El nombre del tipo de institución es obligatorio.");
        }
        if (ambito == null) {
            throw new ExcepcionValidacion("El ámbito del tipo de institución es obligatorio.");
        }
        this.nombre = Textos.normalizarNombre(nombre);
        this.ambito = ambito;
        this.descripcion = Textos.limpiarOpcional(descripcion);
        this.actualizadoEn = ahora;
    }

    /**
     * Activa o desactiva (baja lógica) el tipo.
     *
     * @param vigente nuevo estado de vigencia
     * @param ahora   instante del cambio (UTC)
     */
    public void establecerVigencia(boolean vigente, Instant ahora) {
        this.vigente = vigente;
        this.actualizadoEn = ahora;
    }

    public String altKey() {
        return altKey;
    }

    public String nombre() {
        return nombre;
    }

    public Ambito ambito() {
        return ambito;
    }

    public String descripcion() {
        return descripcion;
    }

    public boolean vigente() {
        return vigente;
    }

    public Instant creadoEn() {
        return creadoEn;
    }

    public Instant actualizadoEn() {
        return actualizadoEn;
    }
}
