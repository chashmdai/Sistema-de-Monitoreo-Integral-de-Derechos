package cl.smid.instituciones.dominio.modelo;

import cl.smid.instituciones.dominio.excepcion.ExcepcionValidacion;

import java.time.Instant;

/**
 * Agregado raíz <strong>Institucion</strong>: una entidad del catálogo nacional de
 * referencia (juzgados, hospitales, OPD, comisarías, municipios, etc.).
 *
 * <p>Invariantes propios del agregado (validados aquí):</p>
 * <ul>
 *   <li>{@code nombre} obligatorio y normalizado.</li>
 *   <li>{@code tipoAlt} obligatorio (la verificación de que el tipo exista y esté
 *       vigente es una regla de negocio que requiere el repositorio, por lo que se
 *       resuelve en el servicio de dominio: {@code INS-422}).</li>
 *   <li>{@code rut} opcional; si viene, se valida por módulo 11 (objeto de valor
 *       {@link Rut}); un RUT inválido es regla de negocio ({@code INS-422}).</li>
 *   <li>{@code email} opcional; si viene, se valida su formato ({@code INS-001}).</li>
 * </ul>
 *
 * <p>Sin máquina de estados: la baja se modela con la bandera {@code activa}.
 * El dominio trabaja en {@link Instant} (UTC).</p>
 */
public final class Institucion {

    private final String altKey;
    private String codigo;
    private String nombre;
    private String tipoAlt;
    private Rut rut;
    private String regionCodigo;
    private String comunaCodigo;
    private String direccion;
    private String telefono;
    private String email;
    private String sitioWeb;
    private boolean activa;
    private final Instant creadoEn;
    private Instant actualizadoEn;

    private Institucion(String altKey, String codigo, String nombre, String tipoAlt, Rut rut,
                        String regionCodigo, String comunaCodigo, String direccion, String telefono,
                        String email, String sitioWeb, boolean activa,
                        Instant creadoEn, Instant actualizadoEn) {
        this.altKey = altKey;
        this.codigo = codigo;
        this.nombre = nombre;
        this.tipoAlt = tipoAlt;
        this.rut = rut;
        this.regionCodigo = regionCodigo;
        this.comunaCodigo = comunaCodigo;
        this.direccion = direccion;
        this.telefono = telefono;
        this.email = email;
        this.sitioWeb = sitioWeb;
        this.activa = activa;
        this.creadoEn = creadoEn;
        this.actualizadoEn = actualizadoEn;
    }

    /**
     * Crea una institución nueva, activa, validando obligatoriedad y formatos.
     *
     * @param altKey       identificador público generado
     * @param codigo       código institucional opcional (único si viene)
     * @param nombre       nombre (obligatorio; se normaliza)
     * @param tipoAlt      alt_key del tipo de institución (obligatorio)
     * @param rut          RUT en texto, opcional (si viene se valida módulo 11)
     * @param regionCodigo código de región opcional
     * @param comunaCodigo código de comuna opcional
     * @param direccion    dirección opcional
     * @param telefono     teléfono opcional
     * @param email        correo opcional (se valida formato si viene)
     * @param sitioWeb     sitio web opcional
     * @param ahora        instante de creación (UTC)
     * @return la institución creada
     */
    public static Institucion crear(String altKey, String codigo, String nombre, String tipoAlt,
                                    String rut, String regionCodigo, String comunaCodigo,
                                    String direccion, String telefono, String email, String sitioWeb,
                                    Instant ahora) {
        if (Textos.esVacio(nombre)) {
            throw new ExcepcionValidacion("El nombre de la institución es obligatorio.");
        }
        if (Textos.esVacio(tipoAlt)) {
            throw new ExcepcionValidacion("El tipo de institución (tipoAlt) es obligatorio.");
        }
        return new Institucion(
                altKey,
                Textos.limpiarOpcional(codigo),
                Textos.normalizarNombre(nombre),
                tipoAlt.trim(),
                interpretarRut(rut),
                Textos.limpiarOpcional(regionCodigo),
                Textos.limpiarOpcional(comunaCodigo),
                Textos.limpiarOpcional(direccion),
                Textos.limpiarOpcional(telefono),
                validarEmail(email),
                Textos.limpiarOpcional(sitioWeb),
                true,
                ahora,
                ahora);
    }

