package cl.smid.esnna.entity;

import cl.smid.esnna.domain.EstadoGestion;
import cl.smid.esnna.domain.RespuestaSiNo;
import cl.smid.esnna.domain.Semaforo;
import cl.smid.esnna.domain.SexoNna;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Caso ESNNA. Cambios estructurales respecto del modelo original:
 *
 *  - Tipos correctos: fechas -> LocalDate, edad -> Integer, SÍ/NO -> RespuestaSiNo,
 *    semáforo/estado/sexo -> enums (MOD-1, MOD-2). El semáforo ya no es String libre.
 *  - Semáforo desdoblado (BIZ-5): semaforoIA inmutable (lo que computó el backend)
 *    vs semaforoFinal (override humano, con autor y fecha). Permite auditar overrides
 *    y medir la calibración del motor.
 *  - Imputados como relación 1:N a Imputado (MOD-3), no tres listas paralelas.
 *  - @Version para optimistic locking (MOD-5): evita lost update en edición concurrente.
 *  - fechaIngreso/fechaActualizacion por Hibernate (MOD-6).
 *  - Borrado lógico (anulado + motivo + autor): nunca DELETE físico en dominio legal.
 *  - Índices para el dashboard y la deduplicación (MOD-4).
 *
 * CTRL-2: fechaIngreso y estadoGestion ya NO deben setearse desde el controller.
 * fechaIngreso la gestiona @CreationTimestamp; estadoGestion tiene su default aquí.
 */
