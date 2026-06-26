package cl.smid.catalogo.dominio.modelo;

import java.util.Objects;

/**
 * Modelo de dominio de una <b>Causa</b> vinculada a un derecho.
 *
 * <p>POJO puro del núcleo. Una causa es un motivo o forma de vulneración asociado a un
 * derecho concreto (p. ej. para el derecho "Educación · Acceso y permanencia", causas
 * como "Deserción escolar" o "No matrícula"). La unicidad del {@code codigo} es
 * <b>por derecho</b> (un mismo código puede repetirse bajo derechos distintos).</p>
 */
public class Causa {

    private Long id;             // PK BIGINT interna; null mientras no se persiste
    private final Long idDerecho; // FK interna al derecho dueño
    private final String altKey; // UUID público opaco
    private final String codigo; // único dentro del derecho
    private String nombre;
    private boolean vigente;

    public Causa(Long id, Long idDerecho, String altKey, String codigo, String nombre, boolean vigente) {
        this.id = id;
        this.idDerecho = Objects.requireNonNull(idDerecho, "idDerecho no puede ser nulo");
        this.altKey = Objects.requireNonNull(altKey, "altKey no puede ser nulo");
        this.codigo = exigir(codigo, "El código de la causa es obligatorio");
        this.nombre = exigir(nombre, "El nombre de la causa es obligatorio");
        this.vigente = vigente;
    }

    /** Crea una causa nueva, vigente, colgando del derecho indicado. */
    public static Causa nueva(Long idDerecho, String altKey, String codigo, String nombre) {
        return new Causa(null, idDerecho, altKey, codigo, nombre, true);
    }

    public void renombrar(String nuevoNombre) {
        this.nombre = exigir(nuevoNombre, "El nombre de la causa es obligatorio");
    }

    /** Da de baja lógica la causa (idempotente). */
    public boolean darDeBaja() {
        if (!vigente) {
            return false;
        }
        vigente = false;
        return true;
    }

    public boolean estaVigente() { return vigente; }

    private static String exigir(String valor, String mensaje) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(mensaje);
        }
        return valor;
    }

    public Long getId() { return id; }

    public void asignarId(Long id) { this.id = id; }

    public Long getIdDerecho() { return idDerecho; }

    public String getAltKey() { return altKey; }

    public String getCodigo() { return codigo; }

    public String getNombre() { return nombre; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Causa otra)) return false;
        return altKey.equals(otra.altKey);
    }

    @Override
    public int hashCode() {
        return altKey.hashCode();
    }
}
