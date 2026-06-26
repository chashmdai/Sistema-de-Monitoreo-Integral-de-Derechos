package cl.smid.catalogo.dominio.modelo;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Modelo de dominio de un <b>Derecho</b> del catálogo (nodo del árbol taxonómico).
 *
 * <p>Es un POJO del núcleo: <b>no depende de Spring, ni de JPA, ni de jjwt</b>. Concentra
 * las reglas de negocio del derecho (invariantes de árbol, inmutabilidad del código,
 * borrado lógico) de forma que sean verificables con dobles en memoria, sin levantar
 * contexto ni base de datos.</p>
 *
 * <h2>Sobre las llaves</h2>
 * <ul>
 *   <li>{@code id} y {@code idPadre} son las llaves numéricas <b>internas</b> (PK BIGINT
 *       y FK de la lista de adyacencia). Son privadas del servicio: el algoritmo de árbol
 *       las usa para ensamblar la jerarquía, pero el adaptador de API jamás las expone
 *       (cierre de IDOR, Núcleo 2.2). En un derecho recién creado y aún no persistido,
 *       {@code id} es {@code null} hasta que el repositorio lo asigna.</li>
 *   <li>{@code altKey} es el identificador <b>público opaco</b> (UUID): lo único que viaja
 *       en URL y entre servicios.</li>
 * </ul>
 *
 * <h2>Invariantes que esta clase garantiza</h2>
 * <ul>
 *   <li>El {@code codigo} es <b>inmutable</b>: se fija en construcción y no expone mutador.</li>
 *   <li>El {@code nivel} se <b>deriva del padre</b> ({@code nivel(padre)+1}); una raíz es nivel 0.</li>
 *   <li>El borrado es <b>lógico</b>: {@link #darDeBaja(LocalDate)} marca {@code vigente=false}
 *       y fija {@code vigenteHasta}; nunca se elimina físicamente.</li>
 * </ul>
 */
public class Derecho {

    /** Nivel de una categoría raíz (sin padre). */
    public static final short NIVEL_RAIZ = 0;

    // --- Identidad interna (privada del servicio) ---
    private Long id;            // PK BIGINT; null mientras no se ha persistido
    private Long idPadre;       // FK a derecho(id); null en las raíces

    // --- Identidad pública ---
    private final String altKey; // UUID opaco

    // --- Atributos de negocio ---
    private final String codigo; // estable e INMUTABLE (p. ej. "EDU", "EDU.ACCESO")
    private String nombre;       // editable
    private String descripcion;  // editable, opcional
    private short nivel;         // derivado del padre
    private short orden;         // orden de despliegue entre hermanos

    // --- Estado de vigencia (borrado lógico) ---
    private boolean vigente;
    private LocalDate vigenteDesde;
    private LocalDate vigenteHasta; // null mientras está vigente

    /**
     * Constructor completo. Es de uso del mapeador de persistencia (reconstrucción desde BD)
     * y de las fábricas estáticas. Para crear derechos nuevos desde la lógica de negocio,
     * preferir {@link #nuevaRaiz} / {@link #nuevoHijo}.
     */
    public Derecho(Long id, Long idPadre, String altKey, String codigo, String nombre,
                   String descripcion, short nivel, short orden, boolean vigente,
                   LocalDate vigenteDesde, LocalDate vigenteHasta) {
        this.id = id;
        this.idPadre = idPadre;
        this.altKey = Objects.requireNonNull(altKey, "altKey no puede ser nulo");
        this.codigo = exigirCodigo(codigo);
        this.nombre = exigirNombre(nombre);
        this.descripcion = descripcion;
        this.nivel = nivel;
        this.orden = orden;
        this.vigente = vigente;
        this.vigenteDesde = vigenteDesde;
        this.vigenteHasta = vigenteHasta;
    }

    /**
     * Crea una categoría raíz (nivel 0, sin padre), vigente desde {@code hoy}.
     *
     * @param altKey      UUID público ya generado por el servicio
     * @param codigo      código estable e inmutable
     * @param nombre      nombre legible
     * @param descripcion descripción opcional
     * @param orden       orden de despliegue entre hermanos
     * @param hoy         fecha de inicio de vigencia (inyectada por el reloj del dominio)
     */
    public static Derecho nuevaRaiz(String altKey, String codigo, String nombre,
                                    String descripcion, short orden, LocalDate hoy) {
        return new Derecho(null, null, altKey, codigo, nombre, descripcion,
                NIVEL_RAIZ, orden, true, hoy, null);
    }

    /**
     * Crea un derecho hijo colgando de {@code padre}. El nivel se deriva del padre
     * ({@code padre.nivel + 1}) y la FK interna apunta al {@code id} del padre.
     *
     * @param padre derecho padre ya persistido (debe tener {@code id} no nulo)
     */
    public static Derecho nuevoHijo(String altKey, String codigo, String nombre,
                                    String descripcion, short orden, Derecho padre, LocalDate hoy) {
        Objects.requireNonNull(padre, "el padre no puede ser nulo");
        if (padre.id == null) {
            throw new IllegalStateException("El padre debe estar persistido para colgar un hijo");
        }
        short nivelHijo = (short) (padre.nivel + 1);
        return new Derecho(null, padre.id, altKey, codigo, nombre, descripcion,
                nivelHijo, orden, true, hoy, null);
    }

    // ----------------------------------------------------------------------
    // Comportamiento de negocio
    // ----------------------------------------------------------------------

    /** Renombra el derecho. El nombre es editable; el código no. */
    public void renombrar(String nuevoNombre) {
        this.nombre = exigirNombre(nuevoNombre);
    }

    /** Cambia la descripción (puede quedar nula). */
    public void cambiarDescripcion(String nuevaDescripcion) {
        this.descripcion = nuevaDescripcion;
    }

    /** Cambia el orden de despliegue entre hermanos. */
    public void cambiarOrden(short nuevoOrden) {
        this.orden = nuevoOrden;
    }

    /**
     * Reubica este derecho bajo un nuevo padre, recalculando su nivel.
     *
     * <p>La detección de ciclos y el recálculo del nivel de los descendientes son
     * responsabilidad del servicio de dominio (que conoce el subárbol completo); aquí
     * solo se ajustan la FK y el nivel propios.</p>
     *
     * @param nuevoPadre nuevo padre persistido (no puede ser este mismo nodo)
     */
    public void reubicarBajo(Derecho nuevoPadre) {
        Objects.requireNonNull(nuevoPadre, "el nuevo padre no puede ser nulo");
        if (nuevoPadre.id == null) {
            throw new IllegalStateException("El nuevo padre debe estar persistido");
        }
        if (Objects.equals(nuevoPadre.id, this.id)) {
            throw new IllegalArgumentException("Un derecho no puede ser su propio padre");
        }
        this.idPadre = nuevoPadre.id;
        this.nivel = (short) (nuevoPadre.nivel + 1);
    }

    /** Fija el nivel directamente (lo usa el servicio al propagar un cambio de nivel a los descendientes). */
    public void fijarNivel(short nuevoNivel) {
        this.nivel = nuevoNivel;
    }

    /**
     * Da de baja el derecho de forma <b>lógica</b>: marca {@code vigente=false} y fija
     * {@code vigenteHasta = hoy}. Es idempotente: si ya estaba de baja, no hace nada.
     *
     * @return {@code true} si esta llamada efectivamente lo dio de baja; {@code false} si ya estaba de baja
     */
    public boolean darDeBaja(LocalDate hoy) {
        if (!this.vigente) {
            return false;
        }
        this.vigente = false;
        this.vigenteHasta = Objects.requireNonNull(hoy, "la fecha de baja no puede ser nula");
        return true;
    }

    public boolean estaVigente() {
        return vigente;
    }

    public boolean esRaiz() {
        return idPadre == null;
    }

    // ----------------------------------------------------------------------
    // Validaciones internas mínimas (las de formato de DTO viven en la capa api)
    // ----------------------------------------------------------------------

    private static String exigirCodigo(String codigo) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("El código del derecho es obligatorio");
        }
        return codigo;
    }

    private static String exigirNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del derecho es obligatorio");
        }
        return nombre;
    }

    // ----------------------------------------------------------------------
    // Acceso de solo lectura (las llaves internas se exponen al dominio/persistencia,
    // NUNCA al adaptador de API)
    // ----------------------------------------------------------------------

    public Long getId() { return id; }

    /** Asigna la PK interna tras persistir (uso del mapeador/repositorio). */
    public void asignarId(Long id) { this.id = id; }

    public Long getIdPadre() { return idPadre; }

    public String getAltKey() { return altKey; }

    public String getCodigo() { return codigo; }

    public String getNombre() { return nombre; }

    public String getDescripcion() { return descripcion; }

    public short getNivel() { return nivel; }

    public short getOrden() { return orden; }

    public LocalDate getVigenteDesde() { return vigenteDesde; }

    public LocalDate getVigenteHasta() { return vigenteHasta; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Derecho otro)) return false;
        // Identidad pública estable: dos derechos son el mismo si comparten altKey.
        return altKey.equals(otro.altKey);
    }

    @Override
    public int hashCode() {
        return altKey.hashCode();
    }

    @Override
    public String toString() {
        return "Derecho{altKey=%s, codigo=%s, nivel=%d, vigente=%s}"
                .formatted(altKey, codigo, nivel, vigente);
    }
}
