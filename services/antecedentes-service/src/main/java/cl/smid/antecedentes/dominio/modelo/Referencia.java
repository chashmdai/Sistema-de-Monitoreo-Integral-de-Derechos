package cl.smid.antecedentes.dominio.modelo;

import java.time.Instant;

/**
 * Item de una tabla de referencia local ({@link TipoReferencia}). POJO puro de dominio:
 * sin anotaciones de Spring/JPA/Lombok. Encapsula sus invariantes y las operaciones de
 * edicion y cambio de vigencia.
 */
public class Referencia {

    private final String altKey;
    private final TipoReferencia tipo;
    private final String codigo;
    private String nombre;
    private boolean vigente;
    private final Instant creadoEn;
    private Instant actualizadoEn;

    /**
     * Reconstruye una referencia desde persistencia (todos los campos conocidos).
     */
    public Referencia(String altKey, TipoReferencia tipo, String codigo, String nombre,
                      boolean vigente, Instant creadoEn, Instant actualizadoEn) {
        if (altKey == null || altKey.isBlank()) {
            throw new IllegalArgumentException("El alt_key de la referencia es obligatorio");
        }
        if (tipo == null) {
            throw new IllegalArgumentException("El tipo de referencia es obligatorio");
        }
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("El codigo de la referencia es obligatorio");
        }
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre de la referencia es obligatorio");
        }
        if (creadoEn == null || actualizadoEn == null) {
            throw new IllegalArgumentException("Las marcas temporales de la referencia son obligatorias");
        }
        this.altKey = altKey;
        this.tipo = tipo;
        this.codigo = codigo.trim();
        this.nombre = nombre.trim();
        this.vigente = vigente;
        this.creadoEn = creadoEn;
        this.actualizadoEn = actualizadoEn;
    }

    /**
     * Crea una referencia nueva (vigente por defecto) con sus marcas temporales iniciales.
     */
    public static Referencia crear(String altKey, TipoReferencia tipo, String codigo, String nombre, Instant ahora) {
        return new Referencia(altKey, tipo, codigo, nombre, true, ahora, ahora);
    }

    /** Actualiza el nombre. El codigo es inmutable (identificador estable). */
    public void editar(String nuevoNombre, Instant ahora) {
        if (nuevoNombre == null || nuevoNombre.isBlank()) {
            throw new IllegalArgumentException("El nombre de la referencia es obligatorio");
        }
        this.nombre = nuevoNombre.trim();
        this.actualizadoEn = ahora;
    }

    /** Activa o desactiva la referencia. */
    public void cambiarVigencia(boolean vigente, Instant ahora) {
        this.vigente = vigente;
        this.actualizadoEn = ahora;
    }

    public String altKey() {
        return altKey;
    }

    public TipoReferencia tipo() {
        return tipo;
    }

    public String codigo() {
        return codigo;
    }

    public String nombre() {
        return nombre;
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
