package cl.smid.personas.dominio.modelo;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Agregado de dominio que representa a una persona del registro maestro (Núcleo 5.3).
 * POJO puro: sin anotaciones de framework, sin Lombok, sin JPA. La capa de persistencia lo
 * mapea desde/hacia {@code PersonaEntity} mediante un mapeador explícito.
 *
 * <p>Inmutable en la práctica: los campos son finales y la lista de contactos se expone como
 * copia no modificable. Las transiciones de estado (alta, edición) se realizan en el
 * servicio de dominio reconstruyendo instancias mediante el {@link Builder}.</p>
 *
 * <p>{@code id} es el identificador interno (BIGINT) y nunca cruza la frontera de la API:
 * hacia afuera sólo viaja {@code altKey}. {@code id} puede ser {@code null} en una persona
 * aún no persistida.</p>
 */
public final class Persona {

    private final Long id;
    private final String altKey;
    private final TipoPersona tipo;
    private final String rut;          // forma canónica "cuerpo-DV" o null
    private final String dv;           // dígito verificador o null
    private final String nombres;
    private final String apellidoPaterno;
    private final String apellidoMaterno;
    private final String razonSocial;
    private final LocalDate fechaNacimiento;
    private final Sexo sexo;
    private final String nacionalidad;
    private final String hashDedup;
    private final String idSede;
    private final String idUnidad;
    private final boolean vigente;
    private final Instant creadoEn;
    private final Instant actualizadoEn;
    private final String creadoPor;
    private final List<Contacto> contactos;

    private Persona(Builder b) {
        this.id = b.id;
        this.altKey = b.altKey;
        this.tipo = b.tipo;
        this.rut = b.rut;
        this.dv = b.dv;
        this.nombres = b.nombres;
        this.apellidoPaterno = b.apellidoPaterno;
        this.apellidoMaterno = b.apellidoMaterno;
        this.razonSocial = b.razonSocial;
        this.fechaNacimiento = b.fechaNacimiento;
        this.sexo = b.sexo;
        this.nacionalidad = b.nacionalidad;
        this.hashDedup = b.hashDedup;
        this.idSede = b.idSede;
        this.idUnidad = b.idUnidad;
        this.vigente = b.vigente;
        this.creadoEn = b.creadoEn;
        this.actualizadoEn = b.actualizadoEn;
        this.creadoPor = b.creadoPor;
        this.contactos = (b.contactos == null)
                ? Collections.emptyList()
                : Collections.unmodifiableList(new ArrayList<>(b.contactos));
    }

    /**
     * Nombre legible compacto para listados y para el dato mínimo de deduplicación.
     * Para persona jurídica usa la razón social; para personas naturales compone
     * "Nombres ApellidoPaterno". Nunca devuelve {@code null}.
     */
    public String nombreLegible() {
        if (tipo == TipoPersona.JURIDICA && razonSocial != null && !razonSocial.isBlank()) {
            return razonSocial.trim();
        }
        StringBuilder sb = new StringBuilder();
        if (nombres != null && !nombres.isBlank()) {
            sb.append(nombres.trim());
        }
        if (apellidoPaterno != null && !apellidoPaterno.isBlank()) {
            if (sb.length() > 0) {
                sb.append(' ');
            }
            sb.append(apellidoPaterno.trim());
        }
        return sb.toString();
    }

    /** Crea un builder vacío. */
    public static Builder builder() {
        return new Builder();
    }

    /** Crea un builder precargado con el estado de esta persona (para edición inmutable). */
    public Builder copia() {
        return new Builder()
                .id(id).altKey(altKey).tipo(tipo).rut(rut).dv(dv)
                .nombres(nombres).apellidoPaterno(apellidoPaterno).apellidoMaterno(apellidoMaterno)
                .razonSocial(razonSocial).fechaNacimiento(fechaNacimiento).sexo(sexo)
                .nacionalidad(nacionalidad).hashDedup(hashDedup).idSede(idSede).idUnidad(idUnidad)
                .vigente(vigente).creadoEn(creadoEn).actualizadoEn(actualizadoEn).creadoPor(creadoPor)
                .contactos(contactos);
    }

    // ----------------------------- Accesores -----------------------------
    public Long id() { return id; }
    public String altKey() { return altKey; }
    public TipoPersona tipo() { return tipo; }
    public String rut() { return rut; }
    public String dv() { return dv; }
    public String nombres() { return nombres; }
    public String apellidoPaterno() { return apellidoPaterno; }
    public String apellidoMaterno() { return apellidoMaterno; }
    public String razonSocial() { return razonSocial; }
    public LocalDate fechaNacimiento() { return fechaNacimiento; }
    public Sexo sexo() { return sexo; }
    public String nacionalidad() { return nacionalidad; }
    public String hashDedup() { return hashDedup; }
    public String idSede() { return idSede; }
    public String idUnidad() { return idUnidad; }
    public boolean vigente() { return vigente; }
    public Instant creadoEn() { return creadoEn; }
    public Instant actualizadoEn() { return actualizadoEn; }
    public String creadoPor() { return creadoPor; }
    public List<Contacto> contactos() { return contactos; }

    /** Builder fluido del agregado. */
    public static final class Builder {
        private Long id;
        private String altKey;
        private TipoPersona tipo;
        private String rut;
        private String dv;
        private String nombres;
        private String apellidoPaterno;
        private String apellidoMaterno;
        private String razonSocial;
        private LocalDate fechaNacimiento;
        private Sexo sexo;
        private String nacionalidad;
        private String hashDedup;
        private String idSede;
        private String idUnidad;
        private boolean vigente = true;
        private Instant creadoEn;
        private Instant actualizadoEn;
        private String creadoPor;
        private List<Contacto> contactos;

        public Builder id(Long v) { this.id = v; return this; }
        public Builder altKey(String v) { this.altKey = v; return this; }
        public Builder tipo(TipoPersona v) { this.tipo = v; return this; }
        public Builder rut(String v) { this.rut = v; return this; }
        public Builder dv(String v) { this.dv = v; return this; }
        public Builder nombres(String v) { this.nombres = v; return this; }
        public Builder apellidoPaterno(String v) { this.apellidoPaterno = v; return this; }
        public Builder apellidoMaterno(String v) { this.apellidoMaterno = v; return this; }
        public Builder razonSocial(String v) { this.razonSocial = v; return this; }
        public Builder fechaNacimiento(LocalDate v) { this.fechaNacimiento = v; return this; }
        public Builder sexo(Sexo v) { this.sexo = v; return this; }
        public Builder nacionalidad(String v) { this.nacionalidad = v; return this; }
        public Builder hashDedup(String v) { this.hashDedup = v; return this; }
        public Builder idSede(String v) { this.idSede = v; return this; }
        public Builder idUnidad(String v) { this.idUnidad = v; return this; }
        public Builder vigente(boolean v) { this.vigente = v; return this; }
        public Builder creadoEn(Instant v) { this.creadoEn = v; return this; }
        public Builder actualizadoEn(Instant v) { this.actualizadoEn = v; return this; }
        public Builder creadoPor(String v) { this.creadoPor = v; return this; }
        public Builder contactos(List<Contacto> v) { this.contactos = v; return this; }

        public Persona construir() {
            return new Persona(this);
        }
    }
}