@Entity
@Table(name = "esnna_casos", indexes = {
        @Index(name = "idx_caso_semaforo_final", columnList = "semaforo_final"),
        @Index(name = "idx_caso_estado", columnList = "estado_gestion"),
        @Index(name = "idx_caso_region", columnList = "region"),
        @Index(name = "idx_caso_fecha_ingreso", columnList = "fecha_ingreso"),
        @Index(name = "idx_caso_nro_oficio", columnList = "nro_oficio"),
        @Index(name = "idx_caso_cedula_nna", columnList = "cedula_nna"),
        @Index(name = "idx_caso_ruc", columnList = "ruc_asociados"),
        @Index(name = "idx_caso_anulado", columnList = "anulado")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class EsnnaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @ToString.Include
    private Long id;

    /** Optimistic locking: dos profesionales editando el mismo caso no se pisan en silencio. */
    @Version
    @Column(name = "version")
    private Long version;

    // ==========================================
    // CONTROL IA Y SEMÁFORO (BIZ-5: IA inmutable vs decisión humana)
    // ==========================================

    /** Semáforo computado por el backend desde la evidencia de la IA. No lo cambia el humano. */
    @ToString.Include
    @Enumerated(EnumType.STRING)
    @Column(name = "semaforo_ia", length = 10, nullable = false)
    private Semaforo semaforoIa;

    @Column(name = "justificacion_ia", columnDefinition = "TEXT")
    private String justificacionIa;

    @Column(name = "confianza_ia")
    private Double confianzaIa;

    /** Semáforo que el profesional confirma o corrige. Es el que rige el dashboard. */
    @ToString.Include
    @Enumerated(EnumType.STRING)
    @Column(name = "semaforo_final", length = 10)
    private Semaforo semaforoFinal;

    @Column(name = "semaforo_final_autor", length = 20)
    private String semaforoFinalAutor;

    @Column(name = "semaforo_final_fecha")
    private LocalDateTime semaforoFinalFecha;

    // ==========================================
    // DATOS DE GESTIÓN
    // ==========================================
    @Column(name = "para_querella", length = 100)
    private String paraQuerella;

    @Column(name = "requerimiento", length = 255)
    private String requerimiento;

    @Column(name = "nro_correlativo", length = 100)
    private String nroCorrelativo;

    @Column(name = "fecha")
    private LocalDate fecha;

    @ToString.Include
    @Column(name = "nro_oficio", length = 100)
    private String nroOficio;

    @Column(name = "carpeta", length = 100)
    private String carpeta;

    @Column(name = "region", length = 100)
    private String region;

    @Column(name = "tipo_programa", length = 150)
    private String tipoPrograma;

    @Column(name = "nombre_programa_residencia", length = 255)
    private String nombreProgramaResidencia;

    // ==========================================
    // CONTEXTO NNA  (decisión #2: nna = nombre completo)
    // ==========================================
    @Column(name = "delito_concreto", length = 255)
    private String delitoConcreto;

    @Enumerated(EnumType.STRING)
    @Column(name = "nna_bajo_cuidado_estado", length = 2)
    private RespuestaSiNo nnaBajoCuidadoEstado;

    @Column(name = "residencia", length = 255)
    private String residencia;

    @Column(name = "denunciante", length = 255)
    private String denunciante;

    @Column(name = "contacto_denunciante", length = 255)
    private String contactoDenunciante;

    /** Nombre completo del NNA (decisión #2). Sin cifrado por decisión #3 (deuda reconocida). */
    @Column(name = "nna", length = 255)
    private String nna;

    @Enumerated(EnumType.STRING)
    @Column(name = "sexo_nna", length = 1)
    private SexoNna sexoNna;

    @Column(name = "cedula_nna", length = 20)
    private String cedulaNna;

    @Column(name = "nacionalidad_nna", length = 100)
    private String nacionalidadNna;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @Column(name = "edad")
    private Integer edad;

    @Column(name = "consumo_drogas_alcohol", length = 100)
    private String consumoDrogasAlcohol;

    @Column(name = "curador", length = 255)
    private String curador;

    @Column(name = "nad_pma", length = 255)
    private String nadPma;

    @Column(name = "contacto_nad_pma", length = 255)
    private String contactoNadPma;

    // ==========================================
    // IMPUTADOS (MOD-3: relación, no listas paralelas)
    // ==========================================
    @Enumerated(EnumType.STRING)
    @Column(name = "imputado_conocido", length = 2)
    private RespuestaSiNo imputadoConocido;

    @OneToMany(mappedBy = "caso", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Imputado> imputados = new ArrayList<>();

    // ==========================================
    // HECHOS Y LUGARES
    // ==========================================
    @Column(name = "lugar_ocurrencia_hechos", columnDefinition = "TEXT")
    private String lugarOcurrenciaHechos;

    @Column(name = "comunas_involucradas", columnDefinition = "TEXT")
    private String comunasInvolucradas;

    @Column(name = "hechos", columnDefinition = "LONGTEXT")
    private String hechos;

    @Column(name = "tipo_violencia", length = 255)
    private String tipoViolencia;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "redes_sociales_mencionadas", columnDefinition = "json")
    private List<String> redesSocialesMencionadas;

    @Column(name = "identificacion_locales_bares_hoteles", columnDefinition = "TEXT")
    private String identificacionLocalesBaresHoteles;

    @Enumerated(EnumType.STRING)
    @Column(name = "presunta_red_explotacion", length = 2)
    private RespuestaSiNo presuntaRedExplotacion;

    // ==========================================
    // SEGUIMIENTO LEGAL
    // ==========================================
    @Column(name = "observacion", columnDefinition = "TEXT")
    private String observacion;

    @Column(name = "querella", length = 255)
    private String querella;

    @Column(name = "denuncias_anteriores", columnDefinition = "TEXT")
    private String denunciasAnteriores;

    @Column(name = "ruc_asociados", length = 255)
    private String rucAsociados;

    @Column(name = "gestiones", columnDefinition = "TEXT")
    private String gestiones;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "pendiente", length = 255)
    private String pendiente;

    // ==========================================
    // METADATOS DEL SISTEMA
    // ==========================================
    @CreationTimestamp
    @Column(name = "fecha_ingreso", updatable = false)
    private LocalDateTime fechaIngreso;

    @UpdateTimestamp
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    /** RUT del profesional que creó el caso (del JWT, CTRL-9). */
    @Column(name = "creado_por", length = 20, updatable = false)
    private String creadoPor;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_gestion", length = 30, nullable = false)
    @Builder.Default
    private EstadoGestion estadoGestion = EstadoGestion.PENDIENTE_REVISION;

    // ==========================================
    // BORRADO LÓGICO (nunca DELETE físico)
    // ==========================================
    @Column(name = "anulado", nullable = false)
    @Builder.Default
    private boolean anulado = false;

    @Column(name = "motivo_anulacion", length = 500)
    private String motivoAnulacion;

    @Column(name = "anulado_por", length = 20)
    private String anuladoPor;

    @Column(name = "fecha_anulacion")
    private LocalDateTime fechaAnulacion;

    // ------------------------------------------
    // Helpers de consistencia de la relación con Imputado.
    // Mantienen ambos lados sincronizados (necesario con orphanRemoval).
    // ------------------------------------------
    public void addImputado(Imputado imputado) {
        imputado.setCaso(this);
        this.imputados.add(imputado);
    }

    public void clearImputados() {
        this.imputados.forEach(i -> i.setCaso(null));
        this.imputados.clear();
    }
}