    /**
     * Reconstruye una institución desde persistencia (sin re-validar).
     */
    public static Institucion reconstruir(String altKey, String codigo, String nombre, String tipoAlt,
                                          Rut rut, String regionCodigo, String comunaCodigo,
                                          String direccion, String telefono, String email, String sitioWeb,
                                          boolean activa, Instant creadoEn, Instant actualizadoEn) {
        return new Institucion(altKey, codigo, nombre, tipoAlt, rut, regionCodigo, comunaCodigo,
                direccion, telefono, email, sitioWeb, activa, creadoEn, actualizadoEn);
    }

    /**
     * Actualiza los datos editables de la institución.
     *
     * @param codigo       nuevo código opcional
     * @param nombre       nuevo nombre (obligatorio)
     * @param tipoAlt      nuevo tipo (obligatorio; la vigencia se valida en el servicio)
     * @param rut          nuevo RUT en texto, opcional
     * @param regionCodigo nuevo código de región
     * @param comunaCodigo nuevo código de comuna
     * @param direccion    nueva dirección
     * @param telefono     nuevo teléfono
     * @param email        nuevo correo (se valida formato si viene)
     * @param sitioWeb     nuevo sitio web
     * @param ahora        instante de la edición (UTC)
     */
    public void actualizar(String codigo, String nombre, String tipoAlt, String rut,
                           String regionCodigo, String comunaCodigo, String direccion, String telefono,
                           String email, String sitioWeb, Instant ahora) {
        if (Textos.esVacio(nombre)) {
            throw new ExcepcionValidacion("El nombre de la institución es obligatorio.");
        }
        if (Textos.esVacio(tipoAlt)) {
            throw new ExcepcionValidacion("El tipo de institución (tipoAlt) es obligatorio.");
        }
        this.codigo = Textos.limpiarOpcional(codigo);
        this.nombre = Textos.normalizarNombre(nombre);
        this.tipoAlt = tipoAlt.trim();
        this.rut = interpretarRut(rut);
        this.regionCodigo = Textos.limpiarOpcional(regionCodigo);
        this.comunaCodigo = Textos.limpiarOpcional(comunaCodigo);
        this.direccion = Textos.limpiarOpcional(direccion);
        this.telefono = Textos.limpiarOpcional(telefono);
        this.email = validarEmail(email);
        this.sitioWeb = Textos.limpiarOpcional(sitioWeb);
        this.actualizadoEn = ahora;
    }

    /**
     * Activa o desactiva (baja lógica) la institución.
     *
     * @param activa nuevo estado
     * @param ahora  instante del cambio (UTC)
     */
    public void establecerActiva(boolean activa, Instant ahora) {
        this.activa = activa;
        this.actualizadoEn = ahora;
    }

    private static Rut interpretarRut(String rut) {
        return Textos.esVacio(rut) ? null : Rut.desde(rut);
    }

    private static String validarEmail(String email) {
        String limpio = Textos.limpiarOpcional(email);
        if (limpio != null && !Textos.esEmailValido(limpio)) {
            throw new ExcepcionValidacion("El correo electrónico de la institución no tiene un formato válido.");
        }
        return limpio;
    }

    public String altKey() {
        return altKey;
    }

    public String codigo() {
        return codigo;
    }

    public String nombre() {
        return nombre;
    }

    public String tipoAlt() {
        return tipoAlt;
    }

    /** @return el RUT validado o {@code null} si la institución no tiene RUT registrado. */
    public Rut rut() {
        return rut;
    }

    public String regionCodigo() {
        return regionCodigo;
    }

    public String comunaCodigo() {
        return comunaCodigo;
    }

    public String direccion() {
        return direccion;
    }

    public String telefono() {
        return telefono;
    }

    public String email() {
        return email;
    }

    public String sitioWeb() {
        return sitioWeb;
    }

    public boolean activa() {
        return activa;
    }

    public Instant creadoEn() {
        return creadoEn;
    }

    public Instant actualizadoEn() {
        return actualizadoEn;
    }
}
