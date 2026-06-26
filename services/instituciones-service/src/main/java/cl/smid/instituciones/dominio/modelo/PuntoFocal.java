package cl.smid.instituciones.dominio.modelo;

import cl.smid.instituciones.dominio.excepcion.ExcepcionValidacion;

import java.time.Instant;

/**
 * Entidad <strong>PuntoFocal</strong>: contacto de referencia de una {@link Institucion}.
 *
 * <p>Pertenece a una institución (referida por {@code institucionAlt}). Invariante a
 * nivel de agregado de institución (garantizado por el servicio dentro de la
 * transacción): a lo sumo <strong>un</strong> punto focal {@code principal} y
 * {@code activo} por institución.</p>
 *
 * <p>Invariantes propios:</p>
 * <ul>
 *   <li>{@code institucionAlt} obligatorio.</li>
 *   <li>{@code nombre} obligatorio y normalizado.</li>
 *   <li>{@code email} opcional; si viene, se valida su formato ({@code INS-001}).</li>
 * </ul>
 */
public final class PuntoFocal {

    private final String altKey;
    private final String institucionAlt;
    private String nombre;
    private String cargo;
    private String email;
    private String telefono;
    private boolean principal;
    private boolean activo;
    private final Instant creadoEn;
    private Instant actualizadoEn;

    private PuntoFocal(String altKey, String institucionAlt, String nombre, String cargo, String email,
                       String telefono, boolean principal, boolean activo,
                       Instant creadoEn, Instant actualizadoEn) {
        this.altKey = altKey;
        this.institucionAlt = institucionAlt;
        this.nombre = nombre;
        this.cargo = cargo;
        this.email = email;
        this.telefono = telefono;
        this.principal = principal;
        this.activo = activo;
        this.creadoEn = creadoEn;
        this.actualizadoEn = actualizadoEn;
    }

    /**
     * Crea un punto focal nuevo, activo.
     *
     * @param altKey         identificador público generado
     * @param institucionAlt alt_key de la institución a la que pertenece (obligatorio)
     * @param nombre         nombre (obligatorio; se normaliza)
     * @param cargo          cargo opcional
     * @param email          correo opcional (se valida formato si viene)
     * @param telefono       teléfono opcional
     * @param principal      si es el contacto principal
     * @param ahora          instante de creación (UTC)
     * @return el punto focal creado
     */
    public static PuntoFocal crear(String altKey, String institucionAlt, String nombre, String cargo,
                                   String email, String telefono, boolean principal, Instant ahora) {
        if (Textos.esVacio(institucionAlt)) {
            throw new ExcepcionValidacion("La institución del punto focal es obligatoria.");
        }
        if (Textos.esVacio(nombre)) {
            throw new ExcepcionValidacion("El nombre del punto focal es obligatorio.");
        }
        return new PuntoFocal(
                altKey,
                institucionAlt.trim(),
                Textos.normalizarNombre(nombre),
                Textos.limpiarOpcional(cargo),
                validarEmail(email),
                Textos.limpiarOpcional(telefono),
                principal,
                true,
                ahora,
                ahora);
    }

    /**
     * Reconstruye un punto focal desde persistencia (sin re-validar).
     */
    public static PuntoFocal reconstruir(String altKey, String institucionAlt, String nombre, String cargo,
                                         String email, String telefono, boolean principal, boolean activo,
                                         Instant creadoEn, Instant actualizadoEn) {
        return new PuntoFocal(altKey, institucionAlt, nombre, cargo, email, telefono,
                principal, activo, creadoEn, actualizadoEn);
    }

    /**
     * Actualiza los datos editables del punto focal.
     *
     * @param nombre    nuevo nombre (obligatorio)
     * @param cargo     nuevo cargo opcional
     * @param email     nuevo correo opcional (se valida formato si viene)
     * @param telefono  nuevo teléfono opcional
     * @param principal nuevo estado de "principal"
     * @param ahora     instante de la edición (UTC)
     */
    public void actualizar(String nombre, String cargo, String email, String telefono,
                           boolean principal, Instant ahora) {
        if (Textos.esVacio(nombre)) {
            throw new ExcepcionValidacion("El nombre del punto focal es obligatorio.");
        }
        this.nombre = Textos.normalizarNombre(nombre);
        this.cargo = Textos.limpiarOpcional(cargo);
        this.email = validarEmail(email);
        this.telefono = Textos.limpiarOpcional(telefono);
        this.principal = principal;
        this.actualizadoEn = ahora;
    }

    /**
     * Activa o desactiva el punto focal.
     *
     * @param activo nuevo estado
     * @param ahora  instante del cambio (UTC)
     */
    public void establecerActivo(boolean activo, Instant ahora) {
        this.activo = activo;
        this.actualizadoEn = ahora;
    }

    /**
     * Marca este punto focal como principal (usado al resolver la invariante de unicidad).
     *
     * @param ahora instante del cambio (UTC)
     */
    public void marcarPrincipal(Instant ahora) {
        this.principal = true;
        this.actualizadoEn = ahora;
    }

    /**
     * Quita la condición de principal a este punto focal.
     *
     * @param ahora instante del cambio (UTC)
     */
    public void quitarPrincipal(Instant ahora) {
        this.principal = false;
        this.actualizadoEn = ahora;
    }

    private static String validarEmail(String email) {
        String limpio = Textos.limpiarOpcional(email);
        if (limpio != null && !Textos.esEmailValido(limpio)) {
            throw new ExcepcionValidacion("El correo electrónico del punto focal no tiene un formato válido.");
        }
        return limpio;
    }

    public String altKey() {
        return altKey;
    }

    public String institucionAlt() {
        return institucionAlt;
    }

    public String nombre() {
        return nombre;
    }

    public String cargo() {
        return cargo;
    }

    public String email() {
        return email;
    }

    public String telefono() {
        return telefono;
    }

    public boolean principal() {
        return principal;
    }

    public boolean activo() {
        return activo;
    }

    public Instant creadoEn() {
        return creadoEn;
    }

    public Instant actualizadoEn() {
        return actualizadoEn;
    }
}
